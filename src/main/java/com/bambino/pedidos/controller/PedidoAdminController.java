package com.bambino.pedidos.controller;

import com.bambino.pedidos.dto.PedidoCambiarEstadoRequest;
import com.bambino.pedidos.dto.PedidoResponse;
import com.bambino.pedidos.service.PedidoEstadoService;
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
@RequestMapping("/api/admin/pedidos")
/**
 * Clase que maneja la funcionalidad de PedidoAdminController.
 */
public class PedidoAdminController {

    private final PedidoEstadoService pedidoEstadoService;
    private final AdminExcelExportService adminExcelExportService;

    public PedidoAdminController(PedidoEstadoService pedidoEstadoService,
                                 AdminExcelExportService adminExcelExportService) {
        this.pedidoEstadoService = pedidoEstadoService;
        this.adminExcelExportService = adminExcelExportService;
    }

    @GetMapping
    /**
     * Metodo que realiza la operacion de listarPedidos.
     * @return resultado de la operacion
     */
    public List<PedidoResponse> listarPedidos() {
        return pedidoEstadoService.listarPedidosAdmin();
    }

    @GetMapping("/exportar-excel")
    public ResponseEntity<StreamingResponseBody> exportarExcel() {
        StreamingResponseBody body = adminExcelExportService::exportarPedidos;
        return excelResponse("pedidos-admin.xlsx", body);
    }

    @PatchMapping("/{idPedido}/estado")
    public PedidoResponse cambiarEstado(
        Authentication authentication,
        @PathVariable Long idPedido,
        @Valid @RequestBody PedidoCambiarEstadoRequest request
    ) {
        return pedidoEstadoService.cambiarEstadoAdmin(authentication.getName(), idPedido, request);
    }

    private ResponseEntity<StreamingResponseBody> excelResponse(String filename, StreamingResponseBody body) {
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .body(body);
    }
}
