package com.bambino.seguridad.controller;

import com.bambino.seguridad.dto.*;
import com.bambino.seguridad.service.AuthClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
/**
 * Clase que maneja la funcionalidad de AuthClienteController.
 */
public class AuthClienteController {

    private final AuthClienteService authClienteService;

    public AuthClienteController(AuthClienteService authClienteService) {
        this.authClienteService = authClienteService;
    }

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    /**
     * Metodo que realiza la operacion de registro.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public RegistroClienteResponse registro(@Valid @RequestBody RegistroClienteRequest request) {
        return authClienteService.registrarCliente(request);
    }

    @PostMapping("/recuperar/solicitar")
    /**
     * Metodo que realiza la operacion de solicitarRecuperacion.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public String solicitarRecuperacion(@Valid @RequestBody RecuperarSolicitarRequest request) {
        return authClienteService.solicitarRecuperacion(request);
    }

    @PostMapping("/recuperar/confirmar")
    /**
     * Metodo que realiza la operacion de confirmarRecuperacion.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public String confirmarRecuperacion(@Valid @RequestBody RecuperarConfirmarRequest request) {
        return authClienteService.confirmarRecuperacion(request);
    }

    @PostMapping("/recuperar/validar-codigo")
    /**
     * Metodo que realiza la operacion de validarCodigoRecuperacion.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public String validarCodigoRecuperacion(@Valid @RequestBody RecuperarValidarCodigoRequest request) {
        return authClienteService.validarCodigoRecuperacion(request);
    }
}
