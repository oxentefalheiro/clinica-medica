package br.com.clinica.clinica_api.repository;

import br.com.clinica.clinica_api.enums.StatusAgendamento;
import br.com.clinica.clinica_api.model.Agendamento;
import br.com.clinica.clinica_api.model.Medico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    Page<Agendamento> findByStatusAgendamento (StatusAgendamento status, Pageable paginacao);
    Page<Agendamento> findByHoraConsulta (LocalTime horaConsulta, Pageable paginacao);
    Page<Agendamento> findByDataConsulta (LocalDate dataConsulta, Pageable paginacao);
    boolean existsByMedicoAndHoraConsultaAndDataConsulta (Medico medico, LocalTime horaConsulta, LocalDate dataConsulta);
}
