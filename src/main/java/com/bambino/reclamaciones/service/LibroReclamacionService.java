package com.bambino.reclamaciones.service;

import com.bambino.clientes.entity.ClienteDocumentoEntity;
import com.bambino.clientes.repository.ClienteDocumentoRepository;
import com.bambino.comprobantes.entity.EmpresaEntity;
import com.bambino.comprobantes.repository.EmpresaRepository;
import com.bambino.pedidos.entity.PedidoEntity;
import com.bambino.pedidos.repository.PedidoRepository;
import com.bambino.reclamaciones.dto.LibroReclamacionCreateRequest;
import com.bambino.reclamaciones.dto.LibroReclamacionResponse;
import com.bambino.reclamaciones.entity.LibroReclamacionEntity;
import com.bambino.reclamaciones.repository.LibroReclamacionRepository;
import com.bambino.seguridad.entity.UsuarioEntity;
import com.bambino.seguridad.repository.UsuarioRepository;
import com.bambino.shared.exception.NegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
/**
 * Clase que maneja la funcionalidad de LibroReclamacionService.
 */
public class LibroReclamacionService {

    private final LibroReclamacionRepository libroReclamacionRepository;
    private final EmpresaRepository empresaRepository;
    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteDocumentoRepository clienteDocumentoRepository;

