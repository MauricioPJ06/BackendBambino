package com.bambino.reclamaciones.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "libro_reclamaciones")
/**
 * Clase que maneja la funcionalidad de LibroReclamacionEntity.
 */
public class LibroReclamacionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reclamo")
    private Long idReclamo;
    @Column(name = "numero_reclamo", nullable = false, length = 40)
    private String numeroReclamo;
    @Column(name = "id_empresa", nullable = false)
    private Long idEmpresa;
    @Column(name = "id_cliente")
    private Long idCliente;
    @Column(name = "id_pedido")
    private Long idPedido;
    @Column(name = "codigo_pedido", length = 40)
    private String codigoPedido;
    @Column(name = "tipo_registro", nullable = false, length = 20)
    private String tipoRegistro;
    @Column(name = "consumidor_nombres", nullable = false, length = 120)
    private String consumidorNombres;
    @Column(name = "consumidor_apellidos", nullable = false, length = 120)
    private String consumidorApellidos;
    @Column(name = "doc_tipo", nullable = false, length = 10)
    private String docTipo;
    @Column(name = "doc_numero", nullable = false, length = 20)
    private String docNumero;
    @Column(name = "correo", nullable = false, length = 190)
    private String correo;
    @Column(name = "telefono", length = 20)
    private String telefono;
    @Column(name = "direccion_consumidor", length = 300)
    private String direccionConsumidor;
    @Column(name = "fecha_consumo")
    private LocalDateTime fechaConsumo;
    @Column(name = "monto_reclamado", precision = 10, scale = 2)
    private BigDecimal montoReclamado;
    @Column(name = "detalle_hechos", nullable = false, columnDefinition = "TEXT")
    private String detalleHechos;
    @Column(name = "pedido_consumidor", nullable = false, columnDefinition = "TEXT")
    private String pedidoConsumidor;
    @Column(name = "estado", nullable = false, length = 20)
    private String estado;
    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    public Long getIdReclamo() { return idReclamo; }
    public void setIdReclamo(Long idReclamo) { this.idReclamo = idReclamo; }
    public String getNumeroReclamo() { return numeroReclamo; }
    public void setNumeroReclamo(String numeroReclamo) { this.numeroReclamo = numeroReclamo; }
    public Long getIdEmpresa() { return idEmpresa; }
    public void setIdEmpresa(Long idEmpresa) { this.idEmpresa = idEmpresa; }
    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }
    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
    public String getCodigoPedido() { return codigoPedido; }
    public void setCodigoPedido(String codigoPedido) { this.codigoPedido = codigoPedido; }
    public String getTipoRegistro() { return tipoRegistro; }
    public void setTipoRegistro(String tipoRegistro) { this.tipoRegistro = tipoRegistro; }
    public String getConsumidorNombres() { return consumidorNombres; }
    public void setConsumidorNombres(String consumidorNombres) { this.consumidorNombres = consumidorNombres; }
    public String getConsumidorApellidos() { return consumidorApellidos; }
    public void setConsumidorApellidos(String consumidorApellidos) { this.consumidorApellidos = consumidorApellidos; }
    public String getDocTipo() { return docTipo; }
    public void setDocTipo(String docTipo) { this.docTipo = docTipo; }
    public String getDocNumero() { return docNumero; }
    public void setDocNumero(String docNumero) { this.docNumero = docNumero; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccionConsumidor() { return direccionConsumidor; }
    public void setDireccionConsumidor(String direccionConsumidor) { this.direccionConsumidor = direccionConsumidor; }
    public LocalDateTime getFechaConsumo() { return fechaConsumo; }
    public void setFechaConsumo(LocalDateTime fechaConsumo) { this.fechaConsumo = fechaConsumo; }
    public BigDecimal getMontoReclamado() { return montoReclamado; }
    public void setMontoReclamado(BigDecimal montoReclamado) { this.montoReclamado = montoReclamado; }
    public String getDetalleHechos() { return detalleHechos; }
    public void setDetalleHechos(String detalleHechos) { this.detalleHechos = detalleHechos; }
    public String getPedidoConsumidor() { return pedidoConsumidor; }
    public void setPedidoConsumidor(String pedidoConsumidor) { this.pedidoConsumidor = pedidoConsumidor; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
