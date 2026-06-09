package com.bambino.seguridad.repository;

import com.bambino.seguridad.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que maneja la funcionalidad de UsuarioRepository.
 */
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByDocumento(String documento);

    List<UsuarioEntity> findAllByOrderByIdUsuarioDesc();
}
