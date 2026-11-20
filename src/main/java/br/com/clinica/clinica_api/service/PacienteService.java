    package br.com.clinica.clinica_api.service;

    import br.com.clinica.clinica_api.dto.PacienteUpdateDTO;
    import br.com.clinica.clinica_api.repository.PacienteRepository;
    import jakarta.transaction.Transactional;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.stereotype.Service;
    import br.com.clinica.clinica_api.model.Paciente;

    import java.util.List;

    @Service
    public class PacienteService {
        private final PacienteRepository pacienteRepository;

        public PacienteService(PacienteRepository pacienteRepository) {
            this.pacienteRepository = pacienteRepository;
        }

        public Paciente buscarPorId(Long id) {
            return pacienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Paciente não encontrado com o ID" + id));
        }

        public Page<Paciente> listarTodos(Pageable paginacao) {
            return pacienteRepository.findAll(paginacao);
        }

        public Paciente cadastrar(Paciente paciente) {
            if (pacienteRepository.existsByCpf(paciente.getCpf())){
                throw new RuntimeException("Já existe uma paciente cadastrado com este CPF ");
            }

            return pacienteRepository.save(paciente);
        }

        @Transactional
        public Paciente atualizar(PacienteUpdateDTO pacienteDto) {
            //Buscamos o paciente no B.D com o ID obrigatório
            Paciente paciente = pacienteRepository.findById(pacienteDto.getId()).orElseThrow(() -> new RuntimeException("Paciente com o ID " + pacienteDto + "não encontrado."));

            //1° Verifica se o nome está nulo ou com caracteres em branco/espaços
            if(pacienteDto.getNome() != null && !pacienteDto.getNome().trim().isEmpty()) {
                paciente.setNome(pacienteDto.getNome());
            }

            //2° Verificamos se foi enviado atualização corretamente para o n° de telefone cadastrado
            if(pacienteDto.getTelefone() != null && !pacienteDto.getTelefone().trim().isEmpty()) {
                paciente.setTelefone(pacienteDto.getTelefone());
            }

            return pacienteRepository.save(paciente);

        }


    }
