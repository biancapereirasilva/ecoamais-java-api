package br.com.fiap.handler;

import br.com.fiap.model.dto.ErroResposta;
import br.com.fiap.exception.ErroDeAcessoADadosException;
import br.com.fiap.exception.RecursoNaoEncontradoException;
import br.com.fiap.exception.RegraDeNegocioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String ERRO_GENERICO =
            "Não foi possível concluir agora. Tente novamente em alguns minutos.";

    // 404
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResposta> tratarNaoEncontrado(RecursoNaoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErroResposta(e.getMessage()));
    }

    // 409: regra de negócio violada ou remoção de registro com filhos
    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErroResposta> tratarRegraDeNegocio(RegraDeNegocioException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErroResposta(e.getMessage()));
    }

    // 400: validação dos DTOs (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResposta> tratarValidacao(MethodArgumentNotValidException e) {
        String mensagem = "";
        for (FieldError erro : e.getBindingResult().getFieldErrors()) {
            mensagem = mensagem + erro.getDefaultMessage() + " ";
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErroResposta(mensagem.trim()));
    }

    // 400: JSON quebrado, campo com tipo errado ou data em formato inválido
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResposta> tratarCorpoInvalido(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErroResposta(
                "Não foi possível ler os dados enviados. Confira os campos e o formato da data (aaaa-mm-ddThh:mm:ss)."));
    }

    // 400: código da rota que não é número (ex.: /servicos/abc)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResposta> tratarTipoInvalido(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErroResposta(
                "O valor informado na rota é inválido."));
    }

    // 405
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroResposta> tratarMetodoNaoSuportado(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ErroResposta(
                "Esse método não é permitido nesta rota."));
    }

    // 415
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErroResposta> tratarTipoDeConteudo(HttpMediaTypeNotSupportedException e) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(new ErroResposta(
                "Envie os dados no formato JSON."));
    }

    // 404: rota que não existe
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErroResposta> tratarRotaInexistente(NoResourceFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErroResposta("Rota não encontrada."));
    }

    // 500: erro no banco. O detalhe (SQL) vai só para o log do servidor, nunca para a resposta
    @ExceptionHandler(ErroDeAcessoADadosException.class)
    public ResponseEntity<ErroResposta> tratarErroDeBanco(ErroDeAcessoADadosException e) {
        log.error("Erro de acesso a dados: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErroResposta(ERRO_GENERICO));
    }

    // 500: qualquer erro inesperado, com mensagem genérica
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResposta> tratarErroInesperado(Exception e) {
        log.error("Erro inesperado", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErroResposta(ERRO_GENERICO));
    }
}
