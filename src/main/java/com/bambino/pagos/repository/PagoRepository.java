package com.bambino.pagos.repository;

import com.bambino.pagos.entity.PagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que maneja la funcionalidad de PagoRepository.
 */
public interface PagoRepository extends JpaRepository<PagoEntity, Long> {
    Optional<PagoEntity> findByIdempotencyKey(String idempotencyKey);
    List<PagoEntity> findByIdPedidoOrderByFechaCreacionDesc(Long idPedido);
    List<PagoEntity> findAllByOrderByFechaCreacionDesc();
}
