package com.bambino.pedidos.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pedido_asignacion_cocina")
/**
 * Clase que maneja la funcionalidad de PedidoAsignacionCocinaEntity.
 */
public class PedidoAsignacionCocinaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignacion")
    private Long idAsignacion;

    @Column(name = "id_pedido", nullable = false)
    private Long idPedido;

    @Column(name = "id_usuario_cocina", nullable = false)
    private Long idUsuarioCocina;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDateTime fechaAsignacion;

    @Column(name = "motivo", length = 255)
    private String motivo;

    public Long getIdAsignacion() { return idAsignacion; }
    public void setIdAsignacion(Long idAsignacion) { this.idAsignacion = idAsignacion; }
    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
    public Long getIdUsuarioCocina() { return idUsuarioCocina; }
    public void setIdUsuarioCocina(Long idUsuarioCocina) { this.idUsuarioCocina = idUsuarioCocina; }
    public LocalDateTime getFechaAsignacion() { return fechaAsignacion; }
    public void setFechaAsignacion(LocalDateTime fechaAsignacion) { this.fechaAsignacion = fechaAsignacion; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
