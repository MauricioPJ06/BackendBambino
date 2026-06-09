package com.bambino.pagos.controller;

import com.bambino.pagos.dto.PagoConfirmarRequest;
import com.bambino.pagos.dto.PagoResponse;
import com.bambino.pagos.service.PagoService;
import com.bambino.reportes.service.AdminExcelExportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

@RestController
@RequestMapping("/api/admin/pagos")
/**
 * Clase que maneja la funcionalidad de PagoAdminController.
 */
public class PagoAdminController {

    private final PagoService pagoService;
    private final AdminExcelExportService adminExcelExportService;

    public PagoAdminController(PagoService pagoService,
                               AdminExcelExportService adminExcelExportService) {
        this.pagoService = pagoService;
        this.adminExcelExportService = adminExcelExportService;
    }

    @GetMapping
    /**
     * Metodo que realiza la operacion de listar.
     * @return resultado de la operacion
     */
    public List<PagoResponse> listar() {
        return pagoService.listarPagosAdmin();
    }

    @GetMapping("/exportar-excel")
    public ResponseEntity<StreamingResponseBody> exportarExcel() {
        StreamingResponseBody body = adminExcelExportService::exportarPagos;
        return excelResponse("pagos-admin.xlsx", body);
    }

    @PatchMapping("/{idPago}/confirmar")
    public PagoResponse confirmar(
        Authentication authentication,
        @PathVariable Long idPago,
        @Valid @RequestBody PagoConfirmarRequest request
    ) {
        return pagoService.confirmarPagoAdmin(idPago, request, authentication.getName());
    }

    private ResponseEntity<StreamingResponseBody> excelResponse(String filename, StreamingResponseBody body) {
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .body(body);
    }
}
