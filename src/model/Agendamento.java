package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Agendamento {
    private Integer id;
    private Integer idPaciente;
    private Integer idMedico;
    private LocalDate dataConsulta;
    private LocalTime horarioInicio;
    private String status; // AGENDADO, CONFIRMADO, REALIZADO, CANCELADO

    public Agendamento() {}

    public Agendamento(Integer id, Integer idPaciente, Integer idMedico, 
                       LocalDate dataConsulta, LocalTime horarioInicio, String status) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
        this.dataConsulta = dataConsulta;
        this.horarioInicio = horarioInicio;
        this.status = status;
    }

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Integer idPaciente) { this.idPaciente = idPaciente; }

    public Integer getIdMedico() { return idMedico; }
    public void setIdMedico(Integer idMedico) { this.idMedico = idMedico; }

    public LocalDate getDataConsulta() { return dataConsulta; }
    public void setDataConsulta(LocalDate dataConsulta) { this.dataConsulta = dataConsulta; }

    public LocalTime getHorarioInicio() { return horarioInicio; }
    public void setHorarioInicio(LocalTime horarioInicio) { this.horarioInicio = horarioInicio; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}