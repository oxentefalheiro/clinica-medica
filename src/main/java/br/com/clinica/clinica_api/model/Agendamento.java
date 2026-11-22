package br.com.clinica.clinica_api.model;

import br.com.clinica.clinica_api.enums.StatusAgendamento;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "agendamento")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // N:1 -> um paciente pode ter várias consultas (FK paciente_id)
    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    // N:1 -> um médico pode ter várias consultas (FK medico_id)
    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @Column(nullable = false)
    private LocalDate dataConsulta;

    @Column(nullable = false)
    private LocalTime horaConsulta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAgendamento statusAgendamento;

    public Agendamento() {
    }
    public Agendamento(Long id, Paciente paciente, Medico medico, LocalDate dataConsulta, LocalTime horaConsulta, StatusAgendamento statusAgendamento) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.dataConsulta = dataConsulta;
        this.horaConsulta = horaConsulta;
        this.statusAgendamento = statusAgendamento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
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

    public StatusAgendamento getStatusAgendamento() {
        return statusAgendamento;
    }

    public void setStatusAgendamento(StatusAgendamento statusAgendamento) {
        this.statusAgendamento = statusAgendamento;
    }
}
