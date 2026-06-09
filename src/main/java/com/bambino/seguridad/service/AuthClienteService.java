package com.bambino.seguridad.service;

import com.bambino.auditoria.entity.AuditoriaActorTipo;
import com.bambino.auditoria.entity.AuditoriaCanal;
import com.bambino.auditoria.service.AuditoriaService;
import com.bambino.clientes.entity.ClientePerfilEntity;
import com.bambino.clientes.entity.ClienteDocumentoEntity;
import com.bambino.clientes.entity.DocTipo;
import com.bambino.clientes.repository.ClienteDocumentoRepository;
import com.bambino.clientes.repository.ClientePerfilRepository;
import com.bambino.documentos.dto.ConsultaDocumentoResponse;
import com.bambino.documentos.service.DocumentoConsultaService;
import com.bambino.seguridad.dto.*;
import com.bambino.seguridad.entity.*;
import com.bambino.seguridad.repository.RecuperacionPasswordCodigoRepository;
import com.bambino.seguridad.repository.RolRepository;
import com.bambino.seguridad.repository.UsuarioRepository;
import com.bambino.shared.exception.NegocioException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

@Service
/**
 * Clase que maneja la funcionalidad de AuthClienteService.
 */
public class AuthClienteService {

    private static final int MAX_INTENTOS_CODIGO = 5;
    private static final String ROL_CLIENTE = "CLIENTE";
    private static final Set<String> DOMINIOS_EMAIL_PERMITIDOS = Set.of("gmail.com", "hotmail.com", "outlook.com");

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final ClientePerfilRepository clientePerfilRepository;
    private final ClienteDocumentoRepository clienteDocumentoRepository;
    private final RecuperacionPasswordCodigoRepository recuperacionRepository;
    private final PasswordEncoder passwordEncoder;
    private final CorreoRecuperacionService correoRecuperacionService;
    private final AuditoriaService auditoriaService;
    private final DocumentoConsultaService documentoConsultaService;

    public AuthClienteService(UsuarioRepository usuarioRepository,
                              RolRepository rolRepository,
                              ClientePerfilRepository clientePerfilRepository,
                              ClienteDocumentoRepository clienteDocumentoRepository,
                              RecuperacionPasswordCodigoRepository recuperacionRepository,
                              PasswordEncoder passwordEncoder,
                              CorreoRecuperacionService correoRecuperacionService,
                              AuditoriaService auditoriaService,
                              DocumentoConsultaService documentoConsultaService) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.clientePerfilRepository = clientePerfilRepository;
        this.clienteDocumentoRepository = clienteDocumentoRepository;
        this.recuperacionRepository = recuperacionRepository;
        this.passwordEncoder = passwordEncoder;
        this.correoRecuperacionService = correoRecuperacionService;
        this.auditoriaService = auditoriaService;
        this.documentoConsultaService = documentoConsultaService;
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de registrarCliente.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public RegistroClienteResponse registrarCliente(RegistroClienteRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        if (!esDominioEmailPermitido(email)) {
            throw new NegocioException("dominio de correo no permitido");
        }
        if (usuarioRepository.existsByEmail(email)) {
            throw new NegocioException("el email ya existe");
        }

        RolEntity rolCliente = rolRepository.findByNombre("CLIENTE")
            .orElseThrow(() -> new NegocioException("rol cliente no configurado"));

        List<RegistroClienteDocumentoRequest> documentosRequest = request.documentos() == null
            ? List.of()
            : request.documentos();

        List<RegistroClienteDocumentoRequest> documentosNormalizados = new ArrayList<>();
        if (documentosRequest.isEmpty()) {
            documentosNormalizados.add(new RegistroClienteDocumentoRequest(request.docTipo(), request.docNumero()));
        } else {
            documentosNormalizados.addAll(documentosRequest);
        }

        List<ClienteDocumentoEntity> documentos = construirDocumentosValidados(documentosNormalizados);
        ClienteDocumentoEntity documentoPrincipal = documentos.getFirst();
        DocTipo docTipo = documentoPrincipal.getDocTipo();
        String docNumeroPrincipal = documentoPrincipal.getDocNumero();

        LocalDateTime ahora = LocalDateTime.now();

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setUuidUsuario(UUID.randomUUID().toString());
        usuario.setEmail(email);
        usuario.setPasswordHash(passwordEncoder.encode(request.password()));
        usuario.setNombres(request.nombres().trim());
        usuario.setApellidos(request.apellidos().trim());
        usuario.setTelefono(request.telefono() == null ? null : request.telefono().trim());
        usuario.setDocumento(docNumeroPrincipal);
        usuario.setEstado(EstadoUsuario.ACTIVO);
        usuario.setRol(rolCliente);
        usuario.setFechaCreacion(ahora);
        usuario.setFechaActualizacion(ahora);

        UsuarioEntity guardado = usuarioRepository.save(usuario);

        ClientePerfilEntity perfil = new ClientePerfilEntity();
        perfil.setIdCliente(guardado.getIdUsuario());
        perfil.setDocTipo(docTipo);
        perfil.setDocNumero(docNumeroPrincipal);
        perfil.setFechaCreacion(ahora);
        perfil.setFechaActualizacion(ahora);
        clientePerfilRepository.save(perfil);

        for (int i = 0; i < documentos.size(); i++) {
            ClienteDocumentoEntity documento = documentos.get(i);
            documento.setIdCliente(guardado.getIdUsuario());
            documento.setEsPrincipal(i == 0);
            documento.setActivo(true);
            documento.setFechaCreacion(ahora);
            documento.setFechaActualizacion(ahora);
            clienteDocumentoRepository.save(documento);
        }
        auditoriaService.registrar(
            "USUARIO",
            String.valueOf(guardado.getIdUsuario()),
            "REGISTRO_CLIENTE",
            AuditoriaActorTipo.CLIENTE,
            guardado.getIdUsuario(),
            AuditoriaCanal.WEB,
            "{\"email\":\"" + guardado.getEmail() + "\"}"
        );

        return new RegistroClienteResponse(guardado.getIdUsuario(), guardado.getEmail(), "registro exitoso");
    }

