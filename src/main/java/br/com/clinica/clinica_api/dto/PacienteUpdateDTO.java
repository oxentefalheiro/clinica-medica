package br.com.clinica.clinica_api.dto;

import jakarta.validation.constraints.Pattern;

// O ID agora vem pela URL (PUT /pacientes/{id}).
// Os campos não são obrigatórios porque o usuário pode querer alterar apenas um deles.
public class PacienteUpdateDTO {

    private String nome;

    @Pattern(regexp = "\\d{10,11}", message = "O telefone deve conter DDD + número, apenas dígitos (10 ou 11).")
    private String telefone;

    public PacienteUpdateDTO() {
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
}
