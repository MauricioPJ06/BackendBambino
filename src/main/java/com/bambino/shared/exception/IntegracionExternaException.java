package com.bambino.shared.exception;

import org.springframework.http.HttpStatus;

public class IntegracionExternaException extends RuntimeException {

    private final HttpStatus status;

    public IntegracionExternaException(HttpStatus status, String mensaje) {
        super(mensaje);
        this.status = status;
    }

    public HttpStatus status() {
        return status;
    }
}
