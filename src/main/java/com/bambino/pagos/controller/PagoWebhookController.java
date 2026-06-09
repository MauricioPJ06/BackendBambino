package com.bambino.pagos.controller;

import com.bambino.pagos.dto.PagoResponse;
import com.bambino.pagos.dto.PagoWebhookRequest;
import com.bambino.pagos.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/pagos")
/**
 * Clase que maneja la funcionalidad de PagoWebhookController.
 */
public class PagoWebhookController {

    private final PagoService pagoService;

    public PagoWebhookController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PatchMapping("/webhook")
    /**
     * Metodo que realiza la operacion de confirmarWebhook.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public PagoResponse confirmarWebhook(@Valid @RequestBody PagoWebhookRequest request) {
        return pagoService.confirmarPagoWebhookPublico(request);
    }
}
