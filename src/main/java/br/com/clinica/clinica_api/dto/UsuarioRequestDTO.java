package br.com.clinica.clinica_api.dto;

import br.com.clinica.clinica_api.enums.Perfil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UsuarioRequestDTO {

    @NotBlank(message = "O login do usuário é obrigatório. ")
    private String login;

    @NotBlank(message = "A senha do usuário é obrigatória.")
    private String senha;

    @NotNull(message = "O perfil do usuário é obrigatório.")
    private Perfil perfil;
    public UsuarioRequestDTO() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}
