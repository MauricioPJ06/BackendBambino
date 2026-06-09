package com.bambino.documentos.controller;

import com.bambino.documentos.dto.ConsultaDocumentoResponse;
import com.bambino.documentos.service.DocumentoConsultaService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/public/documentos")
public class DocumentoConsultaController {

    private final DocumentoConsultaService documentoConsultaService;

    public DocumentoConsultaController(DocumentoConsultaService documentoConsultaService) {
        this.documentoConsultaService = documentoConsultaService;
    }

    @GetMapping("/consultar")
    public ConsultaDocumentoResponse consultar(
        @RequestParam
        @NotBlank(message = "documento es obligatorio")
        @Size(max = 20, message = "documento excede longitud")
        String documento
    ) {
        return documentoConsultaService.consultar(documento);
    }
}
