package com.bambino.seguridad.repository;

import com.bambino.seguridad.entity.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interfaz que maneja la funcionalidad de RolRepository.
 */
public interface RolRepository extends JpaRepository<RolEntity, Short> {

    Optional<RolEntity> findByNombre(String nombre);
}
