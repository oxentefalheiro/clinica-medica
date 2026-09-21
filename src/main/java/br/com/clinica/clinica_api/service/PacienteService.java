    package br.com.clinica.clinica_api.service;

    import br.com.clinica.clinica_api.repository.PacienteRepository;
    import org.springframework.stereotype.Service;
    import br.com.clinica.clinica_api.model.Paciente;

    @Service
    public class PacienteService {
        private final PacienteRepository pacienteRepository;

        public PacienteService(PacienteRepository pacienteRepository) {
            this.pacienteRepository = pacienteRepository;
        }

        public Paciente cadastrar(Paciente paciente) {
            return pacienteRepository.save(paciente);
        }



    }
