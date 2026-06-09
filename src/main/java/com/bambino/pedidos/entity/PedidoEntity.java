package com.bambino.pedidos.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedido")
/**
 * Clase que maneja la funcionalidad de PedidoEntity.
 */
public class PedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long idPedido;

    @Column(name = "codigo_pedido", nullable = false, length = 40, unique = true)
    private String codigoPedido;

    @Column(name = "id_cliente", nullable = false)
    private Long idCliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "canal_origen", nullable = false, length = 20)
    private CanalPedido canalOrigen;

    @Enumerated(EnumType.STRING)
    @Column(name = "modalidad", nullable = false, length = 10)
    private ModalidadPedido modalidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_actual", nullable = false, length = 20)
    private EstadoPedido estadoActual;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_comprobante", nullable = false, length = 10)
    private TipoComprobantePedido tipoComprobante;

    @Enumerated(EnumType.STRING)
    @Column(name = "cliente_doc_tipo_snapshot", length = 10)
    private DocReceptorPedido docReceptorTipo;

    @Column(name = "cliente_doc_numero_snapshot", length = 20)
    private String docReceptorNumero;

    @Column(name = "id_direccion_entrega")
    private Long idDireccionEntrega;

    @Column(name = "razon_social_snapshot", length = 200)
    private String razonSocialSnapshot;

    @Column(name = "direccion_fiscal_snapshot", length = 300)
    private String direccionFiscalSnapshot;

    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "descuento_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal descuentoTotal;

    @Column(name = "impuesto_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal impuestoTotal;

    @Column(name = "total", nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(name = "usuario_cocina_preparacion")
    private Long usuarioCocinaPreparacion;

    @Column(name = "fecha_inicio_preparacion")
    private LocalDateTime fechaInicioPreparacion;

    @Column(name = "fecha_fin_preparacion")
    private LocalDateTime fechaFinPreparacion;

    @Column(name = "usuario_creacion")
    private Long usuarioCreacion;

    @Column(name = "usuario_actualizacion")
    private Long usuarioActualizacion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
    public String getCodigoPedido() { return codigoPedido; }
    public void setCodigoPedido(String codigoPedido) { this.codigoPedido = codigoPedido; }
    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }
    public CanalPedido getCanalOrigen() { return canalOrigen; }
    public void setCanalOrigen(CanalPedido canalOrigen) { this.canalOrigen = canalOrigen; }
    public ModalidadPedido getModalidad() { return modalidad; }
    public void setModalidad(ModalidadPedido modalidad) { this.modalidad = modalidad; }
    public EstadoPedido getEstadoActual() { return estadoActual; }
    public void setEstadoActual(EstadoPedido estadoActual) { this.estadoActual = estadoActual; }
    public TipoComprobantePedido getTipoComprobante() { return tipoComprobante; }
    public void setTipoComprobante(TipoComprobantePedido tipoComprobante) { this.tipoComprobante = tipoComprobante; }
    public DocReceptorPedido getDocReceptorTipo() { return docReceptorTipo; }
    public void setDocReceptorTipo(DocReceptorPedido docReceptorTipo) { this.docReceptorTipo = docReceptorTipo; }
    public String getDocReceptorNumero() { return docReceptorNumero; }
    public void setDocReceptorNumero(String docReceptorNumero) { this.docReceptorNumero = docReceptorNumero; }
    public Long getIdDireccionEntrega() { return idDireccionEntrega; }
    public void setIdDireccionEntrega(Long idDireccionEntrega) { this.idDireccionEntrega = idDireccionEntrega; }
    public String getRazonSocialSnapshot() { return razonSocialSnapshot; }
    public void setRazonSocialSnapshot(String razonSocialSnapshot) { this.razonSocialSnapshot = razonSocialSnapshot; }
    public String getDireccionFiscalSnapshot() { return direccionFiscalSnapshot; }
    public void setDireccionFiscalSnapshot(String direccionFiscalSnapshot) { this.direccionFiscalSnapshot = direccionFiscalSnapshot; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getDescuentoTotal() { return descuentoTotal; }
    public void setDescuentoTotal(BigDecimal descuentoTotal) { this.descuentoTotal = descuentoTotal; }
    public BigDecimal getImpuestoTotal() { return impuestoTotal; }
    public void setImpuestoTotal(BigDecimal impuestoTotal) { this.impuestoTotal = impuestoTotal; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public Long getUsuarioCocinaPreparacion() { return usuarioCocinaPreparacion; }
    public void setUsuarioCocinaPreparacion(Long usuarioCocinaPreparacion) { this.usuarioCocinaPreparacion = usuarioCocinaPreparacion; }
    public LocalDateTime getFechaInicioPreparacion() { return fechaInicioPreparacion; }
    public void setFechaInicioPreparacion(LocalDateTime fechaInicioPreparacion) { this.fechaInicioPreparacion = fechaInicioPreparacion; }
    public LocalDateTime getFechaFinPreparacion() { return fechaFinPreparacion; }
    public void setFechaFinPreparacion(LocalDateTime fechaFinPreparacion) { this.fechaFinPreparacion = fechaFinPreparacion; }
    public Long getUsuarioCreacion() { return usuarioCreacion; }
    public void setUsuarioCreacion(Long usuarioCreacion) { this.usuarioCreacion = usuarioCreacion; }
    public Long getUsuarioActualizacion() { return usuarioActualizacion; }
    public void setUsuarioActualizacion(Long usuarioActualizacion) { this.usuarioActualizacion = usuarioActualizacion; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
