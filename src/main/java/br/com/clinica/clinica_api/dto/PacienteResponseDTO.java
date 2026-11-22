package br.com.clinica.clinica_api.dto;

import br.com.clinica.clinica_api.model.Paciente;

import java.time.LocalDate;

public class PacienteResponseDTO {

    private Long id;
    private String nome;
    private LocalDate dataNascimento;
    private String telefone;
    private String cpf;
    private boolean ativo;

    public PacienteResponseDTO(Paciente paciente) {
        this.id = paciente.getId();
        this.nome = paciente.getNome();
        this.dataNascimento = paciente.getDataNascimento();
        this.telefone = paciente.getTelefone();
        this.cpf = paciente.getCpf();
        this.ativo = paciente.isAtivo();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public String getTelefone() { return telefone; }
    public String getCpf() { return cpf; }
    public boolean isAtivo() { return ativo; }
}
