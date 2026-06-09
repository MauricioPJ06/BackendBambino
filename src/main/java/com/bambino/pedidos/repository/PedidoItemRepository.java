package com.bambino.pedidos.repository;

import com.bambino.pedidos.entity.PedidoItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interfaz que maneja la funcionalidad de PedidoItemRepository.
 */
public interface PedidoItemRepository extends JpaRepository<PedidoItemEntity, Long> {
    List<PedidoItemEntity> findByIdPedidoOrderByIdPedidoItemAsc(Long idPedido);
}
