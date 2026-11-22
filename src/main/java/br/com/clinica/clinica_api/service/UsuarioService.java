package br.com.clinica.clinica_api.service;

import br.com.clinica.clinica_api.exception.RegraNegocioException;

import br.com.clinica.clinica_api.exception.ConflitoException;

import br.com.clinica.clinica_api.exception.RecursoNaoEncontradoException;

import br.com.clinica.clinica_api.dto.SenhaUpdateDTO;
import br.com.clinica.clinica_api.dto.UsuarioRequestDTO;
import br.com.clinica.clinica_api.enums.Perfil;
import br.com.clinica.clinica_api.model.Usuario;
import br.com.clinica.clinica_api.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<Usuario> listarUsuarios (Pageable paginacao){
        return usuarioRepository.findAll(paginacao);
    }

    public Page<Usuario> listarPerfil(Perfil perfil, Pageable paginacao) {
        return usuarioRepository.findByPerfil(perfil, paginacao);
    }

    public Page<Usuario> listarAtivos(Pageable paginacao) {
        return usuarioRepository.findByAtivoTrue(paginacao);
    }

    public Usuario buscarPorId (Long id){
        return usuarioRepository.findById(id).orElseThrow(()-> new RecursoNaoEncontradoException("Usuário não encontrado com o ID: " + id));
    }

    public Usuario cadastrar(UsuarioRequestDTO usuarioDto) {
        if (usuarioRepository.existsByLogin(usuarioDto.getLogin())) {
            throw new ConflitoException("Login já cadastrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setLogin(usuarioDto.getLogin());

        // CONFIRA ESTA LINHA: ela precisa jogar a senha criptografada dentro de 'usuario'!
        String senhaCriptografada = passwordEncoder.encode(usuarioDto.getSenha());
        usuario.setSenha(senhaCriptografada);

        usuario.setPerfil(usuarioDto.getPerfil());
        usuario.setAtivo(true);

        return usuarioRepository.save(usuario);
    }

    public Usuario atualizarSenha (Long id, SenhaUpdateDTO dto){
        Usuario usuarioExistente = this.buscarPorId(id);

        //Criptografando a nova senha antes de salvar!
        usuarioExistente.setSenha(passwordEncoder.encode(dto.getNovaSenha()));

        return usuarioRepository.save(usuarioExistente);
    }

    public Usuario inativar (Long id){
        Usuario usuario = this.buscarPorId(id);

        if(!usuario.isAtivo()){
            throw new ConflitoException("O usuário já está inativo.");
        }

        if(usuario.getPerfil() != Perfil.RECEPCIONISTA){
            throw new RegraNegocioException("Somente usuários com perfil RECEPCIONISTA podem ser inativados.");
        }

        usuario.setAtivo(false);

        return usuarioRepository.save(usuario);
    }
}
