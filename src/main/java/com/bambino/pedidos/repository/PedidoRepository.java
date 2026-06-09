package com.bambino.pedidos.repository;

import com.bambino.pedidos.entity.PedidoEntity;
import com.bambino.pedidos.entity.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interfaz que maneja la funcionalidad de PedidoRepository.
 */
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {
    List<PedidoEntity> findByIdClienteOrderByFechaCreacionDesc(Long idCliente);
    List<PedidoEntity> findAllByOrderByFechaCreacionDesc();
    List<PedidoEntity> findByEstadoActualInOrderByFechaCreacionAsc(List<EstadoPedido> estados);
    boolean existsByCodigoPedido(String codigoPedido);
    java.util.Optional<PedidoEntity> findByCodigoPedido(String codigoPedido);
}
