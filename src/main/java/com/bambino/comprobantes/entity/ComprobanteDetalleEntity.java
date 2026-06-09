package com.bambino.comprobantes.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "comprobante_detalle")
/**
 * Clase que maneja la funcionalidad de ComprobanteDetalleEntity.
 */
public class ComprobanteDetalleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comprobante_detalle")
    private Long idComprobanteDetalle;

    @Column(name = "id_comprobante", nullable = false)
    private Long idComprobante;

    @Column(name = "id_pedido_item")
    private Long idPedidoItem;

    @Column(name = "descripcion_item", nullable = false, length = 220)
    private String descripcionItem;

    @Column(name = "cantidad", nullable = false, precision = 10, scale = 3)
    private BigDecimal cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "descuento_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal descuentoUnitario;

    @Column(name = "subtotal_linea", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotalLinea;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    public Long getIdComprobanteDetalle() { return idComprobanteDetalle; }
    public void setIdComprobanteDetalle(Long idComprobanteDetalle) { this.idComprobanteDetalle = idComprobanteDetalle; }
    public Long getIdComprobante() { return idComprobante; }
    public void setIdComprobante(Long idComprobante) { this.idComprobante = idComprobante; }
    public Long getIdPedidoItem() { return idPedidoItem; }
    public void setIdPedidoItem(Long idPedidoItem) { this.idPedidoItem = idPedidoItem; }
    public String getDescripcionItem() { return descripcionItem; }
    public void setDescripcionItem(String descripcionItem) { this.descripcionItem = descripcionItem; }
    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    public BigDecimal getDescuentoUnitario() { return descuentoUnitario; }
    public void setDescuentoUnitario(BigDecimal descuentoUnitario) { this.descuentoUnitario = descuentoUnitario; }
    public BigDecimal getSubtotalLinea() { return subtotalLinea; }
    public void setSubtotalLinea(BigDecimal subtotalLinea) { this.subtotalLinea = subtotalLinea; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
