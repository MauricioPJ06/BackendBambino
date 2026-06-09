package com.bambino.comprobantes.repository;

import com.bambino.comprobantes.entity.EmpresaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interfaz que maneja la funcionalidad de EmpresaRepository.
 */
public interface EmpresaRepository extends JpaRepository<EmpresaEntity, Long> {
    Optional<EmpresaEntity> findFirstByActivoTrueOrderByIdEmpresaAsc();
}
