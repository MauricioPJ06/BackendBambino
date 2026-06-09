package com.bambino.comprobantes.repository;

import com.bambino.comprobantes.entity.SerieComprobanteEntity;
import com.bambino.comprobantes.entity.TipoComprobante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interfaz que maneja la funcionalidad de SerieComprobanteRepository.
 */
public interface SerieComprobanteRepository extends JpaRepository<SerieComprobanteEntity, Long> {
    Optional<SerieComprobanteEntity> findFirstByIdEmpresaAndTipoComprobanteAndActivoTrueOrderByIdSerieAsc(Long idEmpresa, TipoComprobante tipoComprobante);
}
