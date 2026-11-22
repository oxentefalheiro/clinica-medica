package br.com.clinica.clinica_api.config;

import br.com.clinica.clinica_api.enums.Perfil;
import br.com.clinica.clinica_api.model.Usuario;
import br.com.clinica.clinica_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// Como o cadastro de usuários agora é exclusivo do ADMIN, criamos um ADMIN inicial ao subir a aplicação.
// Login e senha vêm de variáveis de ambiente (ADMIN_LOGIN / ADMIN_SENHA); os valores após ":" são só para desenvolvimento.
@Component
public class DadosIniciais implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${api.admin.login:admin}")
    private String adminLogin;

    @Value("${api.admin.senha:admin123}")
    private String adminSenha;

    public DadosIniciais(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!usuarioRepository.existsByLogin(adminLogin)) {
            Usuario admin = new Usuario();
            admin.setLogin(adminLogin);
            admin.setSenha(passwordEncoder.encode(adminSenha));
            admin.setPerfil(Perfil.ADMIN);
            admin.setAtivo(true);
            usuarioRepository.save(admin);
        }
    }
}
