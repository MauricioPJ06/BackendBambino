package com.bambino.clientes.dto;

/**
 * Record que maneja la funcionalidad de ClientePerfilResponse.
 */
public record ClientePerfilResponse(
    Long idCliente,
    String nombres,
    String apellidos,
    String correo,
    String telefono,
    String docTipo,
    String docNumero
) {
}
