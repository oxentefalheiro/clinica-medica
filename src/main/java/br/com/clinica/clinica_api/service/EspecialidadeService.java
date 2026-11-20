package br.com.clinica.clinica_api.service;

import br.com.clinica.clinica_api.model.Especialidade;
import br.com.clinica.clinica_api.repository.EspecialidadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecialidadeService {

    private final EspecialidadeRepository especialidadeRepository;

    public EspecialidadeService(EspecialidadeRepository especialidadeRepository) {
        this.especialidadeRepository = especialidadeRepository;
    }

    public Especialidade buscarPorId(Long id) {
        return especialidadeRepository.findById(id).orElseThrow(() -> new RuntimeException("Especialidade com o ID" + id + " não encontrado."));
    }

    public List<Especialidade> listarTodas() {
        return especialidadeRepository.findAll();
    }

    public List<Especialidade> listarEspecialidade(String nome) {
        return especialidadeRepository.findByNome(nome);
    }

    public Especialidade cadastrar(Especialidade especialidade) {
        if (especialidadeRepository.existsByNome(especialidade.getNome())) {
            throw new RuntimeException("Especialidade já cadastrada.");
        }

        return especialidadeRepository.save(especialidade);

    }
}
