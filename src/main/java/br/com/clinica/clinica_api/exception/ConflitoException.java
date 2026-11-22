package br.com.clinica.clinica_api.exception;

// Lançada quando a operação conflita com dados já existentes
// (horário ocupado, CPF/CRM/login duplicado, status que não permite a ação) -> HTTP 409
public class ConflitoException extends RuntimeException {
    public ConflitoException(String mensagem) {
        super(mensagem);
    }
}
