package model;

import java.time.LocalTime;

public class Medico {
    private Integer id;
    private String nomeCompleto;
    private String crm;
    private String especialidade;
    private String diasAtendimento;
    private LocalTime horarioInicio;
    private LocalTime horarioFim;

    public Medico() {}

    public Medico(Integer id, String nomeCompleto, String crm, String especialidade, 
                  String diasAtendimento, LocalTime horarioInicio, LocalTime horarioFim) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.crm = crm;
        this.especialidade = especialidade;
        this.diasAtendimento = diasAtendimento;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
    }

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }

    public String getCrm() { return crm; }
    public void setCrm(String crm) { this.crm = crm; }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }

    public String getDiasAtendimento() { return diasAtendimento; }
    public void setDiasAtendimento(String diasAtendimento) { this.diasAtendimento = diasAtendimento; }

    public LocalTime getHorarioInicio() { return horarioInicio; }
    public void setHorarioInicio(LocalTime horarioInicio) { this.horarioInicio = horarioInicio; }

    public LocalTime getHorarioFim() { return horarioFim; }
    public void setHorarioFim(LocalTime horarioFim) { this.horarioFim = horarioFim; }
}