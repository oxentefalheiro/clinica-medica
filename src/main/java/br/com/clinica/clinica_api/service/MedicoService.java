package br.com.clinica.clinica_api.service;

import br.com.clinica.clinica_api.dto.MedicoRequestDTO;
import br.com.clinica.clinica_api.dto.MedicoUpdateDTO;
import br.com.clinica.clinica_api.exception.ConflitoException;
import br.com.clinica.clinica_api.exception.RecursoNaoEncontradoException;
import br.com.clinica.clinica_api.exception.RegraNegocioException;
import br.com.clinica.clinica_api.model.Especialidade;
import br.com.clinica.clinica_api.model.Medico;
import br.com.clinica.clinica_api.repository.MedicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class MedicoService {

    private static final LocalTime ABERTURA = LocalTime.of(7, 0);
    private static final LocalTime FECHAMENTO = LocalTime.of(19, 0);

    private final MedicoRepository medicoRepository;
    private final EspecialidadeService especialidadeService;

    public MedicoService(MedicoRepository medicoRepository, EspecialidadeService especialidadeService) {
        this.medicoRepository = medicoRepository;
        this.especialidadeService = especialidadeService;
    }

    public Medico buscarPorId(Long id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Médico não encontrado com o ID " + id));
    }

    public Page<Medico> listarTodos(Pageable paginacao) {
        return medicoRepository.findAll(paginacao);
    }

    public Page<Medico> listarPorEspecialidade(Long especialidadeId, Pageable paginacao) {
        especialidadeService.buscarPorId(especialidadeId); // 404 se a especialidade não existir
        return medicoRepository.findByEspecialidadeId(especialidadeId, paginacao);
    }

    public Medico cadastrar(MedicoRequestDTO dto) {
        if (medicoRepository.existsByCrm(dto.getCrm())) {
            throw new ConflitoException("Já existe um médico cadastrado com este CRM.");
        }
        validarJornada(dto.getHorarioInicio(), dto.getHorarioFim());

        // Busca a especialidade pelo ID recebido no DTO (relacionamento N:1)
        Especialidade especialidade = especialidadeService.buscarPorId(dto.getEspecialidadeId());

        Medico medico = new Medico();
        medico.setNome(dto.getNome());
        medico.setCrm(dto.getCrm().toUpperCase());
        medico.setDiaAtendimento(dto.getDiaAtendimento());
        medico.setHorarioInicio(dto.getHorarioInicio());
        medico.setHorarioFim(dto.getHorarioFim());
        medico.setEspecialidade(especialidade);
        return medicoRepository.save(medico);
    }

    @Transactional
    public Medico atualizar(Long id, MedicoUpdateDTO dto) {
        Medico medico = buscarPorId(id);

        if (dto.getNome() != null && !dto.getNome().trim().isEmpty()) {
            medico.setNome(dto.getNome().trim());
        }
        if (dto.getEspecialidadeId() != null) {
            medico.setEspecialidade(especialidadeService.buscarPorId(dto.getEspecialidadeId()));
        }
        if (dto.getDiaAtendimento() != null) {
            medico.setDiaAtendimento(dto.getDiaAtendimento());
        }
        if (dto.getHorarioInicio() != null) {
            medico.setHorarioInicio(dto.getHorarioInicio());
        }
        if (dto.getHorarioFim() != null) {
            medico.setHorarioFim(dto.getHorarioFim());
        }
        validarJornada(medico.getHorarioInicio(), medico.getHorarioFim());

        return medicoRepository.save(medico);
    }

    @Transactional
    public Medico inativar(Long id) {
        Medico medico = buscarPorId(id);
        if (!medico.isAtivo()) {
            throw new ConflitoException("O médico já está inativo.");
        }
        medico.setAtivo(false);
        return medicoRepository.save(medico);
    }

    // Regra: a jornada do médico precisa estar dentro do funcionamento da clínica (07h às 19h)
    private void validarJornada(LocalTime inicio, LocalTime fim) {
        if (inicio == null || fim == null) {
            return;
        }
        if (!inicio.isBefore(fim)) {
            throw new RegraNegocioException("O horário de início deve ser anterior ao horário de fim.");
        }
        if (inicio.isBefore(ABERTURA) || fim.isAfter(FECHAMENTO)) {
            throw new RegraNegocioException("A jornada do médico deve estar entre 07:00 e 19:00.");
        }
    }
}
