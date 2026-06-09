package com.bambino.delivery.controller;

import com.bambino.delivery.dto.CoberturaDeliveryResponse;
import com.bambino.delivery.dto.UbicacionRestauranteResponse;
import com.bambino.delivery.service.CoberturaDeliveryService;
import com.bambino.shared.exception.NegocioException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/public/delivery")
/**
 * Clase que maneja la funcionalidad de DeliveryCoberturaController.
 */
public class DeliveryCoberturaController {

    private final CoberturaDeliveryService coberturaDeliveryService;

    public DeliveryCoberturaController(CoberturaDeliveryService coberturaDeliveryService) {
        this.coberturaDeliveryService = coberturaDeliveryService;
    }

    @GetMapping("/cobertura")
    public CoberturaDeliveryResponse validarCobertura(
        @RequestParam BigDecimal latitud,
        @RequestParam BigDecimal longitud
    ) {
        if (latitud.scale() > 7 || longitud.scale() > 7) {
            throw new NegocioException("latitud/longitud exceden precision permitida");
        }
        return coberturaDeliveryService.evaluarCobertura(latitud, longitud);
    }

    @GetMapping("/ubicacion-principal")
    /**
     * Metodo que realiza la operacion de obtenerUbicacionPrincipal.
     * @return resultado de la operacion
     */
    public UbicacionRestauranteResponse obtenerUbicacionPrincipal() {
        return coberturaDeliveryService.obtenerUbicacionPrincipal();
    }
}
