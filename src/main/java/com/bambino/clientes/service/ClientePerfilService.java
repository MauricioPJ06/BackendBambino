package com.bambino.clientes.service;

import com.bambino.clientes.dto.ActualizarDocumentoRequest;
import com.bambino.clientes.dto.ActualizarDatosPersonalesRequest;
import com.bambino.clientes.dto.CambiarPasswordRequest;
import com.bambino.clientes.dto.ClienteDocumentoRequest;
import com.bambino.clientes.dto.ClienteDocumentoResponse;
import com.bambino.clientes.dto.ClientePerfilResponse;
import com.bambino.clientes.entity.ClienteDocumentoEntity;
import com.bambino.auditoria.entity.AuditoriaActorTipo;
import com.bambino.auditoria.entity.AuditoriaCanal;
import com.bambino.auditoria.service.AuditoriaService;
import com.bambino.clientes.entity.ClientePerfilEntity;
import com.bambino.clientes.entity.DocTipo;
import com.bambino.clientes.mapper.ClienteMapper;
import com.bambino.clientes.repository.ClienteDocumentoRepository;
import com.bambino.clientes.repository.ClientePerfilRepository;
import com.bambino.documentos.dto.ConsultaDocumentoResponse;
import com.bambino.documentos.service.DocumentoConsultaService;
import com.bambino.seguridad.entity.UsuarioEntity;
import com.bambino.seguridad.repository.UsuarioRepository;
import com.bambino.shared.exception.NegocioException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
/**
 * Clase que maneja la funcionalidad de ClientePerfilService.
 */
public class ClientePerfilService {

    private final ClientePerfilRepository clientePerfilRepository;
    private final ClienteDocumentoRepository clienteDocumentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteMapper clienteMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuditoriaService auditoriaService;
    private final DocumentoConsultaService documentoConsultaService;

