package br.com.clinica.clinica_api.service;

import br.com.clinica.clinica_api.dto.MedicoRequestDTO;
import br.com.clinica.clinica_api.dto.MedicoUpdateDTO;
import br.com.clinica.clinica_api.model.Especialidade;
import br.com.clinica.clinica_api.model.Medico;
import br.com.clinica.clinica_api.repository.MedicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final EspecialidadeService especialidadeService;
    public MedicoService(MedicoRepository medicoRepository, EspecialidadeService especialidadeService) {
        this.medicoRepository = medicoRepository;
        this.especialidadeService = especialidadeService;
    }

    public Medico buscarPorId (Long id) {
        return medicoRepository.findById(id).orElseThrow(() -> new RuntimeException("Médico não encontrado com o ID " + id));
    }

    public Page<Medico> listarTodos(Pageable paginacao) {
        return medicoRepository.findAll(paginacao);
    }

    public Medico cadastrar(MedicoRequestDTO dto) {
        if(medicoRepository.existsByCrm(dto.getCrm())){
            throw new RuntimeException("Já existe um médico cadastrado com este CRM.");
        }

        //Aqui ele faz a busca da especialidade buscando direto do DTO
        Especialidade especialidade = especialidadeService.buscarPorId(dto.getEspecialidadeId());

        Medico medico = new Medico();
        medico.setNome(dto.getNome());
        medico.setCrm(dto.getCrm());
        medico.setDiaAtendimento(dto.getDiaAtendimento());
        medico.setHorarioInicio(dto.getHorarioInicio());
        medico.setHorarioFim(dto.getHorarioFim());

        medico.setEspecialidade(especialidade);
        return medicoRepository.save(medico);
    }

    @Transactional
    public Medico atualizar(MedicoUpdateDTO medicoDto) {
        //Buscamos o paciente no B.D com o ID obrigatório
        Medico medico = medicoRepository.findById(medicoDto.getId()).orElseThrow(() -> new RuntimeException("Paciente com o ID não encontrado. "));

        //1° Verifica se o nome está nulo ou com caracteres em branco/espaços
        if(medicoDto.getNome() != null && !medicoDto.getNome().trim().isEmpty()) {
            medico.setNome(medicoDto.getNome());
        }

        //2° Verifica se o dia do atendimento está nulo ou com caracteres em branco/espaços
        if(medicoDto.getDiaAtendimento() != null) {
            medico.setDiaAtendimento(medicoDto.getDiaAtendimento());
        }

        //3° Verifica se o nome está nulo ou com caracteres em branco/espaços
        if(medicoDto.getHoraInicio() != null) {
            medico.setHorarioInicio(medicoDto.getHoraInicio());
        }

        //4° Verifica se o nome está nulo ou com caracteres em branco/espaços
        if(medicoDto.getHoraFim() != null) {
            medico.setHorarioFim(medicoDto.getHoraFim());
        }

        return medicoRepository.save(medico);
    }


}
