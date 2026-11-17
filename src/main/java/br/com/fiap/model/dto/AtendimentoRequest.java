package br.com.fiap.model.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public class AtendimentoRequest {
    @NotNull(message = "Escolha um serviço.")
    @Positive(message = "Escolha um serviço válido.")
    private Long idServico;

    @NotNull(message = "Escolha uma unidade.")
    @Positive(message = "Escolha uma unidade válida.")
    private Long idUnidade;

    @NotNull(message = "Escolha a data e a hora do atendimento.")
    @Future(message = "Escolha uma data e uma hora que ainda não passaram.")
    private LocalDateTime dataHora;

    public AtendimentoRequest() {
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
}
