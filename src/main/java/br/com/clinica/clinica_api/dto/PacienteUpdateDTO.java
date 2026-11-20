package br.com.clinica.clinica_api.dto;

import jakarta.validation.constraints.NotNull;

public class PacienteUpdateDTO {

    @NotNull(message = "O ID do paciente é obrigatório.")
    private Long id;

    //Não colocamos NotNull porque o usuário pode desejar modificar apenas uma das variáveis
    private String nome;
    private String telefone;
    public PacienteUpdateDTO() {
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
