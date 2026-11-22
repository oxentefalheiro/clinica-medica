package br.com.clinica.clinica_api.controller;

import br.com.clinica.clinica_api.dto.SenhaUpdateDTO;
import br.com.clinica.clinica_api.dto.UsuarioRequestDTO;
import br.com.clinica.clinica_api.dto.UsuarioResponseDTO;
import br.com.clinica.clinica_api.enums.Perfil;
import br.com.clinica.clinica_api.model.Usuario;
import org.springframework.data.domain.Pageable;
import br.com.clinica.clinica_api.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioResponseDTO>> listarUsuarios(@PageableDefault(size = 10, sort = {"login"}) Pageable paginacao){
        Page<Usuario> lista = usuarioService.listarUsuarios(paginacao);

        Page<UsuarioResponseDTO> usuarioPaginas = lista.map(UsuarioResponseDTO::new);
        return ResponseEntity.ok(usuarioPaginas);
    }

    @GetMapping("/ativos")
    public ResponseEntity<Page<UsuarioResponseDTO>> listarAtivos(@PageableDefault(size = 10, sort = {"login"}) Pageable paginacao){
        Page<Usuario> usuariosAtivos = usuarioService.listarAtivos(paginacao);

        Page<UsuarioResponseDTO> ativosPaginas = usuariosAtivos.map(UsuarioResponseDTO::new);
        return ResponseEntity.ok(ativosPaginas);
    }

    @GetMapping("/perfil/{perfil}")
    public ResponseEntity<Page<UsuarioResponseDTO>> listarPorPerfil(@PathVariable Perfil perfil, @PageableDefault(size = 10, sort = {"login"}) Pageable paginacao) {
        Page<Usuario> listarPorPerfil = usuarioService.listarPerfil(perfil, paginacao);

        Page<UsuarioResponseDTO> usuarioPaginas = listarPorPerfil.map(UsuarioResponseDTO::new);
        return ResponseEntity.ok(usuarioPaginas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new UsuarioResponseDTO(usuarioService.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@RequestBody @Valid UsuarioRequestDTO usuarioDto, UriComponentsBuilder uriBuilder) {
        Usuario cadastrarUsuario = usuarioService.cadastrar(usuarioDto);
        URI uri = uriBuilder.path("/usuario/{id}").buildAndExpand(cadastrarUsuario.getId()).toUri();

        // A resposta usa o DTO: a senha (mesmo criptografada) nunca é devolvida
        return ResponseEntity.created(uri).body(new UsuarioResponseDTO(cadastrarUsuario));
    }

    @PatchMapping("/{id}/senha")
    public ResponseEntity<Void> atualizarSenha(@PathVariable Long id, @RequestBody @Valid SenhaUpdateDTO dto) {
        usuarioService.atualizarSenha(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<UsuarioResponseDTO> inativar(@PathVariable Long id) {
        Usuario inativado = usuarioService.inativar(id);

        return ResponseEntity.ok(new UsuarioResponseDTO(inativado));
    }
}
