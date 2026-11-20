package br.com.clinica.clinica_api.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class MedicoUpdateDTO {

    @NotNull(message = "O ID do médico é obrigatório.")
    private Long id;

    //Não colocamos NotNull porque o usuário pode desejar modificar apenas uma das variáveis
    private String nome;

    private LocalDate diaAtendimento;

    private LocalTime horaInicio;

    private LocalTime horaFim;
    public MedicoUpdateDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDiaAtendimento() {
        return diaAtendimento;
    }

    public void setDiaAtendimento(LocalDate diaAtendimento) {
        this.diaAtendimento = diaAtendimento;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }
}
