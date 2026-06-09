package com.bambino.pedidos.repository;

import com.bambino.pedidos.entity.ActorTipoPedido;
import com.bambino.pedidos.entity.EstadoPedido;
import com.bambino.pedidos.entity.PedidoEstadoTransicionPermitidaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interfaz que maneja la funcionalidad de PedidoEstadoTransicionPermitidaRepository.
 */
public interface PedidoEstadoTransicionPermitidaRepository extends JpaRepository<PedidoEstadoTransicionPermitidaEntity, Long> {
    Optional<PedidoEstadoTransicionPermitidaEntity> findByEstadoOrigenAndEstadoDestinoAndActorTipoAndActivoTrue(
        EstadoPedido estadoOrigen,
        EstadoPedido estadoDestino,
        ActorTipoPedido actorTipo
    );
}
