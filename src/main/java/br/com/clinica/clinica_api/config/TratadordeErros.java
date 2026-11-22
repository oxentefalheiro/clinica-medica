package br.com.clinica.clinica_api.config;

import br.com.clinica.clinica_api.dto.ErroResponseDTO;
import br.com.clinica.clinica_api.exception.ConflitoException;
import br.com.clinica.clinica_api.exception.RecursoNaoEncontradoException;
import br.com.clinica.clinica_api.exception.RegraNegocioException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

// Centraliza o tratamento de erros: cada tipo de exceção vira um código HTTP + JSON padronizado
@RestControllerAdvice
public class TratadordeErros {

    // 404 - registro não encontrado
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponseDTO> tratarNaoEncontrado(RecursoNaoEncontradoException ex, HttpServletRequest request) {
        return montar(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    // 409 - conflito de negócio (horário ocupado, CPF/CRM/login já cadastrado...)
    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<ErroResponseDTO> tratarConflito(ConflitoException ex, HttpServletRequest request) {
        return montar(HttpStatus.CONFLICT, ex.getMessage(), request, null);
    }

    // 400 - regra de negócio violada (ex.: consulta no domingo)
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResponseDTO> tratarRegraNegocio(RegraNegocioException ex, HttpServletRequest request) {
        return montar(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null);
    }

    // 400 - campos inválidos no DTO (@NotBlank, @NotNull... ativados pelo @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDTO> tratarErroValidacao(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> campos = ex.getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .toList();
        return montar(HttpStatus.BAD_REQUEST, "Existem campos inválidos na requisição.", request, campos);
    }

    // 400 - JSON mal formatado, data/hora em formato errado ou valor de enum inexistente
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponseDTO> tratarJsonInvalido(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return montar(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido. Verifique o JSON, as datas (AAAA-MM-DD), as horas (HH:MM) e os valores permitidos.", request, null);
    }

    // 400 - parâmetro da URL com tipo errado (ex.: /pacientes/abc)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponseDTO> tratarParametroInvalido(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        return montar(HttpStatus.BAD_REQUEST, "Valor inválido para o parâmetro '" + ex.getName() + "'.", request, null);
    }

    // 409 - restrição do banco violada (unique, not null, chave estrangeira)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponseDTO> tratarIntegridade(DataIntegrityViolationException ex, HttpServletRequest request) {
        return montar(HttpStatus.CONFLICT, "Operação viola uma restrição do banco de dados (registro duplicado ou vinculado a outro).", request, null);
    }

    // 401 - login ou senha incorretos / usuário inativo
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErroResponseDTO> tratarAutenticacao(AuthenticationException ex, HttpServletRequest request) {
        return montar(HttpStatus.UNAUTHORIZED, "Login ou senha inválidos, ou usuário inativo.", request, null);
    }

    // 403 - usuário autenticado sem permissão
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErroResponseDTO> tratarAcessoNegado(AccessDeniedException ex, HttpServletRequest request) {
        return montar(HttpStatus.FORBIDDEN, "Seu perfil não tem permissão para esta operação.", request, null);
    }

    // 500 - qualquer erro inesperado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseDTO> tratarErroGenerico(Exception ex, HttpServletRequest request) {
        return montar(HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado no servidor.", request, null);
    }

    private ResponseEntity<ErroResponseDTO> montar(HttpStatus status, String mensagem, HttpServletRequest request, List<String> campos) {
        ErroResponseDTO corpo = new ErroResponseDTO(status.value(), status.getReasonPhrase(), mensagem, request.getRequestURI(), campos);
        return ResponseEntity.status(status).body(corpo);
    }
}
