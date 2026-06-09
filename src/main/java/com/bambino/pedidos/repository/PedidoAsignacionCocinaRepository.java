package com.bambino.pedidos.repository;

import com.bambino.pedidos.entity.PedidoAsignacionCocinaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interfaz que maneja la funcionalidad de PedidoAsignacionCocinaRepository.
 */
public interface PedidoAsignacionCocinaRepository extends JpaRepository<PedidoAsignacionCocinaEntity, Long> {
    List<PedidoAsignacionCocinaEntity> findByIdPedidoOrderByFechaAsignacionDesc(Long idPedido);
}