    private List<ClienteDocumentoEntity> construirDocumentosValidados(List<RegistroClienteDocumentoRequest> docsRequest) {
        if (docsRequest.isEmpty()) {
            throw new NegocioException("debe registrar al menos un documento");
        }

        Set<DocTipo> tiposUsados = new HashSet<>();
        List<ClienteDocumentoEntity> docs = new ArrayList<>();

        for (RegistroClienteDocumentoRequest raw : docsRequest) {
            DocTipo tipo = parseDocTipo(raw.docTipo());
            String numero = raw.docNumero() == null ? "" : raw.docNumero().trim().toUpperCase(Locale.ROOT);
            validarNumeroDocumento(tipo, numero);
            validarDocumentoConFactiliza(tipo, numero);
            if (usuarioRepository.existsByDocumento(numero)) {
                throw new NegocioException("docNumero ya existe");
            }
            if (!tiposUsados.add(tipo)) {
                throw new NegocioException("no se permite repetir tipo de documento");
            }

            ClienteDocumentoEntity doc = new ClienteDocumentoEntity();
            doc.setDocTipo(tipo);
            doc.setDocNumero(numero);
            docs.add(doc);
        }
        return docs;
    }

    private DocTipo parseDocTipo(String raw) {
        try {
            DocTipo parsed = DocTipo.valueOf(raw.trim().toUpperCase(Locale.ROOT));
            if (parsed == DocTipo.OTRO) {
                throw new NegocioException("docTipo invalido");
            }
            return parsed;
        } catch (IllegalArgumentException e) {
            throw new NegocioException("docTipo invalido");
        }
    }

    private void validarNumeroDocumento(DocTipo tipo, String numero) {
        if (numero.isBlank()) {
            throw new NegocioException("docNumero es obligatorio");
        }
        if (tipo == DocTipo.DNI && !numero.matches("^\\d{8}$")) {
            throw new NegocioException("docNumero: para DNI debe tener 8 digitos");
        }
        if (tipo == DocTipo.RUC && !numero.matches("^\\d{11}$")) {
            throw new NegocioException("docNumero: para RUC debe tener 11 digitos");
        }
        if (tipo == DocTipo.CE && !numero.matches("^[A-Z0-9]{9,12}$")) {
            throw new NegocioException("docNumero: para CE debe tener entre 9 y 12 caracteres");
        }
    }

