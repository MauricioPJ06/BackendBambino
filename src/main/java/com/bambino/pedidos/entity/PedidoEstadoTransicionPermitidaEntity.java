package com.bambino.pedidos.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pedido_estado_transicion_permitida")
/**
 * Clase que maneja la funcionalidad de PedidoEstadoTransicionPermitidaEntity.
 */
public class PedidoEstadoTransicionPermitidaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transicion")
    private Long idTransicion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_origen", nullable = false, length = 20)
    private EstadoPedido estadoOrigen;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_destino", nullable = false, length = 20)
    private EstadoPedido estadoDestino;

    @Enumerated(EnumType.STRING)
    @Column(name = "actor_tipo", nullable = false, length = 20)
    private ActorTipoPedido actorTipo;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    public Long getIdTransicion() { return idTransicion; }
    public void setIdTransicion(Long idTransicion) { this.idTransicion = idTransicion; }
    public EstadoPedido getEstadoOrigen() { return estadoOrigen; }
    public void setEstadoOrigen(EstadoPedido estadoOrigen) { this.estadoOrigen = estadoOrigen; }
    public EstadoPedido getEstadoDestino() { return estadoDestino; }
    public void setEstadoDestino(EstadoPedido estadoDestino) { this.estadoDestino = estadoDestino; }
    public ActorTipoPedido getActorTipo() { return actorTipo; }
    public void setActorTipo(ActorTipoPedido actorTipo) { this.actorTipo = actorTipo; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
