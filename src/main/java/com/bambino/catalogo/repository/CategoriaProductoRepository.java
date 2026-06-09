package com.bambino.catalogo.repository;

import com.bambino.catalogo.entity.CategoriaProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interfaz que maneja la funcionalidad de CategoriaProductoRepository.
 */
public interface CategoriaProductoRepository extends JpaRepository<CategoriaProductoEntity, Long> {
    List<CategoriaProductoEntity> findByActivaTrueOrderByOrdenVisualAscNombreAsc();
    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCaseAndIdCategoriaNot(String nombre, Long idCategoria);
    boolean existsByOrdenVisual(Integer ordenVisual);
    boolean existsByOrdenVisualAndIdCategoriaNot(Integer ordenVisual, Long idCategoria);
}
