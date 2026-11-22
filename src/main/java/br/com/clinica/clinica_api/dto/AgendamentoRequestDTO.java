package br.com.clinica.clinica_api.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class AgendamentoRequestDTO {

    @NotNull(message = "O paciente é obrigatório.")
    private Long pacienteId;

    @NotNull(message = "O médico é obrigatório.")
    private Long medicoId;

    @NotNull(message = "O dia da consulta é obrigatório.")
    @FutureOrPresent(message = "Não é possível agendar consulta em data passada.")
    private LocalDate dataConsulta;

    @NotNull(message = "A hora da consulta é obrigatória.")
    private LocalTime horaConsulta;

    // O status não vem do cliente: todo agendamento novo nasce como AGENDADO (definido no Service)
    public AgendamentoRequestDTO() {
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public Long getMedicoId() {
        return medicoId;
    }

    public void setMedicoId(Long medicoId) {
        this.medicoId = medicoId;
    }

    public LocalDate getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(LocalDate dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public LocalTime getHoraConsulta() {
        return horaConsulta;
    }

    public void setHoraConsulta(LocalTime horaConsulta) {
        this.horaConsulta = horaConsulta;
    }
}
