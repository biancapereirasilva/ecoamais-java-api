package br.com.fiap.model;

import java.time.LocalDateTime;

public class Atendimento {

    private Long id;
    private Long idCidadao;
    private Long idServico;
    private Long idUnidade;
    private LocalDateTime dataHora;
    private String status;

    public Atendimento() {
    }

    public Atendimento(Long id, Long idCidadao, Long idServico, Long idUnidade, LocalDateTime dataHora, String status) {
        this.id = id;
        this.idCidadao = idCidadao;
        this.idServico = idServico;
        this.idUnidade = idUnidade;
        this.dataHora = dataHora;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCidadao() {
        return idCidadao;
    }

    public void setIdCidadao(Long idCidadao) {
        this.idCidadao = idCidadao;
    }

    public Long getIdServico() {
        return idServico;
    }

    public void setIdServico(Long idServico) {
        this.idServico = idServico;
    }

    public Long getIdUnidade() {
        return idUnidade;
    }

    public void setIdUnidade(Long idUnidade) {
        this.idUnidade = idUnidade;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
