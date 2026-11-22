package br.com.clinica.clinica_api.service;

import br.com.clinica.clinica_api.dto.PacienteRequestDTO;
import br.com.clinica.clinica_api.dto.PacienteUpdateDTO;
import br.com.clinica.clinica_api.exception.ConflitoException;
import br.com.clinica.clinica_api.exception.RecursoNaoEncontradoException;
import br.com.clinica.clinica_api.model.Paciente;
import br.com.clinica.clinica_api.repository.PacienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Paciente buscarPorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado com o ID " + id));
    }

    public Page<Paciente> listarTodos(Pageable paginacao) {
        return pacienteRepository.findAll(paginacao);
    }

    public Paciente cadastrar(PacienteRequestDTO dto) {
        if (pacienteRepository.existsByCpf(dto.getCpf())) {
            throw new ConflitoException("Já existe um paciente cadastrado com este CPF.");
        }
        // Conversão DTO -> Entidade
        Paciente paciente = new Paciente(dto.getNome(), dto.getDataNascimento(), dto.getTelefone(), dto.getCpf());
        return pacienteRepository.save(paciente);
    }

    @Transactional
    public Paciente atualizar(Long id, PacienteUpdateDTO dto) {
        Paciente paciente = buscarPorId(id);

        // 1° Só altera o nome se foi enviado e não está em branco
        if (dto.getNome() != null && !dto.getNome().trim().isEmpty()) {
            paciente.setNome(dto.getNome().trim());
        }

        // 2° Só altera o telefone se foi enviado e não está em branco
        if (dto.getTelefone() != null && !dto.getTelefone().trim().isEmpty()) {
            paciente.setTelefone(dto.getTelefone().trim());
        }

        return pacienteRepository.save(paciente);
    }

    @Transactional
    public Paciente inativar(Long id) {
        Paciente paciente = buscarPorId(id);
        if (!paciente.isAtivo()) {
            throw new ConflitoException("O paciente já está inativo.");
        }
        paciente.setAtivo(false);
        return pacienteRepository.save(paciente);
    }
}
