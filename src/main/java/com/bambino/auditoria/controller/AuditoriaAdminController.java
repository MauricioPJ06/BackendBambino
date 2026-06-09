package com.bambino.auditoria.controller;

import com.bambino.auditoria.dto.AuditoriaEventoResponse;
import com.bambino.auditoria.service.AuditoriaService;
import com.bambino.reportes.service.AdminExcelExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

@RestController
@RequestMapping("/api/admin/auditoria")
/**
 * Clase que maneja la funcionalidad de AuditoriaAdminController.
 */
public class AuditoriaAdminController {

    private final AuditoriaService auditoriaService;
    private final AdminExcelExportService adminExcelExportService;

    public AuditoriaAdminController(AuditoriaService auditoriaService,
                                    AdminExcelExportService adminExcelExportService) {
        this.auditoriaService = auditoriaService;
        this.adminExcelExportService = adminExcelExportService;
    }

    @GetMapping("/eventos")
    public List<AuditoriaEventoResponse> listar(@RequestParam(required = false) String entidad,
                                                @RequestParam(required = false) String accion,
                                                @RequestParam(required = false) String actorTipo) {
        return auditoriaService.listar(entidad, accion, actorTipo);
    }

    @GetMapping("/eventos/exportar-excel")
    public ResponseEntity<StreamingResponseBody> exportarExcel(@RequestParam(required = false) String entidad,
                                                               @RequestParam(required = false) String accion,
                                                               @RequestParam(required = false) String actorTipo) {
        StreamingResponseBody body = outputStream -> adminExcelExportService.exportarAuditoria(outputStream, entidad, accion, actorTipo);
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"auditoria-admin.xlsx\"")
            .body(body);
    }
}
