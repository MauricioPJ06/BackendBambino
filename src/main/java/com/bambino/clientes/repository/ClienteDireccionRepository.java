package com.bambino.clientes.repository;

import com.bambino.clientes.entity.ClienteDireccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que maneja la funcionalidad de ClienteDireccionRepository.
 */
public interface ClienteDireccionRepository extends JpaRepository<ClienteDireccionEntity, Long> {

    List<ClienteDireccionEntity> findByIdClienteAndActivoTrueOrderByEsPrincipalDescIdDireccionDesc(Long idCliente);

    Optional<ClienteDireccionEntity> findByIdDireccionAndIdClienteAndActivoTrue(Long idDireccion, Long idCliente);

    List<ClienteDireccionEntity> findByIdClienteAndActivoTrue(Long idCliente);

    Optional<ClienteDireccionEntity> findFirstByIdClienteAndActivoTrueAndEsPrincipalTrue(Long idCliente);

    @Modifying
    @Query("""
        update ClienteDireccionEntity d
        set d.esPrincipal = false, d.fechaActualizacion = :fecha
        where d.idCliente = :idCliente and d.activo = true and d.esPrincipal = true
        """)
    int desmarcarDireccionesPrincipales(@Param("idCliente") Long idCliente, @Param("fecha") LocalDateTime fecha);
}
