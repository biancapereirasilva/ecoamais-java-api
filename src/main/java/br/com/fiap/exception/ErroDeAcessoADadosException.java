package br.com.fiap.exception;

public class ErroDeAcessoADadosException extends RuntimeException {

    public ErroDeAcessoADadosException(String mensagem, Exception causa) {
        super(mensagem, causa);
    }
}
