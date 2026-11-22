package br.com.clinica.clinica_api.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "medico")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String crm;

    // N:1 -> vários médicos pertencem a uma especialidade (FK especialidade_id)
    @ManyToOne
    @JoinColumn(name = "especialidade_id", nullable = false)
    private Especialidade especialidade;

    @Column(nullable = false)
    private LocalDate diaAtendimento;

    @Column(nullable = false)
    private LocalTime horarioInicio;

    private LocalTime horarioFim;

    // Inativação lógica: o médico não é apagado (preserva o histórico de consultas)
    @Column(nullable = false)
    private boolean ativo = true;

    public Medico() {
    }

    public Medico(Long id, String nome, String crm, Especialidade especialidade, LocalDate diaAtendimento, LocalTime horarioInicio, LocalTime horarioFim) {
        this.id = id;
        this.nome = nome;
        this.crm = crm;
        this.especialidade = especialidade;
        this.diaAtendimento = diaAtendimento;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
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

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
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

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
