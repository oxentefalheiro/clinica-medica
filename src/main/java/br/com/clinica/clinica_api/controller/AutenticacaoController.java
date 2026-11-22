package br.com.clinica.clinica_api.controller;

import br.com.clinica.clinica_api.dto.LoginRequestDTO;
import br.com.clinica.clinica_api.dto.TokenResponseDTO;
import br.com.clinica.clinica_api.model.Usuario;
import br.com.clinica.clinica_api.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    private final AuthenticationManager manager;
    private final TokenService tokenService;

    public AutenticacaoController(AuthenticationManager manager, TokenService tokenService) {
        this.manager = manager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<TokenResponseDTO> efetuarLogin(@RequestBody @Valid LoginRequestDTO dto) {
        // 1. Empacota o login e a senha num objeto do Spring Security
        var authenticationToken = new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getSenha());

        // 2. O Spring chama o AutenticacaoService e valida a senha criptografada automaticamente!
        Authentication authentication = manager.authenticate(authenticationToken);

        // 3. Se passou, gera o crachá (Token JWT) para o usuário autenticado
        String tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new TokenResponseDTO(tokenJWT));
    }
}