    public LibroReclamacionService(LibroReclamacionRepository libroReclamacionRepository,
                                   EmpresaRepository empresaRepository,
                                   PedidoRepository pedidoRepository,
                                   UsuarioRepository usuarioRepository,
                                   ClienteDocumentoRepository clienteDocumentoRepository) {
        this.libroReclamacionRepository = libroReclamacionRepository;
        this.empresaRepository = empresaRepository;
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.clienteDocumentoRepository = clienteDocumentoRepository;
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de crearPublico.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public LibroReclamacionResponse crearPublico(LibroReclamacionCreateRequest request) {
        if (request.idPedido() != null || hasText(request.codigoPedido())) {
            throw new NegocioException("para asociar pedido debe iniciar sesion");
        }
        return toResponse(guardar(request, null));
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de crearCliente.
     * @param emailAutenticado parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public LibroReclamacionResponse crearCliente(String emailAutenticado, LibroReclamacionCreateRequest request) {
        Long idCliente = obtenerIdCliente(emailAutenticado);
        return toResponse(guardar(request, idCliente));
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarMisReclamos.
     * @param emailAutenticado parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public List<LibroReclamacionResponse> listarMisReclamos(String emailAutenticado) {
        Long idCliente = obtenerIdCliente(emailAutenticado);
        return libroReclamacionRepository.findByIdClienteOrderByFechaRegistroDesc(idCliente).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de prefillCliente.
     * @param emailAutenticado parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public LibroReclamacionCreateRequest prefillCliente(String emailAutenticado) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(emailAutenticado)
            .orElseThrow(() -> new NegocioException("usuario autenticado no encontrado"));

        ClienteDocumentoEntity docPrincipal = clienteDocumentoRepository
            .findFirstByIdClienteAndEsPrincipalTrueAndActivoTrue(usuario.getIdUsuario())
            .orElse(null);

        return new LibroReclamacionCreateRequest(
            "RECLAMO",
            nvl(usuario.getNombres()),
            nvl(usuario.getApellidos()),
            docPrincipal != null ? docPrincipal.getDocTipo().name() : "DNI",
            docPrincipal != null ? nvl(docPrincipal.getDocNumero()) : "",
            nvl(usuario.getEmail()),
            nvl(usuario.getTelefono()),
            "",
            null,
            "",
            null,
            null,
            "",
            ""
        );
    }

    private LibroReclamacionEntity guardar(LibroReclamacionCreateRequest request, Long idCliente) {
        EmpresaEntity empresa = obtenerEmpresaPrincipal();

        PedidoEntity pedido = null;
        if (request.idPedido() != null || hasText(request.codigoPedido())) {
            pedido = resolverPedido(request.idPedido(), request.codigoPedido());
            if (idCliente != null && !pedido.getIdCliente().equals(idCliente)) {
                throw new NegocioException("no puede asociar un pedido que no le pertenece");
            }
        }

        LocalDateTime ahora = LocalDateTime.now();
        LibroReclamacionEntity e = new LibroReclamacionEntity();
        e.setNumeroReclamo(generarNumeroReclamo(ahora));
        e.setIdEmpresa(empresa.getIdEmpresa());
        e.setIdCliente(idCliente);
        e.setIdPedido(pedido != null ? pedido.getIdPedido() : null);
        e.setCodigoPedido(pedido != null ? pedido.getCodigoPedido() : normalizeNullable(request.codigoPedido()));
        e.setTipoRegistro(norm(request.tipoRegistro()));
        e.setConsumidorNombres(title(request.nombres()));
        e.setConsumidorApellidos(title(request.apellidos()));
        e.setDocTipo(norm(request.docTipo()));
        e.setDocNumero(norm(request.docNumero()));
        e.setCorreo(lower(request.correo()));
        e.setTelefono(normalizeNullable(request.telefono()));
        e.setDireccionConsumidor(normalizeNullable(request.direccionConsumidor()));
        e.setFechaConsumo(request.fechaConsumo());
        e.setMontoReclamado(request.montoReclamado());
        e.setDetalleHechos(normText(request.detalleHechos()));
        e.setPedidoConsumidor(normText(request.pedidoConsumidor()));
        e.setEstado("REGISTRADO");
        e.setFechaRegistro(ahora);
        e.setFechaCreacion(ahora);
        e.setFechaActualizacion(ahora);

        return libroReclamacionRepository.save(e);
    }

    private EmpresaEntity obtenerEmpresaPrincipal() {
        List<EmpresaEntity> empresas = empresaRepository.findAll();
        return empresas.stream()
            .filter(e -> Boolean.TRUE.equals(e.getActivo()))
            .findFirst()
            .or(() -> empresas.stream().findFirst())
            .orElseThrow(() -> new NegocioException("no hay empresa configurada"));
    }

    private PedidoEntity resolverPedido(Long idPedido, String codigoPedido) {
        if (idPedido != null) {
            return pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new NegocioException("pedido no encontrado"));
        }
        return pedidoRepository.findByCodigoPedido(norm(codigoPedido))
            .orElseThrow(() -> new NegocioException("pedido no encontrado"));
    }

    private String generarNumeroReclamo(LocalDateTime ahora) {
        long correlativo = libroReclamacionRepository.findTopByOrderByIdReclamoDesc()
            .map(x -> x.getIdReclamo() + 1)
            .orElse(1L);
        return "LR-" + ahora.getYear() + "-" + String.format("%06d", correlativo);
    }

    private Long obtenerIdCliente(String emailAutenticado) {
        return usuarioRepository.findByEmail(emailAutenticado)
            .map(UsuarioEntity::getIdUsuario)
            .orElseThrow(() -> new NegocioException("usuario autenticado no encontrado"));
    }

    private LibroReclamacionResponse toResponse(LibroReclamacionEntity e) {
        return new LibroReclamacionResponse(
            e.getIdReclamo(),
            e.getNumeroReclamo(),
            e.getEstado(),
            e.getFechaRegistro(),
            e.getIdEmpresa(),
            e.getIdCliente(),
            e.getIdPedido(),
            e.getCodigoPedido(),
            e.getTipoRegistro(),
            e.getConsumidorNombres(),
            e.getConsumidorApellidos(),
            e.getDocTipo(),
            e.getDocNumero(),
            e.getCorreo(),
            e.getTelefono(),
            e.getDireccionConsumidor(),
            e.getDetalleHechos(),
            e.getPedidoConsumidor()
        );
    }

    private String norm(String value) {
        return value == null ? "" : value.trim().toUpperCase(Locale.ROOT);
    }

    private String title(String value) {
        return value == null ? "" : value.trim();
    }

    private String lower(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    private String normText(String value) {
        return value == null ? "" : value.trim();
    }

    private String normalizeNullable(String value) {
        String v = value == null ? "" : value.trim();
        return v.isEmpty() ? null : v;
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private String nvl(String value) {
        return value == null ? "" : value;
    }
}
