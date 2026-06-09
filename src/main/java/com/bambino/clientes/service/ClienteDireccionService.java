package com.bambino.clientes.service;

import com.bambino.clientes.dto.DireccionRequest;
import com.bambino.clientes.dto.DireccionResponse;
import com.bambino.clientes.entity.ClienteDireccionEntity;
import com.bambino.clientes.mapper.ClienteMapper;
import com.bambino.clientes.repository.ClienteDireccionRepository;
import com.bambino.seguridad.entity.UsuarioEntity;
import com.bambino.seguridad.repository.UsuarioRepository;
import com.bambino.shared.exception.NegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

@Service
/**
 * Clase que maneja la funcionalidad de ClienteDireccionService.
 */
public class ClienteDireccionService {

    private final ClienteDireccionRepository clienteDireccionRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteMapper clienteMapper;

    public ClienteDireccionService(ClienteDireccionRepository clienteDireccionRepository,
                                   UsuarioRepository usuarioRepository,
                                   ClienteMapper clienteMapper) {
        this.clienteDireccionRepository = clienteDireccionRepository;
        this.usuarioRepository = usuarioRepository;
        this.clienteMapper = clienteMapper;
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarDirecciones.
     * @param emailAutenticado parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public List<DireccionResponse> listarDirecciones(String emailAutenticado) {
        Long idCliente = obtenerIdClientePorEmail(emailAutenticado);
        return clienteDireccionRepository.findByIdClienteAndActivoTrueOrderByEsPrincipalDescIdDireccionDesc(idCliente)
            .stream()
            .map(clienteMapper::toDireccionResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de obtenerDireccionPrincipal.
     * @param emailAutenticado parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public DireccionResponse obtenerDireccionPrincipal(String emailAutenticado) {
        Long idCliente = obtenerIdClientePorEmail(emailAutenticado);
        ClienteDireccionEntity principal = clienteDireccionRepository
            .findFirstByIdClienteAndActivoTrueAndEsPrincipalTrue(idCliente)
            .orElseThrow(() -> new NegocioException("el cliente no tiene direccion principal registrada"));
        return clienteMapper.toDireccionResponse(principal);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de crearDireccion.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public DireccionResponse crearDireccion(String emailAutenticado, DireccionRequest request) {
        Long idCliente = obtenerIdClientePorEmail(emailAutenticado);
        LocalDateTime ahora = LocalDateTime.now();

        ClienteDireccionEntity entity = new ClienteDireccionEntity();
        entity.setIdCliente(idCliente);
        aplicarCampos(entity, request);
        entity.setActivo(true);
        entity.setFechaCreacion(ahora);
        entity.setFechaActualizacion(ahora);

        boolean tieneDireccionesActivas = !clienteDireccionRepository.findByIdClienteAndActivoTrue(idCliente).isEmpty();
        entity.setEsPrincipal(!tieneDireccionesActivas);

        ClienteDireccionEntity guardado = clienteDireccionRepository.save(entity);
        asegurarDireccionPrincipal(idCliente);
        return clienteMapper.toDireccionResponse(guardado);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de actualizarDireccion.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param idDireccion parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public DireccionResponse actualizarDireccion(String emailAutenticado, Long idDireccion, DireccionRequest request) {
        Long idCliente = obtenerIdClientePorEmail(emailAutenticado);

        ClienteDireccionEntity entity = clienteDireccionRepository.findByIdDireccionAndIdClienteAndActivoTrue(idDireccion, idCliente)
            .orElseThrow(() -> new NegocioException("direccion no existe para el cliente"));

        aplicarCampos(entity, request);
        entity.setFechaActualizacion(LocalDateTime.now());

        ClienteDireccionEntity guardado = clienteDireccionRepository.save(entity);
        asegurarDireccionPrincipal(idCliente);
        return clienteMapper.toDireccionResponse(guardado);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de eliminarDireccion.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param idDireccion parametro de entrada para la operacion
     */
    public void eliminarDireccion(String emailAutenticado, Long idDireccion) {
        Long idCliente = obtenerIdClientePorEmail(emailAutenticado);

        ClienteDireccionEntity entity = clienteDireccionRepository.findByIdDireccionAndIdClienteAndActivoTrue(idDireccion, idCliente)
            .orElseThrow(() -> new NegocioException("direccion no existe para el cliente"));

        entity.setActivo(false);
        entity.setEsPrincipal(false);
        entity.setFechaActualizacion(LocalDateTime.now());
        clienteDireccionRepository.save(entity);

        List<ClienteDireccionEntity> restantes = clienteDireccionRepository.findByIdClienteAndActivoTrue(idCliente);
        boolean existePrincipal = restantes.stream().anyMatch(d -> Boolean.TRUE.equals(d.getEsPrincipal()));
        if (!restantes.isEmpty() && !existePrincipal) {
            ClienteDireccionEntity primera = restantes.get(0);
            primera.setEsPrincipal(true);
            primera.setFechaActualizacion(LocalDateTime.now());
            clienteDireccionRepository.save(primera);
        }
        asegurarDireccionPrincipal(idCliente);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de marcarPrincipal.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param idDireccion parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public DireccionResponse marcarPrincipal(String emailAutenticado, Long idDireccion) {
        Long idCliente = obtenerIdClientePorEmail(emailAutenticado);

        ClienteDireccionEntity objetivo = clienteDireccionRepository.findByIdDireccionAndIdClienteAndActivoTrue(idDireccion, idCliente)
            .orElseThrow(() -> new NegocioException("direccion no existe para el cliente"));

        LocalDateTime ahora = LocalDateTime.now();
        clienteDireccionRepository.desmarcarDireccionesPrincipales(idCliente, ahora);
        objetivo.setEsPrincipal(true);
        objetivo.setFechaActualizacion(ahora);
        clienteDireccionRepository.save(objetivo);
        asegurarDireccionPrincipal(idCliente);
        return clienteMapper.toDireccionResponse(objetivo);
    }

    private void aplicarCampos(ClienteDireccionEntity entity, DireccionRequest request) {
        validarCoordenadas(request.latitud(), request.longitud());
        validarPlaceId(request.googlePlaceId(), request.latitud(), request.longitud());

        entity.setDireccionLinea1(request.direccionLinea1().trim());
        entity.setReferencia(request.referencia() == null ? null : request.referencia().trim());
        entity.setDistrito(request.distrito() == null ? null : request.distrito().trim());
        entity.setCiudad(request.ciudad() == null ? null : request.ciudad().trim());
        entity.setLatitud(request.latitud());
        entity.setLongitud(request.longitud());
        entity.setGooglePlaceId(request.googlePlaceId() == null ? null : request.googlePlaceId().trim());
        entity.setGooglePlusCode(request.googlePlusCode() == null ? null : request.googlePlusCode().trim());
    }

    private void validarCoordenadas(BigDecimal latitud, BigDecimal longitud) {
        if ((latitud == null && longitud != null) || (latitud != null && longitud == null)) {
            throw new NegocioException("si registra ubicacion debe enviar latitud y longitud");
        }
        if (latitud == null) {
            return;
        }
        if (latitud.compareTo(BigDecimal.valueOf(-90)) < 0 || latitud.compareTo(BigDecimal.valueOf(90)) > 0) {
            throw new NegocioException("latitud fuera de rango");
        }
        if (longitud.compareTo(BigDecimal.valueOf(-180)) < 0 || longitud.compareTo(BigDecimal.valueOf(180)) > 0) {
            throw new NegocioException("longitud fuera de rango");
        }
    }

    private void validarPlaceId(String googlePlaceId, BigDecimal latitud, BigDecimal longitud) {
        if (googlePlaceId != null && !googlePlaceId.isBlank() && (latitud == null || longitud == null)) {
            throw new NegocioException("googlePlaceId requiere coordenadas");
        }
    }

    private Long obtenerIdClientePorEmail(String emailAutenticado) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(emailAutenticado)
            .orElseThrow(() -> new NegocioException("usuario autenticado no encontrado"));
        return usuario.getIdUsuario();
    }

    private void asegurarDireccionPrincipal(Long idCliente) {
        List<ClienteDireccionEntity> activas = clienteDireccionRepository.findByIdClienteAndActivoTrue(idCliente);
        if (activas.isEmpty()) {
            return;
        }
        long principales = activas.stream().filter(d -> Boolean.TRUE.equals(d.getEsPrincipal())).count();
        if (principales == 1) {
            return;
        }
        LocalDateTime ahora = LocalDateTime.now();
        clienteDireccionRepository.desmarcarDireccionesPrincipales(idCliente, ahora);
        ClienteDireccionEntity masReciente = activas.stream()
            .max((a, b) -> a.getIdDireccion().compareTo(b.getIdDireccion()))
            .orElse(activas.get(0));
        masReciente.setEsPrincipal(true);
        masReciente.setFechaActualizacion(ahora);
        clienteDireccionRepository.save(masReciente);
    }
}
