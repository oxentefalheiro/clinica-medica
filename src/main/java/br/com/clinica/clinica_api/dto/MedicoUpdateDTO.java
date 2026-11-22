package br.com.clinica.clinica_api.dto;

import java.time.LocalDate;
import java.time.LocalTime;

// O ID agora vem pela URL (PUT /medico/{id}).
// Os campos não são obrigatórios porque o usuário pode querer alterar apenas um deles.
public class MedicoUpdateDTO {

    private String nome;
    private Long especialidadeId;
    private LocalDate diaAtendimento;
    private LocalTime horarioInicio;
    private LocalTime horarioFim;

    public MedicoUpdateDTO() {
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Long getEspecialidadeId() { return especialidadeId; }
    public void setEspecialidadeId(Long especialidadeId) { this.especialidadeId = especialidadeId; }

    public LocalDate getDiaAtendimento() { return diaAtendimento; }
    public void setDiaAtendimento(LocalDate diaAtendimento) { this.diaAtendimento = diaAtendimento; }

    public LocalTime getHorarioInicio() { return horarioInicio; }
    public void setHorarioInicio(LocalTime horarioInicio) { this.horarioInicio = horarioInicio; }

    public LocalTime getHorarioFim() { return horarioFim; }
    public void setHorarioFim(LocalTime horarioFim) { this.horarioFim = horarioFim; }
}