    private void validarDocumentoConFactiliza(DocTipo tipo, String numero) {
        if (tipo == DocTipo.CE) {
            return;
        }
        ConsultaDocumentoResponse response = documentoConsultaService.consultar(numero);
        if (!tipo.name().equalsIgnoreCase(response.tipoDocumento()) || !numero.equals(response.numeroDocumento())) {
            throw new NegocioException("documento no coincide con la validacion externa");
        }
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de solicitarRecuperacion.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public String solicitarRecuperacion(RecuperarSolicitarRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);

        UsuarioEntity usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (usuario != null && !esClienteActivo(usuario)) {
            throw new NegocioException("recuperacion disponible solo para clientes");
        }

        if (usuario != null) {
            LocalDateTime ahora = LocalDateTime.now();

            List<RecuperacionPasswordCodigoEntity> pendientes = recuperacionRepository
                .findByCorreoAndEstado(email, EstadoRecuperacionCodigo.PENDIENTE);

            for (RecuperacionPasswordCodigoEntity pendiente : pendientes) {
                pendiente.setEstado(EstadoRecuperacionCodigo.EXPIRADO);
                pendiente.setFechaActualizacion(ahora);
            }
            recuperacionRepository.saveAll(pendientes);

            String codigo = generarCodigoSeisDigitosUnico(email);

            RecuperacionPasswordCodigoEntity nuevo = new RecuperacionPasswordCodigoEntity();
            nuevo.setIdUsuario(usuario.getIdUsuario());
            nuevo.setCorreo(email);
            // Compatibilidad temporal: se evita guardar el codigo plano para nuevos registros.
            nuevo.setCodigo(null);
            nuevo.setCodigoHash(passwordEncoder.encode(codigo));
            nuevo.setEstado(EstadoRecuperacionCodigo.PENDIENTE);
            nuevo.setFechaExpiracion(ahora.plusMinutes(15));
            nuevo.setIntentosFallidos(0);
            nuevo.setFechaCreacion(ahora);
            nuevo.setFechaActualizacion(ahora);
            recuperacionRepository.save(nuevo);

            correoRecuperacionService.enviarCodigoRecuperacion(email, codigo);
            auditoriaService.registrar(
                "SEGURIDAD",
                String.valueOf(usuario.getIdUsuario()),
                "SOLICITAR_RECUPERACION_PASSWORD",
                AuditoriaActorTipo.CLIENTE,
                usuario.getIdUsuario(),
                AuditoriaCanal.WEB,
                "{\"email\":\"" + email + "\"}"
            );
        }

        return "si el correo existe, se envio un codigo de recuperacion";
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de confirmarRecuperacion.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public String confirmarRecuperacion(RecuperarConfirmarRequest request) {
        RecuperacionPasswordCodigoEntity recuperacion = validarCodigoRecuperacion(request.email(), request.codigo());
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        LocalDateTime ahora = LocalDateTime.now();

        UsuarioEntity usuario = usuarioRepository.findById(recuperacion.getIdUsuario())
            .orElseThrow(() -> new NegocioException("usuario no encontrado"));

        if (!esClienteActivo(usuario)) {
            recuperacion.setEstado(EstadoRecuperacionCodigo.EXPIRADO);
            recuperacion.setFechaActualizacion(ahora);
            recuperacionRepository.save(recuperacion);
            throw new NegocioException("codigo invalido o expirado");
        }

        usuario.setPasswordHash(passwordEncoder.encode(request.nuevaPassword()));
        usuario.setFechaActualizacion(ahora);
        usuarioRepository.save(usuario);

        recuperacion.setEstado(EstadoRecuperacionCodigo.USADO);
        recuperacion.setFechaActualizacion(ahora);
        recuperacionRepository.save(recuperacion);
        auditoriaService.registrar(
            "SEGURIDAD",
            String.valueOf(usuario.getIdUsuario()),
            "CONFIRMAR_RECUPERACION_PASSWORD",
            AuditoriaActorTipo.CLIENTE,
            usuario.getIdUsuario(),
            AuditoriaCanal.WEB,
            "{\"email\":\"" + email + "\"}"
        );

        return "contrasena actualizada";
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de validarCodigoRecuperacion.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public String validarCodigoRecuperacion(RecuperarValidarCodigoRequest request) {
        validarCodigoRecuperacion(request.email(), request.codigo());
        return "codigo valido";
    }

    private RecuperacionPasswordCodigoEntity validarCodigoRecuperacion(String emailRaw, String codigoRaw) {
        String email = emailRaw.trim().toLowerCase(Locale.ROOT);

        List<RecuperacionPasswordCodigoEntity> pendientes = recuperacionRepository
            .findByCorreoAndEstado(email, EstadoRecuperacionCodigo.PENDIENTE);
        if (pendientes.isEmpty()) {
            throw new NegocioException("codigo invalido o expirado");
        }

        pendientes.sort(Comparator.comparing(RecuperacionPasswordCodigoEntity::getIdCodigo).reversed());
        RecuperacionPasswordCodigoEntity recuperacion = pendientes.getFirst();

        LocalDateTime ahora = LocalDateTime.now();

        if (pendientes.size() > 1) {
            for (int i = 1; i < pendientes.size(); i++) {
                RecuperacionPasswordCodigoEntity anterior = pendientes.get(i);
                anterior.setEstado(EstadoRecuperacionCodigo.EXPIRADO);
                anterior.setFechaActualizacion(ahora);
            }
            recuperacionRepository.saveAll(pendientes.subList(1, pendientes.size()));
        }

        if (recuperacion.getFechaExpiracion().isBefore(ahora)) {
            recuperacion.setEstado(EstadoRecuperacionCodigo.EXPIRADO);
            recuperacion.setFechaActualizacion(ahora);
            recuperacionRepository.save(recuperacion);
            throw new NegocioException("codigo expirado");
        }

        if (recuperacion.getIntentosFallidos() >= MAX_INTENTOS_CODIGO) {
            recuperacion.setEstado(EstadoRecuperacionCodigo.EXPIRADO);
            recuperacion.setFechaActualizacion(ahora);
            recuperacionRepository.save(recuperacion);
            throw new NegocioException("codigo bloqueado por intentos");
        }

        if (recuperacion.getCodigoHash() == null || recuperacion.getCodigoHash().isBlank()) {
            recuperacion.setEstado(EstadoRecuperacionCodigo.EXPIRADO);
            recuperacion.setFechaActualizacion(ahora);
            recuperacionRepository.save(recuperacion);
            throw new NegocioException("codigo invalido o expirado");
        }

        String codigoIngresado = codigoRaw.trim();
        boolean codigoValido = passwordEncoder.matches(codigoIngresado, recuperacion.getCodigoHash());

        if (!codigoValido) {
            recuperacion.setIntentosFallidos(recuperacion.getIntentosFallidos() + 1);
            recuperacion.setFechaActualizacion(ahora);
            recuperacionRepository.save(recuperacion);
            throw new NegocioException("codigo incorrecto");
        }

        return recuperacion;
    }

    private String generarCodigoSeisDigitos() {
        int numero = 100000 + new Random().nextInt(900000);
        return String.valueOf(numero);
    }

    private String generarCodigoSeisDigitosUnico(String email) {
        List<RecuperacionPasswordCodigoEntity> recientes = recuperacionRepository
            .findTop5ByCorreoOrderByIdCodigoDesc(email);

        for (int intento = 0; intento < 10; intento++) {
            String candidato = generarCodigoSeisDigitos();
            boolean repetido = recientes.stream().anyMatch(rec -> {
                String hash = rec.getCodigoHash();
                if (hash != null && !hash.isBlank() && passwordEncoder.matches(candidato, hash)) {
                    return true;
                }
                String legacyCodigo = rec.getCodigo();
                return legacyCodigo != null && legacyCodigo.equals(candidato);
            });
            if (!repetido) {
                return candidato;
            }
        }
        throw new NegocioException("no se pudo generar un codigo valido");
    }

    private boolean esClienteActivo(UsuarioEntity usuario) {
        if (usuario == null || usuario.getRol() == null || usuario.getRol().getNombre() == null) {
            return false;
        }
        return ROL_CLIENTE.equalsIgnoreCase(usuario.getRol().getNombre())
            && EstadoUsuario.ACTIVO.equals(usuario.getEstado());
    }

    private boolean esDominioEmailPermitido(String email) {
        int atIndex = email.lastIndexOf('@');
        if (atIndex < 0 || atIndex == email.length() - 1) {
            return false;
        }
        String dominio = email.substring(atIndex + 1).toLowerCase(Locale.ROOT);
        return DOMINIOS_EMAIL_PERMITIDOS.contains(dominio);
    }
}
