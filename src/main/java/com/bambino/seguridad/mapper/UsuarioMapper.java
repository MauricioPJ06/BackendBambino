package com.bambino.seguridad.mapper;

import com.bambino.seguridad.dto.UsuarioResponse;
import com.bambino.seguridad.entity.UsuarioEntity;
import org.springframework.stereotype.Component;

@Component
/**
 * Clase que maneja la funcionalidad de UsuarioMapper.
 */
public class UsuarioMapper {

    /**
     * Metodo que realiza la operacion de toResponse.
     * @param entity parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public UsuarioResponse toResponse(UsuarioEntity entity) {
        return new UsuarioResponse(
            entity.getIdUsuario(),
            entity.getUuidUsuario(),
            entity.getEmail(),
            entity.getNombres(),
            entity.getApellidos(),
            entity.getTelefono(),
            entity.getEstado().name(),
            entity.getRol().getNombre(),
            entity.getFechaCreacion(),
            entity.getFechaActualizacion()
        );
    }
}
