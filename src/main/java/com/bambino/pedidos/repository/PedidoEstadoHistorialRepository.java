package com.bambino.pedidos.repository;

import com.bambino.pedidos.entity.PedidoEstadoHistorialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interfaz que maneja la funcionalidad de PedidoEstadoHistorialRepository.
 */
public interface PedidoEstadoHistorialRepository extends JpaRepository<PedidoEstadoHistorialEntity, Long> {
    List<PedidoEstadoHistorialEntity> findByIdPedidoOrderByFechaCreacionAsc(Long idPedido);
}
