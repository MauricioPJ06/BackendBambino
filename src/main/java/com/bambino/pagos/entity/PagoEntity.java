package com.bambino.pagos.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago")
/**
 * Clase que maneja la funcionalidad de PagoEntity.
 */
public class PagoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long idPago;

    @Column(name = "id_pedido", nullable = false)
    private Long idPedido;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo", nullable = false, length = 20)
    private MetodoPago metodo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoPago estado;

    @Column(name = "monto", nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Column(name = "proveedor", length = 80)
    private String proveedor;

    @Column(name = "proveedor_txn_id", length = 120)
    private String proveedorTxnId;

    @Column(name = "idempotency_key", nullable = false, length = 36, unique = true)
    private String idempotencyKey;

    @Column(name = "url_pago")
    private String urlPago;

    @Column(name = "usuario_creacion")
    private Long usuarioCreacion;

    @Column(name = "usuario_actualizacion")
    private Long usuarioActualizacion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    public Long getIdPago() { return idPago; }
    public void setIdPago(Long idPago) { this.idPago = idPago; }
    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
    public MetodoPago getMetodo() { return metodo; }
    public void setMetodo(MetodoPago metodo) { this.metodo = metodo; }
    public EstadoPago getEstado() { return estado; }
    public void setEstado(EstadoPago estado) { this.estado = estado; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public String getProveedor() { return proveedor; }
    public void setProveedor(String proveedor) { this.proveedor = proveedor; }
    public String getProveedorTxnId() { return proveedorTxnId; }
    public void setProveedorTxnId(String proveedorTxnId) { this.proveedorTxnId = proveedorTxnId; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
    public String getUrlPago() { return urlPago; }
    public void setUrlPago(String urlPago) { this.urlPago = urlPago; }
    public Long getUsuarioCreacion() { return usuarioCreacion; }
    public void setUsuarioCreacion(Long usuarioCreacion) { this.usuarioCreacion = usuarioCreacion; }
    public Long getUsuarioActualizacion() { return usuarioActualizacion; }
    public void setUsuarioActualizacion(Long usuarioActualizacion) { this.usuarioActualizacion = usuarioActualizacion; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
