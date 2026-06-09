package com.bambino.carrito.repository;

import com.bambino.carrito.entity.ConfiguracionGlobalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interfaz que maneja la funcionalidad de ConfiguracionGlobalRepository.
 */
public interface ConfiguracionGlobalRepository extends JpaRepository<ConfiguracionGlobalEntity, Short> {
}
