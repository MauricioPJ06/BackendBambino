package com.bambino.reclamaciones.controller;

import com.bambino.reclamaciones.dto.LibroReclamacionCreateRequest;
import com.bambino.reclamaciones.dto.LibroReclamacionResponse;
import com.bambino.reclamaciones.service.LibroReclamacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/libro-reclamaciones")
/**
 * Clase que maneja la funcionalidad de LibroReclamacionPublicController.
 */
public class LibroReclamacionPublicController {

    private final LibroReclamacionService libroReclamacionService;

    public LibroReclamacionPublicController(LibroReclamacionService libroReclamacionService) {
        this.libroReclamacionService = libroReclamacionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    /**
     * Metodo que realiza la operacion de crear.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public LibroReclamacionResponse crear(@Valid @RequestBody LibroReclamacionCreateRequest request) {
        return libroReclamacionService.crearPublico(request);
    }
}
