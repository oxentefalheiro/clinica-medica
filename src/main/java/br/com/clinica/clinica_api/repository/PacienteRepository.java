package br.com.clinica.clinica_api.repository;

import br.com.clinica.clinica_api.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    boolean existsByCpf (String cpf);



}
