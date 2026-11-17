package br.com.fiap.model;

import java.util.ArrayList;

public class Servico {

    private Long id;
    private String nome;
    private String descricao;
    private String categoria;
    private String pictograma;
    private Integer duracaoMin;
    private ArrayList<String> documentos = new ArrayList<>();

    public Servico() {
    }

    public Servico(Long id, String nome, String descricao, String categoria, String pictograma, Integer duracaoMin, ArrayList<String> documentos) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.pictograma = pictograma;
        this.duracaoMin = duracaoMin;
        this.documentos = documentos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
