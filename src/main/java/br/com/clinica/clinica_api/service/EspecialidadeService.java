package br.com.clinica.clinica_api.service;

import br.com.clinica.clinica_api.dto.EspecialidadeRequestDTO;
import br.com.clinica.clinica_api.exception.ConflitoException;
import br.com.clinica.clinica_api.exception.RecursoNaoEncontradoException;
import br.com.clinica.clinica_api.model.Especialidade;
import br.com.clinica.clinica_api.repository.EspecialidadeRepository;
import br.com.clinica.clinica_api.repository.MedicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecialidadeService {

    private final EspecialidadeRepository especialidadeRepository;
    private final MedicoRepository medicoRepository;

    public EspecialidadeService(EspecialidadeRepository especialidadeRepository, MedicoRepository medicoRepository) {
        this.especialidadeRepository = especialidadeRepository;
        this.medicoRepository = medicoRepository;
    }

    public Especialidade buscarPorId(Long id) {
        return especialidadeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Especialidade com o ID " + id + " não encontrada."));
    }

    public List<Especialidade> listarTodas() {
        return especialidadeRepository.findAll();
    }

    public Especialidade cadastrar(EspecialidadeRequestDTO dto) {
        if (especialidadeRepository.existsByNomeIgnoreCase(dto.getNome().trim())) {
            throw new ConflitoException("Especialidade já cadastrada.");
        }
        Especialidade especialidade = new Especialidade();
        especialidade.setNome(dto.getNome().trim());
        return especialidadeRepository.save(especialidade);
    }

    @Transactional
    public Especialidade atualizar(Long id, EspecialidadeRequestDTO dto) {
        Especialidade especialidade = buscarPorId(id);
        String novoNome = dto.getNome().trim();
        if (!especialidade.getNome().equalsIgnoreCase(novoNome) && especialidadeRepository.existsByNomeIgnoreCase(novoNome)) {
            throw new ConflitoException("Já existe outra especialidade com este nome.");
        }
        especialidade.setNome(novoNome);
        return especialidadeRepository.save(especialidade);
    }

    public void excluir(Long id) {
        Especialidade especialidade = buscarPorId(id);
        // Regra: não excluir especialidade que ainda tem médicos vinculados (protege a FK)
        if (medicoRepository.existsByEspecialidade(especialidade)) {
            throw new ConflitoException("Não é possível excluir: existem médicos vinculados a esta especialidade.");
        }
        especialidadeRepository.delete(especialidade);
    }
}
