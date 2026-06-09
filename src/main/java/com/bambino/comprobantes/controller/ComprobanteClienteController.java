package com.bambino.comprobantes.controller;

import com.bambino.comprobantes.dto.ComprobantePdfResponse;
import com.bambino.comprobantes.dto.ComprobanteResponse;
import com.bambino.comprobantes.service.ComprobantePdfService;
import com.bambino.comprobantes.service.ComprobanteService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cliente/comprobantes")
/**
 * Clase que maneja la funcionalidad de ComprobanteClienteController.
 */
public class ComprobanteClienteController {

    private final ComprobanteService comprobanteService;
    private final ComprobantePdfService comprobantePdfService;

    public ComprobanteClienteController(ComprobanteService comprobanteService, ComprobantePdfService comprobantePdfService) {
        this.comprobanteService = comprobanteService;
        this.comprobantePdfService = comprobantePdfService;
    }

    @GetMapping("/pedido/{idPedido}")
    /**
     * Metodo que realiza la operacion de obtenerPorPedido.
     * @param authentication parametro de entrada para la operacion
     * @param idPedido parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ComprobanteResponse obtenerPorPedido(Authentication authentication, @PathVariable Long idPedido) {
        return comprobanteService.obtenerPorPedidoCliente(authentication.getName(), idPedido);
    }

    @GetMapping("/pedido/{idPedido}/pdf")
    public ResponseEntity<byte[]> obtenerPdfPorPedido(Authentication authentication, @PathVariable Long idPedido) {
        ComprobantePdfResponse pdf = comprobantePdfService.generarPdfClientePorPedido(authentication.getName(), idPedido);
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + pdf.filename() + "\"")
            .body(pdf.contenido());
    }
}
