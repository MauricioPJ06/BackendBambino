package com.bambino.reclamaciones.dto;

import java.time.LocalDateTime;

/**
 * Record que maneja la funcionalidad de LibroReclamacionResponse.
 */
public record LibroReclamacionResponse(
    Long idReclamo,
    String numeroReclamo,
    String estado,
    LocalDateTime fechaRegistro,
    Long idEmpresa,
    Long idCliente,
    Long idPedido,
    String codigoPedido,
    String tipoRegistro,
    String nombres,
    String apellidos,
    String docTipo,
    String docNumero,
    String correo,
    String telefono,
    String direccionConsumidor,
    String detalleHechos,
    String pedidoConsumidor
) {}
