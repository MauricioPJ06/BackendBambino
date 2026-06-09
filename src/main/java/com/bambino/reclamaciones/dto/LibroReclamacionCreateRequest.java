package com.bambino.reclamaciones.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Record que maneja la funcionalidad de LibroReclamacionCreateRequest.
 */
public record LibroReclamacionCreateRequest(
    @NotBlank(message = "tipoRegistro es obligatorio")
    @Pattern(regexp = "(?i)RECLAMO|QUEJA", message = "tipoRegistro debe ser RECLAMO o QUEJA")
    String tipoRegistro,

    @NotBlank(message = "nombres es obligatorio")
    @Size(max = 120, message = "nombres excede longitud")
    String nombres,

    @NotBlank(message = "apellidos es obligatorio")
    @Size(max = 120, message = "apellidos excede longitud")
    String apellidos,

    @NotBlank(message = "docTipo es obligatorio")
    @Pattern(regexp = "(?i)DNI|RUC|CE|OTRO", message = "docTipo invalido")
    String docTipo,

    @NotBlank(message = "docNumero es obligatorio")
    @Size(max = 20, message = "docNumero excede longitud")
    String docNumero,

    @NotBlank(message = "correo es obligatorio")
    @Email(message = "correo invalido")
    @Size(max = 190, message = "correo excede longitud")
    String correo,

    @Size(max = 20, message = "telefono excede longitud")
    String telefono,

    @Size(max = 300, message = "direccionConsumidor excede longitud")
    String direccionConsumidor,

    Long idPedido,

    @Size(max = 40, message = "codigoPedido excede longitud")
    String codigoPedido,

    LocalDateTime fechaConsumo,

    @DecimalMin(value = "0.0", inclusive = false, message = "montoReclamado debe ser mayor a 0")
    BigDecimal montoReclamado,

    @NotBlank(message = "detalleHechos es obligatorio")
    @Size(max = 4000, message = "detalleHechos excede longitud")
    String detalleHechos,

    @NotBlank(message = "pedidoConsumidor es obligatorio")
    @Size(max = 4000, message = "pedidoConsumidor excede longitud")
    String pedidoConsumidor
) {}
