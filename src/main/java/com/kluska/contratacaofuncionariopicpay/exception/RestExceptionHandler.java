package com.kluska.contratacaofuncionariopicpay.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(FuncionarioNaoEncontradoException.class)
    public ResponseEntity<ErroApiResponse> tratarFuncionarioNaoEncontrado(
            FuncionarioNaoEncontradoException exception,
            HttpServletRequest request
    ) {
        return criarResposta(HttpStatus.NOT_FOUND, exception.getMessage(), request, List.of());
    }

    @ExceptionHandler(FuncionarioIdDuplicadoException.class)
    public ResponseEntity<ErroApiResponse> tratarFuncionarioIdDuplicado(
            FuncionarioIdDuplicadoException exception,
            HttpServletRequest request
    ) {
        return criarResposta(HttpStatus.CONFLICT, exception.getMessage(), request, List.of());
    }

    @ExceptionHandler(RequisicaoInvalidaException.class)
    public ResponseEntity<ErroApiResponse> tratarRequisicaoInvalida(
            RequisicaoInvalidaException exception,
            HttpServletRequest request
    ) {
        return criarResposta(HttpStatus.BAD_REQUEST, exception.getMessage(), request, List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroApiResponse> tratarCamposInvalidos(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        List<CampoInvalidoResponse> campos = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::paraCampoInvalido)
                .toList();

        return criarResposta(
                HttpStatus.BAD_REQUEST,
                "Um ou mais campos enviados são inválidos.",
                request,
                campos
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroApiResponse> tratarRestricaoDeParametro(
            ConstraintViolationException exception,
            HttpServletRequest request
    ) {
        List<CampoInvalidoResponse> campos = exception.getConstraintViolations()
                .stream()
                .map(violacao -> CampoInvalidoResponse.builder()
                        .campo(violacao.getPropertyPath().toString())
                        .mensagem(violacao.getMessage())
                        .build())
                .toList();

        return criarResposta(
                HttpStatus.BAD_REQUEST,
                "Um ou mais parâmetros enviados são inválidos.",
                request,
                campos
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroApiResponse> tratarCorpoIlegivel(
            HttpMessageNotReadableException exception,
            HttpServletRequest request
    ) {
        return criarResposta(
                HttpStatus.BAD_REQUEST,
                "O corpo da requisição está ausente ou contém JSON inválido.",
                request,
                List.of()
        );
    }

    private CampoInvalidoResponse paraCampoInvalido(FieldError erro) {
        return CampoInvalidoResponse.builder()
                .campo(erro.getField())
                .mensagem(erro.getDefaultMessage())
                .build();
    }

    private ResponseEntity<ErroApiResponse> criarResposta(
            HttpStatus status,
            String mensagem,
            HttpServletRequest request,
            List<CampoInvalidoResponse> campos
    ) {
        ErroApiResponse erro = ErroApiResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .erro(status.getReasonPhrase())
                .mensagem(mensagem)
                .caminho(request.getRequestURI())
                .campos(campos)
                .build();

        return ResponseEntity.status(status).body(erro);
    }
}
