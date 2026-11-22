package br.com.clinica.clinica_api.repository;

import br.com.clinica.clinica_api.enums.StatusAgendamento;
import br.com.clinica.clinica_api.model.Agendamento;
import br.com.clinica.clinica_api.model.Medico;
import br.com.clinica.clinica_api.model.Paciente;
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

    // Conflito: o médico já tem consulta (não cancelada) nesse dia e horário?
    boolean existsByMedicoAndDataConsultaAndHoraConsultaAndStatusAgendamentoNot(
            Medico medico, LocalDate dataConsulta, LocalTime horaConsulta, StatusAgendamento status);

    // Conflito: o paciente já tem consulta (não cancelada) nesse dia e horário?
    boolean existsByPacienteAndDataConsultaAndHoraConsultaAndStatusAgendamentoNot(
            Paciente paciente, LocalDate dataConsulta, LocalTime horaConsulta, StatusAgendamento status);

    // Agenda do médico em um dia (usada para a agenda e para calcular os horários livres)
    List<Agendamento> findByMedicoAndDataConsultaAndStatusAgendamentoNotOrderByHoraConsulta(
            Medico medico, LocalDate dataConsulta, StatusAgendamento status);

    boolean existsByPacienteAndStatusAgendamentoIn(Paciente paciente, List<StatusAgendamento> status);
    boolean existsByMedicoAndStatusAgendamentoIn(Medico medico, List<StatusAgendamento> status);
}
