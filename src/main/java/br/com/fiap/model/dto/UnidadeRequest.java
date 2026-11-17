package br.com.fiap.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UnidadeRequest {
    @NotBlank(message = "Informe o nome da unidade.")
    @Size(max = 150, message = "O nome pode ter no máximo 150 caracteres.")
    private String nome;

    @NotBlank(message = "Informe o tipo da unidade.")
    @Pattern(regexp = "UBS|CRAS|POUPATEMPO", message = "Tipo inválido. Use UBS, CRAS ou POUPATEMPO.")
    private String tipo;

    @NotBlank(message = "Informe o endereço.")
    @Size(max = 200, message = "O endereço pode ter no máximo 200 caracteres.")
    private String endereco;

    @NotBlank(message = "Informe o bairro.")
    @Size(max = 100, message = "O bairro pode ter no máximo 100 caracteres.")
    private String bairro;

    public UnidadeRequest() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }
}
