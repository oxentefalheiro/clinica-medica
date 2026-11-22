package br.com.clinica.clinica_api.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReagendamentoRequestDTO {

    @NotNull(message = "A nova data da consulta é obrigatória.")
    @FutureOrPresent(message = "A nova data não pode estar no passado.")
    private LocalDate novaData;

    @NotNull(message = "O novo horário da consulta é obrigatório.")
    private LocalTime novaHora;

    public ReagendamentoRequestDTO() {
    }

    public LocalDate getNovaData() { return novaData; }
    public void setNovaData(LocalDate novaData) { this.novaData = novaData; }

    public LocalTime getNovaHora() { return novaHora; }
    public void setNovaHora(LocalTime novaHora) { this.novaHora = novaHora; }
}
