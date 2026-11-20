package br.com.clinica.clinica_api.repository;

import br.com.clinica.clinica_api.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicoRepository extends JpaRepository <Medico, Long> {

    List<Medico> findByEspecialidade (String especialidade);
    List<Medico> findByDiaAtendimento (LocalDate diaAtendimento);
    boolean existsByCrm (String crm);
}
