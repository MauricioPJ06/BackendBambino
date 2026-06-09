package com.bambino.pedidos.repository;

import com.bambino.pedidos.entity.PedidoCocinaIncidenciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interfaz que maneja la funcionalidad de PedidoCocinaIncidenciaRepository.
 */
public interface PedidoCocinaIncidenciaRepository extends JpaRepository<PedidoCocinaIncidenciaEntity, Long> {
    List<PedidoCocinaIncidenciaEntity> findByIdPedidoOrderByFechaCreacionDesc(Long idPedido);
}
