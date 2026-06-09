package com.bambino.shared.exception;

/**
 * Clase que maneja la funcionalidad de NegocioException.
 */
public class NegocioException extends RuntimeException {

    public NegocioException(String mensaje) {
        super(mensaje);
    }
}
