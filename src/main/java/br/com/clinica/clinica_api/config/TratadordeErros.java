package br.com.clinica.clinica_api.config;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class TratadordeErros {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> tratarRegraDeNegocio(RuntimeException ex) {
        //Faz com que mostre somente a mensagem configurada no Service
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    //Validação de erros em casos que não sao RunTimeExceptional
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> tratarErroValidacao  (MethodArgumentNotValidException exception) {
        //Pega todos os erros de validação que o @Valid do DTO encontrou
        List<FieldError> erros = exception.getFieldErrors();

        //Mostra a mensagem bonitinha que foi programada no Service
        String textoLimpo = erros.stream().map(erro -> erro.getDefaultMessage()).collect(Collectors.joining(", "));

        return ResponseEntity.badRequest().body(textoLimpo);
    }

}
