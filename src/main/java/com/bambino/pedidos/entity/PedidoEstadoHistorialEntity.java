package com.bambino.pedidos.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pedido_estado_historial")
/**
 * Clase que maneja la funcionalidad de PedidoEstadoHistorialEntity.
 */
public class PedidoEstadoHistorialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Long idHistorial;

    @Column(name = "id_pedido", nullable = false)
    private Long idPedido;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_origen", length = 20)
    private EstadoPedido estadoOrigen;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_destino", nullable = false, length = 20)
    private EstadoPedido estadoDestino;

    @Column(name = "actor_id")
    private Long actorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "actor_tipo", nullable = false, length = 20)
    private ActorTipoPedido actorTipo;

    @Column(name = "motivo", length = 255)
    private String motivo;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    public Long getIdHistorial() { return idHistorial; }
    public void setIdHistorial(Long idHistorial) { this.idHistorial = idHistorial; }
    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
    public EstadoPedido getEstadoOrigen() { return estadoOrigen; }
    public void setEstadoOrigen(EstadoPedido estadoOrigen) { this.estadoOrigen = estadoOrigen; }
    public EstadoPedido getEstadoDestino() { return estadoDestino; }
    public void setEstadoDestino(EstadoPedido estadoDestino) { this.estadoDestino = estadoDestino; }
    public Long getActorId() { return actorId; }
    public void setActorId(Long actorId) { this.actorId = actorId; }
    public ActorTipoPedido getActorTipo() { return actorTipo; }
    public void setActorTipo(ActorTipoPedido actorTipo) { this.actorTipo = actorTipo; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
