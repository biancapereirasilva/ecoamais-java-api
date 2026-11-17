package br.com.fiap.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

public class AtendimentoAtualizacaoRequest {
    private Long id;

    @NotNull(message = "Informe o cidadão do atendimento.")
    private Long idCidadao;

    @NotNull(message = "Informe o serviço do atendimento.")
    private Long idServico;

    @NotNull(message = "Informe a unidade do atendimento.")
    private Long idUnidade;

    @NotNull(message = "Informe a data e a hora do atendimento.")
    private LocalDateTime dataHora;

    @NotBlank(message = "Informe o status do atendimento.")
    @Pattern(regexp = "AGENDADO|CHAMADO|CONCLUIDO|CANCELADO", message = "Status inválido. Use AGENDADO, CHAMADO, CONCLUIDO ou CANCELADO.")
    private String status;

    public AtendimentoAtualizacaoRequest() {
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