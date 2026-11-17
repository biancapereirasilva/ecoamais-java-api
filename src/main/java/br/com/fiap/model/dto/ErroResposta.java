package br.com.fiap.model.dto;

public class ErroResposta {
    private String mensagem;

    public ErroResposta() {
    }

    public ErroResposta(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
