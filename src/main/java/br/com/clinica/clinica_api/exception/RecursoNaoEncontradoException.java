package br.com.clinica.clinica_api.exception;

// Lançada quando um registro buscado pelo ID não existe no banco -> HTTP 404
public class RecursoNaoEncontradoException extends RuntimeException {
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
