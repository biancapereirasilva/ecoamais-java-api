package br.com.fiap.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;

public class ServicoRequest {
    @NotBlank(message = "Informe o nome do serviço.")
    @Size(max = 150, message = "O nome pode ter no máximo 150 caracteres.")
    private String nome;

    @Size(max = 1000, message = "A descrição pode ter no máximo 1000 caracteres.")
    private String descricao;

    @NotBlank(message = "Informe a categoria.")
    @Pattern(regexp = "SAUDE|DOCUMENTOS|BENEFICIOS", message = "Categoria inválida. Use SAUDE, DOCUMENTOS ou BENEFICIOS.")
    private String categoria;

    @NotBlank(message = "Informe o pictograma.")
    @Pattern(regexp = "saude|vacina|documento|beneficio", message = "Pictograma inválido. Use saude, vacina, documento ou beneficio.")
    private String pictograma;

    @NotNull(message = "Informe a duração em minutos.")
    @Positive(message = "A duração deve ser maior que zero.")
    private Integer duracaoMin;

    @Size(max = 10, message = "Informe no máximo 10 documentos.")
    private ArrayList<@NotBlank(message = "Os documentos não podem ficar em branco.") @Size(max = 90, message = "Cada documento pode ter no máximo 90 caracteres.") String> documentos;

    public ServicoRequest() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPictograma() {
        return pictograma;
    }

    public void setPictograma(String pictograma) {
        this.pictograma = pictograma;
    }

    public Integer getDuracaoMin() {
        return duracaoMin;
    }

    public void setDuracaoMin(Integer duracaoMin) {
        this.duracaoMin = duracaoMin;
    }

    public ArrayList<String> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(ArrayList<String> documentos) {
        this.documentos = documentos;
    }
}
