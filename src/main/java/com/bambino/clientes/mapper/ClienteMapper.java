package com.bambino.clientes.mapper;

import com.bambino.clientes.dto.ClientePerfilResponse;
import com.bambino.clientes.dto.DireccionResponse;
import com.bambino.clientes.entity.ClienteDireccionEntity;
import com.bambino.clientes.entity.ClientePerfilEntity;
import org.springframework.stereotype.Component;

@Component
/**
 * Clase que maneja la funcionalidad de ClienteMapper.
 */
public class ClienteMapper {

    public ClientePerfilResponse toPerfilResponse(ClientePerfilEntity perfil,
                                                  String nombres,
                                                  String apellidos,
                                                  String correo,
                                                  String telefono) {
        return new ClientePerfilResponse(
            perfil.getIdCliente(),
            nombres,
            apellidos,
            correo,
            telefono,
            perfil.getDocTipo() == null ? null : perfil.getDocTipo().name(),
            perfil.getDocNumero()
        );
    }

    /**
     * Metodo que realiza la operacion de toDireccionResponse.
     * @param direccion parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public DireccionResponse toDireccionResponse(ClienteDireccionEntity direccion) {
        return new DireccionResponse(
            direccion.getIdDireccion(),
            direccion.getDireccionLinea1(),
            direccion.getReferencia(),
            direccion.getDistrito(),
            direccion.getCiudad(),
            direccion.getLatitud(),
            direccion.getLongitud(),
            direccion.getGooglePlaceId(),
            direccion.getGooglePlusCode(),
            direccion.getEsPrincipal(),
            direccion.getActivo(),
            direccion.getFechaCreacion(),
            direccion.getFechaActualizacion()
        );
    }
}
