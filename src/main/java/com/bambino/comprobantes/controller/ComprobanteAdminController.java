package com.bambino.comprobantes.controller;

import com.bambino.comprobantes.dto.ComprobantePdfResponse;
import com.bambino.comprobantes.dto.ComprobanteResponse;
import com.bambino.comprobantes.service.ComprobantePdfService;
import com.bambino.comprobantes.service.ComprobanteService;
import com.bambino.reportes.service.AdminExcelExportService;
import com.bambino.seguridad.repository.UsuarioRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

@RestController
@RequestMapping("/api/admin/comprobantes")
/**
 * Clase que maneja la funcionalidad de ComprobanteAdminController.
 */
public class ComprobanteAdminController {

    private final ComprobanteService comprobanteService;
    private final ComprobantePdfService comprobantePdfService;
    private final AdminExcelExportService adminExcelExportService;
    private final UsuarioRepository usuarioRepository;

    public ComprobanteAdminController(ComprobanteService comprobanteService,
                                      ComprobantePdfService comprobantePdfService,
                                      AdminExcelExportService adminExcelExportService,
                                      UsuarioRepository usuarioRepository) {
        this.comprobanteService = comprobanteService;
        this.comprobantePdfService = comprobantePdfService;
        this.adminExcelExportService = adminExcelExportService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    /**
     * Metodo que realiza la operacion de listar.
     * @return resultado de la operacion
     */
    public List<ComprobanteResponse> listar() {
        return comprobanteService.listarAdmin();
    }

    @GetMapping("/exportar-excel")
    public ResponseEntity<StreamingResponseBody> exportarExcel() {
        StreamingResponseBody body = adminExcelExportService::exportarComprobantes;
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"comprobantes-admin.xlsx\"")
            .body(body);
    }

    @GetMapping("/{idComprobante}/pdf")
    public ResponseEntity<byte[]> obtenerPdf(@PathVariable Long idComprobante) {
        ComprobantePdfResponse pdf = comprobantePdfService.generarPdfAdmin(idComprobante);
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + pdf.filename() + "\"")
            .body(pdf.contenido());
    }

    @PostMapping("/{idComprobante}/enviar-correo")
    public ComprobanteResponse enviarCorreo(@PathVariable Long idComprobante, Authentication authentication) {
        Long actorId = usuarioRepository.findByEmail(authentication.getName())
            .map(usuario -> usuario.getIdUsuario())
            .orElse(null);
        return comprobanteService.enviarCorreoAdmin(idComprobante, actorId);
    }
}
