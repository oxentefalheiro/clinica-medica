package br.com.clinica.clinica_api.dto;


import br.com.clinica.clinica_api.model.Medico;

public class MedicoResponseDTO {

    private Long id;
    private String nome;
    private String crm;
    private String especialidade;

    // Construtor inteligente que "traduz" a entidade para o DTO
    public MedicoResponseDTO(Medico medico) {
        this.id = medico.getId();
        this.nome = medico.getNome();
        this.crm = medico.getCrm();
        this.especialidade = medico.getEspecialidade().getNome();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCrm() {
        return crm;
    }

    public String getEspecialidade() {
        return especialidade;
    }
}
