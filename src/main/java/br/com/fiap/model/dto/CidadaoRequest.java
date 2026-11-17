package br.com.fiap.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CidadaoRequest {
    @NotBlank(message = "Informe o seu nome.")
    @Size(max = 150, message = "O nome pode ter no máximo 150 caracteres.")
    private String nome;

    @Pattern(regexp = "^(\\d{10,11})?$", message = "Telefone inválido. Use só números, com DDD (10 ou 11 dígitos).")
    private String telefone;

    public CidadaoRequest() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
