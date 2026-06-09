package com.bambino.comprobantes.controller;

import com.bambino.comprobantes.dto.ComprobantePdfResponse;
import com.bambino.comprobantes.service.ComprobantePdfService;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/comprobantes")
public class ComprobantePublicController {

    private final ComprobantePdfService comprobantePdfService;

    public ComprobantePublicController(ComprobantePdfService comprobantePdfService) {
        this.comprobantePdfService = comprobantePdfService;
    }

    @GetMapping("/t/{token}")
    public ResponseEntity<byte[]> obtenerPdfPorToken(@PathVariable String token) {
        ComprobantePdfResponse pdf = comprobantePdfService.generarPdfPublicoPorToken(token);
        return ResponseEntity.ok()
            .cacheControl(CacheControl.noStore())
            .contentType(MediaType.APPLICATION_PDF)
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + pdf.filename() + "\"")
            .body(pdf.contenido());
    }
}
