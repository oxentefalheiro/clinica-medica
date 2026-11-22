package br.com.clinica.clinica_api.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.time.LocalTime;

public class MedicoRequestDTO {

    @NotBlank(message = "O nome não pode estar em branco.")
    private String nome;

    @NotBlank(message = "O CRM é obrigatório.")
    @Pattern(regexp = "\\d{4,7}-?[A-Za-z]{2}", message = "O CRM deve seguir o formato número + UF (ex.: 123456-BA).")
    private String crm;

    @NotNull(message = "O ID da especialidade é obrigatório.")
    private Long especialidadeId;

    @NotNull(message = "O dia do atendimento é obrigatório.")
    private LocalDate diaAtendimento;

    @NotNull(message = "O horário do início do atendimento é obrigatório.")
    private LocalTime horarioInicio;

    @NotNull(message = "O horário de fim do atendimento é obrigatório.")
    private LocalTime horarioFim;

    public MedicoRequestDTO() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public Long getEspecialidadeId() {
        return especialidadeId;
    }

    public void setEspecialidadeId(Long especialidadeId) {
        this.especialidadeId = especialidadeId;
    }

    public LocalDate getDiaAtendimento() {
        return diaAtendimento;
    }

    public void setDiaAtendimento(LocalDate diaAtendimento) {
        this.diaAtendimento = diaAtendimento;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(LocalTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public LocalTime getHorarioFim() {
        return horarioFim;
    }

    public void setHorarioFim(LocalTime horarioFim) {
        this.horarioFim = horarioFim;
    }
}
