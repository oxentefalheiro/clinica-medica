package br.com.clinica.clinica_api.exception;

// Lançada quando os dados violam uma regra da clínica (ex.: consulta no domingo) -> HTTP 400
public class RegraNegocioException extends RuntimeException {
    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
