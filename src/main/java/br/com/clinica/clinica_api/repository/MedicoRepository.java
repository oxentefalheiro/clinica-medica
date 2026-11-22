package br.com.clinica.clinica_api.repository;

import br.com.clinica.clinica_api.model.Especialidade;
import br.com.clinica.clinica_api.model.Medico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicoRepository extends JpaRepository <Medico, Long> {

    Page<Medico> findByEspecialidadeId (Long especialidadeId, Pageable paginacao);
    boolean existsByCrm (String crm);
    boolean existsByEspecialidade (Especialidade especialidade);
}
