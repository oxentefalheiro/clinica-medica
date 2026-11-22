package br.com.clinica.clinica_api.dto;

import java.time.LocalDateTime;
import java.util.List;

// Formato padrão de erro devolvido pela API (exigência do item 3.11 do enunciado)
public class ErroResponseDTO {

    private int status;
    private String erro;
    private String mensagem;
    private LocalDateTime dataHora;
    private String caminho;
    private List<String> campos;

    public ErroResponseDTO(int status, String erro, String mensagem, String caminho, List<String> campos) {
        this.status = status;
        this.erro = erro;
        this.mensagem = mensagem;
        this.dataHora = LocalDateTime.now();
        this.caminho = caminho;
        this.campos = campos;
    }

    public int getStatus() { return status; }
    public String getErro() { return erro; }
    public String getMensagem() { return mensagem; }
    public LocalDateTime getDataHora() { return dataHora; }
    public String getCaminho() { return caminho; }
    public List<String> getCampos() { return campos; }
}
