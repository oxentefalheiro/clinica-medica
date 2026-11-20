package br.com.clinica.clinica_api.dto;

import br.com.clinica.clinica_api.model.Paciente;

public class PacienteResponseDTO {

    private Long id;
    private String nome;
    private String telefone;
    private String cpf;

    public PacienteResponseDTO(Paciente paciente) {
        this.id = paciente.getId();
        this.nome = paciente.getNome();
        this.telefone = paciente.getTelefone();
        this.cpf = paciente.getCpf();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getCpf() {
        return cpf;
    }

}
