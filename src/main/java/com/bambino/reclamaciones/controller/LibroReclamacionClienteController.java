package com.bambino.reclamaciones.controller;

import com.bambino.reclamaciones.dto.LibroReclamacionCreateRequest;
import com.bambino.reclamaciones.dto.LibroReclamacionResponse;
import com.bambino.reclamaciones.service.LibroReclamacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cliente/libro-reclamaciones")
/**
 * Clase que maneja la funcionalidad de LibroReclamacionClienteController.
 */
public class LibroReclamacionClienteController {

    private final LibroReclamacionService libroReclamacionService;

    public LibroReclamacionClienteController(LibroReclamacionService libroReclamacionService) {
        this.libroReclamacionService = libroReclamacionService;
    }

    @GetMapping("/prefill")
    /**
     * Metodo que realiza la operacion de prefill.
     * @param authentication parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public LibroReclamacionCreateRequest prefill(Authentication authentication) {
        return libroReclamacionService.prefillCliente(authentication.getName());
    }

    @GetMapping
    /**
     * Metodo que realiza la operacion de listarMisReclamos.
     * @param authentication parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public List<LibroReclamacionResponse> listarMisReclamos(Authentication authentication) {
        return libroReclamacionService.listarMisReclamos(authentication.getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LibroReclamacionResponse crear(Authentication authentication,
                                          @Valid @RequestBody LibroReclamacionCreateRequest request) {
        return libroReclamacionService.crearCliente(authentication.getName(), request);
    }
}
