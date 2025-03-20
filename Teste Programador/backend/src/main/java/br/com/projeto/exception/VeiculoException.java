package br.com.projeto.exception;

public class VeiculoException extends RuntimeException {

    public VeiculoException(String message) {
        super(message);
    }
    public VeiculoException(String message, Throwable cause) {
        super(message, cause);
    }
}
