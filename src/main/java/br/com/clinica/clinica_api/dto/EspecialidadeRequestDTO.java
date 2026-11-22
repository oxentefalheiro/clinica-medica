package br.com.clinica.clinica_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EspecialidadeRequestDTO {

    @NotBlank(message = "O nome da especialidade é obrigatório.")
    @Size(max = 80, message = "O nome da especialidade deve ter no máximo 80 caracteres.")
    private String nome;

    public EspecialidadeRequestDTO() {
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
