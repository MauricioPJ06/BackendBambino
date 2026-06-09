package com.bambino.pedidos.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pedido_cocina_incidencia")
/**
 * Clase que maneja la funcionalidad de PedidoCocinaIncidenciaEntity.
 */
public class PedidoCocinaIncidenciaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_incidencia")
    private Long idIncidencia;

    @Column(name = "id_pedido", nullable = false)
    private Long idPedido;

    @Column(name = "id_usuario_cocina", nullable = false)
    private Long idUsuarioCocina;

    @Column(name = "tipo_incidencia", nullable = false, length = 40)
    private String tipoIncidencia;

    @Column(name = "detalle", nullable = false, length = 255)
    private String detalle;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    public Long getIdIncidencia() { return idIncidencia; }
    public void setIdIncidencia(Long idIncidencia) { this.idIncidencia = idIncidencia; }
    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
    public Long getIdUsuarioCocina() { return idUsuarioCocina; }
    public void setIdUsuarioCocina(Long idUsuarioCocina) { this.idUsuarioCocina = idUsuarioCocina; }
    public String getTipoIncidencia() { return tipoIncidencia; }
    public void setTipoIncidencia(String tipoIncidencia) { this.tipoIncidencia = tipoIncidencia; }
    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