    public ClientePerfilService(ClientePerfilRepository clientePerfilRepository,
                                ClienteDocumentoRepository clienteDocumentoRepository,
                                UsuarioRepository usuarioRepository,
                                ClienteMapper clienteMapper,
                                PasswordEncoder passwordEncoder,
                                AuditoriaService auditoriaService,
                                DocumentoConsultaService documentoConsultaService) {
        this.clientePerfilRepository = clientePerfilRepository;
        this.clienteDocumentoRepository = clienteDocumentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.clienteMapper = clienteMapper;
        this.passwordEncoder = passwordEncoder;
        this.auditoriaService = auditoriaService;
        this.documentoConsultaService = documentoConsultaService;
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de obtenerPerfil.
     * @param emailAutenticado parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ClientePerfilResponse obtenerPerfil(String emailAutenticado) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(emailAutenticado)
            .orElseThrow(() -> new NegocioException("usuario autenticado no encontrado"));
        Long idCliente = usuario.getIdUsuario();
        ClientePerfilEntity perfil = clientePerfilRepository.findById(idCliente)
            .orElseThrow(() -> new NegocioException("perfil de cliente no existe"));
        return clienteMapper.toPerfilResponse(
            perfil,
            usuario.getNombres(),
            usuario.getApellidos(),
            usuario.getEmail(),
            usuario.getTelefono()
        );
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de actualizarDocumento.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ClientePerfilResponse actualizarDocumento(String emailAutenticado, ActualizarDocumentoRequest request) {
        Long idCliente = obtenerIdClientePorEmail(emailAutenticado);
        ClientePerfilEntity perfilActual = clientePerfilRepository.findById(idCliente)
            .orElseThrow(() -> new NegocioException("perfil de cliente no existe"));
        DocTipo docTipo = parseDocTipo(request.docTipo());
        String docNumero = sanitizeDocNumero(request.docNumero());
        validarDocNumero(docTipo, docNumero);
        LocalDateTime ahora = LocalDateTime.now();

        boolean mismoTipoPerfilActual = perfilActual.getDocTipo() == docTipo;
        if (!mismoTipoPerfilActual && clienteDocumentoRepository.existsByIdClienteAndDocTipoAndActivoTrue(idCliente, docTipo)) {
            throw new NegocioException("ya existe un documento de tipo " + docTipo.name() + " para el cliente");
        }

        ClienteDocumentoEntity documento = clienteDocumentoRepository
            .findByIdClienteAndDocTipoAndDocNumeroAndActivoTrue(idCliente, docTipo, docNumero)
            .orElseGet(() -> {
                ClienteDocumentoEntity nuevo = new ClienteDocumentoEntity();
                nuevo.setIdCliente(idCliente);
                nuevo.setDocTipo(docTipo);
                nuevo.setDocNumero(docNumero);
                nuevo.setActivo(true);
                nuevo.setEsPrincipal(false);
                nuevo.setFechaCreacion(ahora);
                nuevo.setFechaActualizacion(ahora);
                return nuevo;
            });
        documento.setActivo(true);
        documento.setFechaActualizacion(ahora);
        ClienteDocumentoEntity guardadoDocumento = clienteDocumentoRepository.save(documento);

        marcarDocumentoComoPrincipalInterno(idCliente, guardadoDocumento.getIdDocumento(), ahora);
        ClientePerfilEntity guardado = sincronizarPerfilPrincipal(idCliente, docTipo, docNumero, ahora);
        UsuarioEntity usuario = usuarioRepository.findById(idCliente)
            .orElseThrow(() -> new NegocioException("usuario autenticado no encontrado"));
        return clienteMapper.toPerfilResponse(
            guardado,
            usuario.getNombres(),
            usuario.getApellidos(),
            usuario.getEmail(),
            usuario.getTelefono()
        );
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarDocumentos.
     * @param emailAutenticado parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public List<ClienteDocumentoResponse> listarDocumentos(String emailAutenticado) {
        Long idCliente = obtenerIdClientePorEmail(emailAutenticado);
        return clienteDocumentoRepository
            .findByIdClienteAndActivoTrueOrderByEsPrincipalDescFechaActualizacionDesc(idCliente)
            .stream()
            .map(this::toDocumentoResponse)
            .toList();
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de agregarDocumento.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ClienteDocumentoResponse agregarDocumento(String emailAutenticado, ClienteDocumentoRequest request) {
        Long idCliente = obtenerIdClientePorEmail(emailAutenticado);
        DocTipo docTipo = parseDocTipo(request.docTipo());
        String docNumero = sanitizeDocNumero(request.docNumero());
        validarDocNumero(docTipo, docNumero);
        validarDocumentoConFactiliza(docTipo, docNumero);

        if (clienteDocumentoRepository.existsByIdClienteAndDocTipoAndActivoTrue(idCliente, docTipo)) {
            throw new NegocioException("ya existe un documento de tipo " + docTipo.name() + " para el cliente");
        }

        if (clienteDocumentoRepository.existsByIdClienteAndDocTipoAndDocNumeroAndActivoTrue(idCliente, docTipo, docNumero)) {
            throw new NegocioException("el documento ya existe para el cliente");
        }

        LocalDateTime ahora = LocalDateTime.now();
        boolean tienePrincipal = clienteDocumentoRepository.findFirstByIdClienteAndEsPrincipalTrueAndActivoTrue(idCliente).isPresent();

        ClienteDocumentoEntity documento = new ClienteDocumentoEntity();
        documento.setIdCliente(idCliente);
        documento.setDocTipo(docTipo);
        documento.setDocNumero(docNumero);
        documento.setEsPrincipal(!tienePrincipal);
        documento.setActivo(true);
        documento.setFechaCreacion(ahora);
        documento.setFechaActualizacion(ahora);

        ClienteDocumentoEntity guardado = clienteDocumentoRepository.save(documento);
        if (Boolean.TRUE.equals(guardado.getEsPrincipal())) {
            sincronizarPerfilPrincipal(idCliente, guardado.getDocTipo(), guardado.getDocNumero(), ahora);
        }
        return toDocumentoResponse(guardado);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de eliminarDocumento.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param idDocumento parametro de entrada para la operacion
     */
    public void eliminarDocumento(String emailAutenticado, Long idDocumento) {
        Long idCliente = obtenerIdClientePorEmail(emailAutenticado);
        ClienteDocumentoEntity documento = clienteDocumentoRepository
            .findByIdDocumentoAndIdClienteAndActivoTrue(idDocumento, idCliente)
            .orElseThrow(() -> new NegocioException("documento no encontrado"));

        List<ClienteDocumentoEntity> activos = clienteDocumentoRepository
            .findByIdClienteAndActivoTrueOrderByEsPrincipalDescFechaActualizacionDesc(idCliente);
        if (activos.size() <= 1) {
            throw new NegocioException("debe existir al menos un documento activo");
        }

        LocalDateTime ahora = LocalDateTime.now();
        boolean eraPrincipal = Boolean.TRUE.equals(documento.getEsPrincipal());
        documento.setActivo(false);
        documento.setEsPrincipal(false);
        documento.setFechaActualizacion(ahora);
        clienteDocumentoRepository.save(documento);

        if (eraPrincipal) {
            ClienteDocumentoEntity reemplazo = clienteDocumentoRepository
                .findByIdClienteAndActivoTrueOrderByEsPrincipalDescFechaActualizacionDesc(idCliente)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NegocioException("no existe documento de reemplazo"));
            marcarDocumentoComoPrincipalInterno(idCliente, reemplazo.getIdDocumento(), ahora);
            sincronizarPerfilPrincipal(idCliente, reemplazo.getDocTipo(), reemplazo.getDocNumero(), ahora);
        }
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de marcarDocumentoPrincipal.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param idDocumento parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ClienteDocumentoResponse marcarDocumentoPrincipal(String emailAutenticado, Long idDocumento) {
        Long idCliente = obtenerIdClientePorEmail(emailAutenticado);
        ClienteDocumentoEntity documento = clienteDocumentoRepository
            .findByIdDocumentoAndIdClienteAndActivoTrue(idDocumento, idCliente)
            .orElseThrow(() -> new NegocioException("documento no encontrado"));
        LocalDateTime ahora = LocalDateTime.now();
        marcarDocumentoComoPrincipalInterno(idCliente, idDocumento, ahora);
        sincronizarPerfilPrincipal(idCliente, documento.getDocTipo(), documento.getDocNumero(), ahora);
        documento.setEsPrincipal(true);
        documento.setFechaActualizacion(ahora);
        return toDocumentoResponse(documento);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de actualizarDatosPersonales.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     */
    public void actualizarDatosPersonales(String emailAutenticado, ActualizarDatosPersonalesRequest request) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(emailAutenticado)
            .orElseThrow(() -> new NegocioException("usuario autenticado no encontrado"));

        String nombres = request.nombres().trim();
        String apellidos = request.apellidos().trim();
        String telefono = request.telefono() == null ? null : request.telefono().trim();
        usuario.setNombres(nombres);
        usuario.setApellidos(apellidos);
        usuario.setTelefono(telefono == null || telefono.isBlank() ? null : telefono);
        usuario.setFechaActualizacion(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de cambiarPassword.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public String cambiarPassword(String emailAutenticado, CambiarPasswordRequest request) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(emailAutenticado)
            .orElseThrow(() -> new NegocioException("usuario autenticado no encontrado"));

        String passwordActual = request.passwordActual().trim();
        String passwordNueva = request.passwordNueva().trim();
        String passwordConfirmacion = request.passwordConfirmacion().trim();

        if (!passwordEncoder.matches(passwordActual, usuario.getPasswordHash())) {
            throw new NegocioException("passwordActual incorrecta");
        }
        if (!passwordNueva.equals(passwordConfirmacion)) {
            throw new NegocioException("passwordConfirmacion no coincide");
        }
        if (passwordActual.equals(passwordNueva)) {
            throw new NegocioException("passwordNueva debe ser diferente a la actual");
        }
        if (passwordEncoder.matches(passwordNueva, usuario.getPasswordHash())) {
            throw new NegocioException("passwordNueva debe ser diferente a la actual");
        }

        usuario.setPasswordHash(passwordEncoder.encode(passwordNueva));
        usuario.setFechaActualizacion(LocalDateTime.now());
        usuarioRepository.save(usuario);

        auditoriaService.registrar(
            "SEGURIDAD",
            String.valueOf(usuario.getIdUsuario()),
            "CAMBIAR_PASSWORD_CLIENTE",
            AuditoriaActorTipo.CLIENTE,
            usuario.getIdUsuario(),
            AuditoriaCanal.WEB,
            "{\"email\":\"" + usuario.getEmail() + "\"}"
        );
        return "contrasena actualizada";
    }

    private Long obtenerIdClientePorEmail(String emailAutenticado) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(emailAutenticado)
            .orElseThrow(() -> new NegocioException("usuario autenticado no encontrado"));

        return usuario.getIdUsuario();
    }

    private DocTipo parseDocTipo(String rawDocTipo) {
        DocTipo docTipo;
        try {
            docTipo = DocTipo.valueOf(rawDocTipo.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new NegocioException("docTipo invalido");
        }
        if (docTipo == DocTipo.OTRO) {
            throw new NegocioException("docTipo invalido");
        }
        return docTipo;
    }

    private String sanitizeDocNumero(String rawDocNumero) {
        return rawDocNumero == null ? "" : rawDocNumero.trim().toUpperCase(Locale.ROOT);
    }

    private void validarDocNumero(DocTipo docTipo, String docNumero) {
        if (docNumero.isBlank()) {
            throw new NegocioException("docNumero es obligatorio");
        }
        if (docTipo == DocTipo.DNI && !docNumero.matches("^\\d{8}$")) {
            throw new NegocioException("docNumero: para DNI debe tener 8 digitos");
        }
        if (docTipo == DocTipo.RUC && !docNumero.matches("^\\d{11}$")) {
            throw new NegocioException("docNumero: para RUC debe tener 11 digitos");
        }
        if (docTipo == DocTipo.CE && !docNumero.matches("^[A-Z0-9]{9,12}$")) {
            throw new NegocioException("docNumero: para CE debe tener entre 9 y 12 caracteres alfanumericos");
        }
    }

    private void validarDocumentoConFactiliza(DocTipo docTipo, String docNumero) {
        if (docTipo == DocTipo.CE) {
            return;
        }
        ConsultaDocumentoResponse response = documentoConsultaService.consultar(docNumero);
        if (!docTipo.name().equalsIgnoreCase(response.tipoDocumento()) || !docNumero.equals(response.numeroDocumento())) {
            throw new NegocioException("documento no coincide con la validacion externa");
        }
    }

    private void marcarDocumentoComoPrincipalInterno(Long idCliente, Long idDocumentoPrincipal, LocalDateTime ahora) {
        List<ClienteDocumentoEntity> documentos = clienteDocumentoRepository
            .findByIdClienteAndActivoTrueOrderByEsPrincipalDescFechaActualizacionDesc(idCliente);
        for (ClienteDocumentoEntity doc : documentos) {
            boolean principal = doc.getIdDocumento().equals(idDocumentoPrincipal);
            if (Boolean.TRUE.equals(doc.getEsPrincipal()) == principal) continue;
            doc.setEsPrincipal(principal);
            doc.setFechaActualizacion(ahora);
            clienteDocumentoRepository.save(doc);
        }
    }

    private ClientePerfilEntity sincronizarPerfilPrincipal(Long idCliente, DocTipo docTipo, String docNumero, LocalDateTime ahora) {
        ClientePerfilEntity perfil = clientePerfilRepository.findById(idCliente)
            .orElseThrow(() -> new NegocioException("perfil de cliente no existe"));
        perfil.setDocTipo(docTipo);
        perfil.setDocNumero(docNumero);
        perfil.setFechaActualizacion(ahora);
        return clientePerfilRepository.save(perfil);
    }

    private ClienteDocumentoResponse toDocumentoResponse(ClienteDocumentoEntity doc) {
        return new ClienteDocumentoResponse(
            doc.getIdDocumento(),
            doc.getDocTipo().name(),
            doc.getDocNumero(),
            doc.getEsPrincipal(),
            doc.getActivo(),
            doc.getFechaCreacion(),
            doc.getFechaActualizacion()
        );
    }
}
