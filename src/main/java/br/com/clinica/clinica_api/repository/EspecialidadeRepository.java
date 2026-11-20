package br.com.clinica.clinica_api.repository;

import br.com.clinica.clinica_api.model.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {

    List<Especialidade> findByNome (String nome);
    boolean existsByNome (String nome);
}
