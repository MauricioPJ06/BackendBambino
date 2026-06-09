package com.bambino.shared.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.CannotAcquireLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.OffsetDateTime;
import java.util.List;

@RestControllerAdvice
/**
 * Clase que maneja la funcionalidad de GlobalExceptionHandler.
 */
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    /**
     * Metodo que realiza la operacion de manejarValidacionBody.
     * @param ex parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ResponseEntity<ApiError> manejarValidacionBody(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ApiErrorDetalle> detalles = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> new ApiErrorDetalle(error.getField(), error.getDefaultMessage()))
            .toList();

        return construirErrorResponse(
            HttpStatus.BAD_REQUEST,
            "datos de entrada invalidos",
            request.getRequestURI(),
            detalles
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    /**
     * Metodo que realiza la operacion de manejarValidacionParametros.
     * @param ex parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ResponseEntity<ApiError> manejarValidacionParametros(ConstraintViolationException ex, HttpServletRequest request) {
        List<ApiErrorDetalle> detalles = ex.getConstraintViolations()
            .stream()
            .map(v -> new ApiErrorDetalle(v.getPropertyPath().toString(), v.getMessage()))
            .toList();

        return construirErrorResponse(
            HttpStatus.BAD_REQUEST,
            "parametros invalidos",
            request.getRequestURI(),
            detalles
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    /**
     * Metodo que realiza la operacion de manejarTipoInvalido.
     * @param ex parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ResponseEntity<ApiError> manejarTipoInvalido(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String campo = ex.getName();
        String mensaje = "valor invalido para el parametro";

        return construirErrorResponse(
            HttpStatus.BAD_REQUEST,
            mensaje,
            request.getRequestURI(),
            List.of(new ApiErrorDetalle(campo, mensaje))
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    /**
     * Metodo que realiza la operacion de manejarJsonInvalido.
     * @param ex parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ResponseEntity<ApiError> manejarJsonInvalido(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return construirErrorResponse(
            HttpStatus.BAD_REQUEST,
            "cuerpo json invalido",
            request.getRequestURI(),
            List.of()
        );
    }

    @ExceptionHandler(NegocioException.class)
    /**
     * Metodo que realiza la operacion de manejarNegocio.
     * @param ex parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ResponseEntity<ApiError> manejarNegocio(NegocioException ex, HttpServletRequest request) {
        return construirErrorResponse(
            HttpStatus.UNPROCESSABLE_ENTITY,
            ex.getMessage(),
            request.getRequestURI(),
            List.of()
        );
    }

    @ExceptionHandler(IntegracionExternaException.class)
    public ResponseEntity<ApiError> manejarIntegracionExterna(IntegracionExternaException ex, HttpServletRequest request) {
        return construirErrorResponse(
            ex.status(),
            ex.getMessage(),
            request.getRequestURI(),
            List.of()
        );
    }

    @ExceptionHandler(CannotAcquireLockException.class)
    public ResponseEntity<ApiError> manejarBloqueoTemporal(CannotAcquireLockException ex, HttpServletRequest request) {
        return construirErrorResponse(
            HttpStatus.CONFLICT,
            "operacion temporalmente ocupada, intenta nuevamente",
            request.getRequestURI(),
            List.of()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    /**
     * Metodo que realiza la operacion de manejarAccesoDenegado.
     * @param ex parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ResponseEntity<ApiError> manejarAccesoDenegado(AccessDeniedException ex, HttpServletRequest request) {
        return construirErrorResponse(
            HttpStatus.FORBIDDEN,
            "acceso denegado",
            request.getRequestURI(),
            List.of()
        );
    }

    @ExceptionHandler(Exception.class)
    /**
     * Metodo que realiza la operacion de manejarGeneral.
     * @param ex parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ResponseEntity<ApiError> manejarGeneral(Exception ex, HttpServletRequest request) {
        log.error("Error interno no controlado en {}", request.getRequestURI(), ex);
        return construirErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "error interno del servidor",
            request.getRequestURI(),
            List.of()
        );
    }

    private ResponseEntity<ApiError> construirErrorResponse(HttpStatus estado, String mensaje, String ruta, List<ApiErrorDetalle> detalles) {
        ApiError body = new ApiError(
            OffsetDateTime.now(),
            estado.value(),
            estado.getReasonPhrase(),
            mensaje,
            ruta,
            detalles
        );
        return ResponseEntity.status(estado).body(body);
    }
}
