package com.bambino.comprobantes.repository;

import com.bambino.comprobantes.entity.ComprobanteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que maneja la funcionalidad de ComprobanteRepository.
 */
public interface ComprobanteRepository extends JpaRepository<ComprobanteEntity, Long> {
    Optional<ComprobanteEntity> findByIdPedido(Long idPedido);
    Optional<ComprobanteEntity> findByPdfToken(String pdfToken);
    List<ComprobanteEntity> findAllByOrderByFechaEmisionDesc();
}
