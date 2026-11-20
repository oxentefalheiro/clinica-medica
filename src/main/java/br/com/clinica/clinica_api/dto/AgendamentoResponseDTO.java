package br.com.clinica.clinica_api.dto;

import br.com.clinica.clinica_api.enums.StatusAgendamento;
import br.com.clinica.clinica_api.model.Agendamento;
import br.com.clinica.clinica_api.model.Medico;
import br.com.clinica.clinica_api.model.Paciente;

import java.time.LocalDate;
import java.time.LocalTime;

public class AgendamentoResponseDTO {

    private Long id;
    private String paciente;
    private String medico;
    private LocalDate dataConsulta;
    private LocalTime horaConsulta;
    private StatusAgendamento status;

    public AgendamentoResponseDTO(Agendamento agendamento) {
        this.id = agendamento.getId();
        this.paciente = agendamento.getPaciente().getNome();
        this.medico = agendamento.getMedico().getNome();
        this.dataConsulta = agendamento.getDataConsulta();
        this.horaConsulta = agendamento.getHoraConsulta();
        this.status = agendamento.getStatusAgendamento();
    }

    public Long getId() {
        return id;
    }

    public String getPaciente() {
        return paciente;
    }

    public String getMedico() {
        return medico;
    }

    public LocalDate getDataConsulta() {
        return dataConsulta;
    }

    public LocalTime getHoraConsulta() {
        return horaConsulta;
    }

    public StatusAgendamento getStatus() {
        return status;
    }
}
