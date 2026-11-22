package br.com.clinica.clinica_api.dto;

import br.com.clinica.clinica_api.enums.StatusAgendamento;
import br.com.clinica.clinica_api.model.Agendamento;

import java.time.LocalDate;
import java.time.LocalTime;

public class AgendamentoResponseDTO {

    private Long id;
    private Long pacienteId;
    private String paciente;
    private Long medicoId;
    private String medico;
    private String especialidade;
    private LocalDate dataConsulta;
    private LocalTime horaConsulta;
    private StatusAgendamento status;

    public AgendamentoResponseDTO(Agendamento agendamento) {
        this.id = agendamento.getId();
        this.pacienteId = agendamento.getPaciente().getId();
        this.paciente = agendamento.getPaciente().getNome();
        this.medicoId = agendamento.getMedico().getId();
        this.medico = agendamento.getMedico().getNome();
        this.especialidade = agendamento.getMedico().getEspecialidade().getNome();
        this.dataConsulta = agendamento.getDataConsulta();
        this.horaConsulta = agendamento.getHoraConsulta();
        this.status = agendamento.getStatusAgendamento();
    }

    public Long getId() { return id; }
    public Long getPacienteId() { return pacienteId; }
    public String getPaciente() { return paciente; }
    public Long getMedicoId() { return medicoId; }
    public String getMedico() { return medico; }
    public String getEspecialidade() { return especialidade; }
    public LocalDate getDataConsulta() { return dataConsulta; }
    public LocalTime getHoraConsulta() { return horaConsulta; }
    public StatusAgendamento getStatus() { return status; }
}
