package br.com.clinica.clinica_api.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    private final SecurityFilter securityFilter;

    public SecurityConfigurations(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                // Permite que o console do H2 abra dentro de frames
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    // IMPORTANTE: as regras são lidas de cima para baixo e o anyRequest() TEM que ser o último.

                    // Rotas públicas
                    req.requestMatchers(HttpMethod.POST, "/login").permitAll();
                    req.requestMatchers("/h2-console/**", "/error").permitAll();

                    // Usuários: somente ADMIN gerencia
                    req.requestMatchers("/usuario/**").hasRole("ADMIN");

                    // Especialidades e médicos: todos os perfis consultam, somente ADMIN altera
                    req.requestMatchers(HttpMethod.GET, "/especialidade/**", "/medico/**").authenticated();
                    req.requestMatchers("/especialidade/**", "/medico/**").hasRole("ADMIN");

                    // Pacientes: ADMIN e RECEPCIONISTA
                    req.requestMatchers("/pacientes/**").hasAnyRole("ADMIN", "RECEPCIONISTA");

                    // Agendamentos: todos consultam; MÉDICO marca como realizado; RECEPCIONISTA agenda/cancela
                    req.requestMatchers(HttpMethod.GET, "/agendamento/**").authenticated();
                    req.requestMatchers(HttpMethod.PATCH, "/agendamento/*/realizar").hasAnyRole("ADMIN", "MEDICO");
                    req.requestMatchers("/agendamento/**").hasAnyRole("ADMIN", "RECEPCIONISTA");

                    // Qualquer outra rota exige estar logado
                    req.anyRequest().authenticated();
                })
                .exceptionHandling(ex -> ex
                        // Sem token ou token inválido -> 401
                        .authenticationEntryPoint((request, response, e) ->
                                escreverErro(response, 401, "Unauthorized",
                                        "Autenticação necessária. Envie o token no cabeçalho Authorization: Bearer <token>.",
                                        request.getRequestURI()))
                        // Logado, mas o perfil não tem permissão -> 403
                        .accessDeniedHandler((request, response, e) ->
                                escreverErro(response, 403, "Forbidden",
                                        "Seu perfil não tem permissão para esta operação.",
                                        request.getRequestURI())))
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // Os erros de segurança acontecem antes de chegar no Controller, por isso o JSON é escrito aqui
    private void escreverErro(HttpServletResponse response, int status, String erro, String mensagem, String caminho) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                "{\"status\":" + status +
                ",\"erro\":\"" + erro + "\"" +
                ",\"mensagem\":\"" + mensagem + "\"" +
                ",\"dataHora\":\"" + LocalDateTime.now() + "\"" +
                ",\"caminho\":\"" + caminho + "\"}");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
