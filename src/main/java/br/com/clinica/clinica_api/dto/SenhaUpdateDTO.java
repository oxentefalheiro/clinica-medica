package br.com.clinica.clinica_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SenhaUpdateDTO {

    @NotBlank(message = "A nova senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres.")
    private String novaSenha;

    public SenhaUpdateDTO() {
    }

    public String getNovaSenha() { return novaSenha; }
    public void setNovaSenha(String novaSenha) { this.novaSenha = novaSenha; }
}
