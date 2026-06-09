package com.bambino.comprobantes.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Record que maneja la funcionalidad de ComprobanteResponse.
 */
public record ComprobanteResponse(
    Long idComprobante,
    Long idPedido,
    String tipo,
    String serie,
    Long correlativo,
    String numeroCompleto,
    String estado,
    String docReceptorTipo,
    String docReceptorNumero,
    String razonSocialReceptor,
    String direccionFiscalReceptor,
    BigDecimal subtotal,
    BigDecimal impuestoTotal,
    BigDecimal total,
    LocalDateTime fechaEmision,
    Boolean correoEnviado,
    String correoDestino,
    LocalDateTime fechaCorreoEnvio,
    String correoError,
    String pdfPath,
    String pdfToken,
    LocalDateTime fechaPdfGenerado,
    List<ComprobanteDetalleResponse> detalle
) {}
