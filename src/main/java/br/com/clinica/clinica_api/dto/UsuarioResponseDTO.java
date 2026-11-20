package br.com.clinica.clinica_api.dto;

import br.com.clinica.clinica_api.enums.Perfil;
import br.com.clinica.clinica_api.model.Usuario;

public class UsuarioResponseDTO {

    private Long id;
    private String login;
    private Perfil perfil;
    private Boolean ativo;

    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.login = usuario.getLogin();
        this.perfil = usuario.getPerfil();
        this.ativo = usuario.isAtivo();
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public Boolean getAtivo() {
        return ativo;
    }

}
