package br.com.clinica.clinica_api.dto;

import br.com.clinica.clinica_api.model.Especialidade;

public class EspecialidadeResponseDTO {

    private Long id;
    private String nome;

    public EspecialidadeResponseDTO(Especialidade especialidade) {
        this.id = especialidade.getId();
        this.nome = especialidade.getNome();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
}
