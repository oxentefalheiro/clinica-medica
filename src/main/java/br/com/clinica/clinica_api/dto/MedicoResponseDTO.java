package br.com.clinica.clinica_api.dto;

import br.com.clinica.clinica_api.model.Medico;

import java.time.LocalDate;
import java.time.LocalTime;

public class MedicoResponseDTO {

    private Long id;
    private String nome;
    private String crm;
    private Long especialidadeId;
    private String especialidade;
    private LocalDate diaAtendimento;
    private LocalTime horarioInicio;
    private LocalTime horarioFim;
    private boolean ativo;

    // Construtor que "traduz" a entidade para o DTO (evita expor a entidade e JSON recursivo)
    public MedicoResponseDTO(Medico medico) {
        this.id = medico.getId();
        this.nome = medico.getNome();
        this.crm = medico.getCrm();
        this.especialidadeId = medico.getEspecialidade().getId();
        this.especialidade = medico.getEspecialidade().getNome();
        this.diaAtendimento = medico.getDiaAtendimento();
        this.horarioInicio = medico.getHorarioInicio();
        this.horarioFim = medico.getHorarioFim();
        this.ativo = medico.isAtivo();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCrm() { return crm; }
    public Long getEspecialidadeId() { return especialidadeId; }
    public String getEspecialidade() { return especialidade; }
    public LocalDate getDiaAtendimento() { return diaAtendimento; }
    public LocalTime getHorarioInicio() { return horarioInicio; }
    public LocalTime getHorarioFim() { return horarioFim; }
    public boolean isAtivo() { return ativo; }
}
