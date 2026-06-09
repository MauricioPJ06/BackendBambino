package com.bambino.pedidos.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedido_item")
/**
 * Clase que maneja la funcionalidad de PedidoItemEntity.
 */
public class PedidoItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido_item")
    private Long idPedidoItem;

    @Column(name = "id_pedido", nullable = false)
    private Long idPedido;

    @Column(name = "id_producto")
    private Long idProducto;

    @Column(name = "nombre_producto_snapshot", nullable = false, length = 180)
    private String nombreProductoSnapshot;

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

    public Long getIdPedidoItem() { return idPedidoItem; }
    public void setIdPedidoItem(Long idPedidoItem) { this.idPedidoItem = idPedidoItem; }
    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }
    public String getNombreProductoSnapshot() { return nombreProductoSnapshot; }
    public void setNombreProductoSnapshot(String nombreProductoSnapshot) { this.nombreProductoSnapshot = nombreProductoSnapshot; }
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
