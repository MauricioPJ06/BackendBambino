package com.bambino.catalogo.service;

import com.bambino.auditoria.entity.AuditoriaActorTipo;
import com.bambino.auditoria.entity.AuditoriaCanal;
import com.bambino.auditoria.service.AuditoriaService;
import com.bambino.catalogo.dto.*;
import com.bambino.catalogo.entity.*;
import com.bambino.catalogo.mapper.CatalogoMapper;
import com.bambino.catalogo.repository.*;
import com.bambino.shared.exception.NegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;

@Service
/**
 * Clase que maneja la funcionalidad de CatalogoAdminService.
 */
public class CatalogoAdminService {

    private final CategoriaProductoRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final OfertaRepository ofertaRepository;
    private final OfertaProductoRepository ofertaProductoRepository;
    private final CatalogoMapper mapper;
    private final AuditoriaService auditoriaService;

    public CatalogoAdminService(CategoriaProductoRepository categoriaRepository,
                                ProductoRepository productoRepository,
                                OfertaRepository ofertaRepository,
                                OfertaProductoRepository ofertaProductoRepository,
                                CatalogoMapper mapper,
                                AuditoriaService auditoriaService) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
        this.ofertaRepository = ofertaRepository;
        this.ofertaProductoRepository = ofertaProductoRepository;
        this.mapper = mapper;
        this.auditoriaService = auditoriaService;
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarCategoriasAdmin.
     * @return resultado de la operacion
     */
    public List<CategoriaResponse> listarCategoriasAdmin() {
        return categoriaRepository.findAll().stream()
            .sorted(Comparator.comparing(CategoriaProductoEntity::getOrdenVisual).thenComparing(CategoriaProductoEntity::getNombre))
            .map(mapper::toCategoriaResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarProductosAdmin.
     * @param idCategoria parametro de entrada para la operacion
     * @param estado parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public List<ProductoResponse> listarProductosAdmin(Long idCategoria, String estado) {
        EstadoProducto estadoFiltro = null;
        if (estado != null && !estado.isBlank()) {
            try {
                estadoFiltro = EstadoProducto.valueOf(estado.trim().toUpperCase(Locale.ROOT));
            } catch (Exception e) {
                throw new NegocioException("estado producto invalido");
            }
        }
        EstadoProducto finalEstadoFiltro = estadoFiltro;
        return productoRepository.findAll().stream()
            .filter(p -> idCategoria == null || (p.getCategoria() != null && Objects.equals(p.getCategoria().getIdCategoria(), idCategoria)))
            .filter(p -> finalEstadoFiltro == null || p.getEstado() == finalEstadoFiltro)
            .sorted(Comparator.comparing(ProductoEntity::getOrdenVisual).thenComparing(ProductoEntity::getNombre))
            .map(mapper::toProductoResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarOfertasAdmin.
     * @param estado parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public List<OfertaResponse> listarOfertasAdmin(String estado) {
        EstadoOferta estadoFiltro = null;
        if (estado != null && !estado.isBlank()) {
            try {
                estadoFiltro = EstadoOferta.valueOf(estado.trim().toUpperCase(Locale.ROOT));
            } catch (Exception e) {
                throw new NegocioException("estado oferta invalido");
            }
        }
        EstadoOferta finalEstadoFiltro = estadoFiltro;
        List<OfertaEntity> ofertas = ofertaRepository.findAll().stream()
            .filter(o -> finalEstadoFiltro == null || o.getEstado() == finalEstadoFiltro)
            .toList();
        if (ofertas.isEmpty()) {
            return List.of();
        }

        List<Long> ofertaIds = ofertas.stream()
            .map(OfertaEntity::getIdOferta)
            .toList();
        Map<Long, List<OfertaProductoEntity>> enlacesPorOferta = ofertaProductoRepository.findByOfertaIdOfertaInWithProducto(ofertaIds).stream()
            .collect(Collectors.groupingBy(enlace -> enlace.getOferta().getIdOferta()));

        return ofertas.stream()
            .map(o -> mapper.toOfertaResponse(
                o,
                enlacesPorOferta.getOrDefault(o.getIdOferta(), List.of())
            ))
            .toList();
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de crearCategoria.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public CategoriaResponse crearCategoria(CategoriaRequest request) {
        String nombre = request.nombre().trim();
        if (categoriaRepository.existsByNombreIgnoreCase(nombre)) {
            throw new NegocioException("ya existe una categoria con ese nombre");
        }
        if (categoriaRepository.existsByOrdenVisual(request.ordenVisual())) {
            throw new NegocioException("ya existe una categoria con ese orden visual");
        }
        CategoriaProductoEntity e = new CategoriaProductoEntity();
        e.setNombre(nombre);
        e.setDescripcion(request.descripcion());
        e.setOrdenVisual(request.ordenVisual());
        e.setActiva(request.activa());
        e.setFechaCreacion(LocalDateTime.now());
        e.setFechaActualizacion(LocalDateTime.now());
        CategoriaProductoEntity guardada = categoriaRepository.save(e);
        auditoriaService.registrar(
            "CATEGORIA_PRODUCTO",
            String.valueOf(guardada.getIdCategoria()),
            "CREAR_CATEGORIA",
            AuditoriaActorTipo.ADMIN,
            null,
            AuditoriaCanal.WEB,
            "{\"nombre\":\"" + guardada.getNombre() + "\"}"
        );
        return mapper.toCategoriaResponse(guardada);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de actualizarCategoria.
     * @param idCategoria parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public CategoriaResponse actualizarCategoria(Long idCategoria, CategoriaRequest request) {
        CategoriaProductoEntity e = categoriaRepository.findById(idCategoria)
            .orElseThrow(() -> new NegocioException("categoria no existe"));
        String nuevoNombre = request.nombre().trim();
        String nuevaDescripcion = request.descripcion();
        Integer nuevoOrdenVisual = request.ordenVisual();
        Boolean nuevaActiva = request.activa();

        if (categoriaRepository.existsByNombreIgnoreCaseAndIdCategoriaNot(nuevoNombre, idCategoria)) {
            throw new NegocioException("ya existe una categoria con ese nombre");
        }
        if (categoriaRepository.existsByOrdenVisualAndIdCategoriaNot(nuevoOrdenVisual, idCategoria)) {
            throw new NegocioException("ya existe una categoria con ese orden visual");
        }
        boolean sinCambios = e.getNombre().equalsIgnoreCase(nuevoNombre)
            && Objects.equals(e.getDescripcion(), nuevaDescripcion)
            && Objects.equals(e.getOrdenVisual(), nuevoOrdenVisual)
            && Objects.equals(e.getActiva(), nuevaActiva);
        if (sinCambios) {
            throw new NegocioException("no se detectaron cambios para guardar");
        }

        e.setNombre(nuevoNombre);
        e.setDescripcion(nuevaDescripcion);
        e.setOrdenVisual(nuevoOrdenVisual);
        e.setActiva(nuevaActiva);
        e.setFechaActualizacion(LocalDateTime.now());
        CategoriaProductoEntity guardada = categoriaRepository.save(e);
        auditoriaService.registrar(
            "CATEGORIA_PRODUCTO",
            String.valueOf(guardada.getIdCategoria()),
            "ACTUALIZAR_CATEGORIA",
            AuditoriaActorTipo.ADMIN,
            null,
            AuditoriaCanal.WEB,
            "{\"nombre\":\"" + guardada.getNombre() + "\"}"
        );
        return mapper.toCategoriaResponse(guardada);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de actualizarOrdenCategorias.
     * @param idsCategorias parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public List<CategoriaResponse> actualizarOrdenCategorias(List<Long> idsCategorias) {
        if (idsCategorias == null || idsCategorias.isEmpty()) {
            throw new NegocioException("debe enviar categorias para ordenar");
        }
        Set<Long> unicos = new HashSet<>(idsCategorias);
        if (unicos.size() != idsCategorias.size()) {
            throw new NegocioException("la lista de categorias contiene ids repetidos");
        }

        List<CategoriaProductoEntity> existentes = categoriaRepository.findAll();
        if (existentes.isEmpty()) {
            throw new NegocioException("no existen categorias para ordenar");
        }
        Map<Long, CategoriaProductoEntity> porId = existentes.stream()
            .collect(Collectors.toMap(CategoriaProductoEntity::getIdCategoria, c -> c));

        for (Long idCategoria : idsCategorias) {
            if (!porId.containsKey(idCategoria)) {
                throw new NegocioException("categoria no existe para ordenar");
            }
        }

        int orden = 1;
        for (Long idCategoria : idsCategorias) {
            CategoriaProductoEntity categoria = porId.get(idCategoria);
            categoria.setOrdenVisual(orden++);
            categoria.setFechaActualizacion(LocalDateTime.now());
            categoriaRepository.save(categoria);
        }

        auditoriaService.registrar(
            "CATEGORIA_PRODUCTO",
            "BULK",
            "ORDENAR_CATEGORIAS",
            AuditoriaActorTipo.ADMIN,
            null,
            AuditoriaCanal.WEB,
            "{\"cantidad\":" + idsCategorias.size() + "}"
        );

        return listarCategoriasAdmin();
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de crearProducto.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ProductoResponse crearProducto(ProductoRequest request) {
        String nombre = request.nombre().trim();
        String imagenUrl = normalizeAndValidateOptionalHttpUrl(request.imagenUrl());
        if (productoRepository.existsByNombreIgnoreCase(nombre)) {
            throw new NegocioException("ya existe un producto con ese nombre");
        }
        if (productoRepository.existsByOrdenVisual(request.ordenVisual())) {
            throw new NegocioException("ya existe un producto con ese orden visual");
        }
        ProductoEntity e = new ProductoEntity();
        e.setNombre(nombre);
        e.setDescripcion(request.descripcion());
        if (request.idCategoria() != null) {
            e.setCategoria(categoriaRepository.findById(request.idCategoria()).orElseThrow(() -> new NegocioException("categoria no existe")));
        }
        e.setPrecioBase(request.precioBase());
        e.setDisponible(request.disponible());
        try {
            e.setEstado(EstadoProducto.valueOf(request.estado().trim().toUpperCase(Locale.ROOT)));
        } catch (IllegalArgumentException ex) {
            throw new NegocioException("estado producto invalido");
        }
        e.setVisibleWeb(e.getEstado() == EstadoProducto.ACTIVO);
        e.setImagenUrl(imagenUrl);
        e.setOrdenVisual(request.ordenVisual());
        e.setFechaCreacion(LocalDateTime.now());
        e.setFechaActualizacion(LocalDateTime.now());
        ProductoEntity guardado = productoRepository.save(e);
        auditoriaService.registrar(
            "PRODUCTO",
            String.valueOf(guardado.getIdProducto()),
            "CREAR_PRODUCTO",
            AuditoriaActorTipo.ADMIN,
            null,
            AuditoriaCanal.WEB,
            "{\"nombre\":\"" + guardado.getNombre() + "\"}"
        );
        return mapper.toProductoResponse(guardado);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de actualizarProducto.
     * @param idProducto parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ProductoResponse actualizarProducto(Long idProducto, ProductoRequest request) {
        ProductoEntity e = productoRepository.findById(idProducto)
            .orElseThrow(() -> new NegocioException("producto no existe"));
        String nuevoNombre = request.nombre().trim();
        String nuevaDescripcion = request.descripcion();
        Long nuevaIdCategoria = request.idCategoria();
        var nuevoPrecioBase = request.precioBase();
        Boolean nuevaDisponible = request.disponible();
        String nuevaImagenUrl = normalizeAndValidateOptionalHttpUrl(request.imagenUrl());
        Integer nuevoOrdenVisual = request.ordenVisual();
        String nuevoEstadoTexto = request.estado().trim().toUpperCase(Locale.ROOT);

        if (productoRepository.existsByNombreIgnoreCaseAndIdProductoNot(nuevoNombre, idProducto)) {
            throw new NegocioException("ya existe un producto con ese nombre");
        }
        if (productoRepository.existsByOrdenVisualAndIdProductoNot(nuevoOrdenVisual, idProducto)) {
            throw new NegocioException("ya existe un producto con ese orden visual");
        }

        EstadoProducto nuevoEstado;
        try {
            nuevoEstado = EstadoProducto.valueOf(nuevoEstadoTexto);
        } catch (IllegalArgumentException ex) {
            throw new NegocioException("estado producto invalido");
        }
        boolean visibleWebDerivado = nuevoEstado == EstadoProducto.ACTIVO;

        Long idCategoriaActual = e.getCategoria() != null ? e.getCategoria().getIdCategoria() : null;
        boolean sinCambios = e.getNombre().equalsIgnoreCase(nuevoNombre)
            && Objects.equals(e.getDescripcion(), nuevaDescripcion)
            && Objects.equals(idCategoriaActual, nuevaIdCategoria)
            && Objects.equals(e.getPrecioBase(), nuevoPrecioBase)
            && Objects.equals(e.getVisibleWeb(), visibleWebDerivado)
            && Objects.equals(e.getDisponible(), nuevaDisponible)
            && e.getEstado() == nuevoEstado
            && Objects.equals(e.getImagenUrl(), nuevaImagenUrl)
            && Objects.equals(e.getOrdenVisual(), nuevoOrdenVisual);
        if (sinCambios) {
            throw new NegocioException("no se detectaron cambios para guardar");
        }

        e.setNombre(nuevoNombre);
        e.setDescripcion(nuevaDescripcion);
        if (nuevaIdCategoria != null) {
            e.setCategoria(categoriaRepository.findById(nuevaIdCategoria).orElseThrow(() -> new NegocioException("categoria no existe")));
        } else {
            e.setCategoria(null);
        }
        e.setPrecioBase(nuevoPrecioBase);
        e.setVisibleWeb(nuevoEstado == EstadoProducto.ACTIVO);
        e.setDisponible(nuevaDisponible);
        e.setEstado(nuevoEstado);
        e.setImagenUrl(nuevaImagenUrl);
        e.setOrdenVisual(nuevoOrdenVisual);
        e.setFechaActualizacion(LocalDateTime.now());
        ProductoEntity guardado = productoRepository.save(e);
        auditoriaService.registrar(
            "PRODUCTO",
            String.valueOf(guardado.getIdProducto()),
            "ACTUALIZAR_PRODUCTO",
            AuditoriaActorTipo.ADMIN,
            null,
            AuditoriaCanal.WEB,
            "{\"nombre\":\"" + guardado.getNombre() + "\",\"estado\":\"" + guardado.getEstado().name() + "\"}"
        );
        return mapper.toProductoResponse(guardado);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de actualizarOrdenProductos.
     * @param idsProductos parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public List<ProductoResponse> actualizarOrdenProductos(List<Long> idsProductos) {
        if (idsProductos == null || idsProductos.isEmpty()) {
            throw new NegocioException("debe enviar productos para ordenar");
        }
        Set<Long> unicos = new HashSet<>(idsProductos);
        if (unicos.size() != idsProductos.size()) {
            throw new NegocioException("la lista de productos contiene ids repetidos");
        }

        List<ProductoEntity> existentes = productoRepository.findAll();
        if (existentes.isEmpty()) {
            throw new NegocioException("no existen productos para ordenar");
        }
        Map<Long, ProductoEntity> porId = existentes.stream()
            .collect(Collectors.toMap(ProductoEntity::getIdProducto, p -> p));

        for (Long idProducto : idsProductos) {
            if (!porId.containsKey(idProducto)) {
                throw new NegocioException("producto no existe para ordenar");
            }
        }

        int orden = 1;
        for (Long idProducto : idsProductos) {
            ProductoEntity producto = porId.get(idProducto);
            producto.setOrdenVisual(orden++);
            producto.setFechaActualizacion(LocalDateTime.now());
            productoRepository.save(producto);
        }

        auditoriaService.registrar(
            "PRODUCTO",
            "BULK",
            "ORDENAR_PRODUCTOS",
            AuditoriaActorTipo.ADMIN,
            null,
            AuditoriaCanal.WEB,
            "{\"cantidad\":" + idsProductos.size() + "}"
        );

        return listarProductosAdmin(null, null);
    }

    private String normalizeAndValidateOptionalHttpUrl(String value) {
        if (value == null) return null;
        String normalized = value.trim();
        if (normalized.isEmpty()) return null;
        try {
            URI uri = URI.create(normalized);
            String scheme = uri.getScheme();
            String host = uri.getHost();
            if (scheme == null || host == null) {
                throw new NegocioException("imagenUrl invalida, use una URL http/https valida");
            }
            String s = scheme.toLowerCase(Locale.ROOT);
            if (!"http".equals(s) && !"https".equals(s)) {
                throw new NegocioException("imagenUrl invalida, use una URL http/https valida");
            }
            return normalized;
        } catch (IllegalArgumentException ex) {
            throw new NegocioException("imagenUrl invalida, use una URL http/https valida");
        }
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de crearOferta.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public OfertaResponse crearOferta(OfertaRequest request) {
        OfertaEntity oferta = new OfertaEntity();
        aplicarCamposOferta(oferta, request);
        oferta.setFechaCreacion(LocalDateTime.now());
        oferta.setFechaActualizacion(LocalDateTime.now());

        OfertaEntity guardada = ofertaRepository.save(oferta);
        List<OfertaProductoEntity> enlaces = guardarProductosOferta(guardada, request.idsProductos());

        auditoriaService.registrar(
            "OFERTA",
            String.valueOf(guardada.getIdOferta()),
            "CREAR_OFERTA",
            AuditoriaActorTipo.ADMIN,
            null,
            AuditoriaCanal.WEB,
            "{\"nombre\":\"" + guardada.getNombre() + "\",\"cantidadProductos\":" + enlaces.size() + "}"
        );
        return mapper.toOfertaResponse(guardada, enlaces);
    }

    @Transactional
    /**
     * Metodo que realiza la operacion de actualizarOferta.
     * @param idOferta parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public OfertaResponse actualizarOferta(Long idOferta, OfertaRequest request) {
        OfertaEntity oferta = ofertaRepository.findById(idOferta)
            .orElseThrow(() -> new NegocioException("oferta no existe"));
        aplicarCamposOferta(oferta, request);
        oferta.setFechaActualizacion(LocalDateTime.now());
        OfertaEntity guardada = ofertaRepository.save(oferta);

        ofertaProductoRepository.deleteByOfertaIdOferta(guardada.getIdOferta());
        ofertaProductoRepository.flush();
        List<OfertaProductoEntity> enlaces = guardarProductosOferta(guardada, request.idsProductos());

        auditoriaService.registrar(
            "OFERTA",
            String.valueOf(guardada.getIdOferta()),
            "ACTUALIZAR_OFERTA",
            AuditoriaActorTipo.ADMIN,
            null,
            AuditoriaCanal.WEB,
            "{\"nombre\":\"" + guardada.getNombre() + "\",\"estado\":\"" + guardada.getEstado().name() + "\"}"
        );
        return mapper.toOfertaResponse(guardada, enlaces);
    }

    private void aplicarCamposOferta(OfertaEntity oferta, OfertaRequest request) {
        TipoOferta tipo = parseTipoOferta(request.tipo());
        EstadoOferta estado = parseEstadoOferta(request.estado());
        validarOferta(request, tipo, estado);

        oferta.setNombre(request.nombre().trim());
        oferta.setTipo(tipo);
        oferta.setValorDescuento(tipo == TipoOferta.PRECIO_ESPECIAL || tipo == TipoOferta.COMBO ? null : normalizarMonto(request.valorDescuento()));
        oferta.setPrecioEspecial(tipo == TipoOferta.PRECIO_ESPECIAL || tipo == TipoOferta.COMBO ? normalizarMonto(request.precioEspecial()) : null);
        oferta.setEstado(estado);
        oferta.setFechaInicio(request.fechaInicio());
        oferta.setFechaFin(request.fechaFin());
    }

    private TipoOferta parseTipoOferta(String tipo) {
        try {
            return TipoOferta.valueOf(tipo.trim().toUpperCase(Locale.ROOT));
        } catch (Exception ex) {
            throw new NegocioException("tipo oferta invalido");
        }
    }

    private EstadoOferta parseEstadoOferta(String estado) {
        try {
            return EstadoOferta.valueOf(estado.trim().toUpperCase(Locale.ROOT));
        } catch (Exception ex) {
            throw new NegocioException("estado oferta invalido");
        }
    }

    private void validarOferta(OfertaRequest request, TipoOferta tipo, EstadoOferta estado) {
        if (request.fechaFin().isBefore(request.fechaInicio()) || request.fechaFin().isEqual(request.fechaInicio())) {
            throw new NegocioException("fechaFin debe ser posterior a fechaInicio");
        }
        if (estado == EstadoOferta.ACTIVA) {
            LocalDateTime ahora = LocalDateTime.now();
            if (request.fechaInicio().isAfter(ahora)) {
                throw new NegocioException("fechaInicio debe ser ahora o anterior para una oferta activa");
            }
            if (!request.fechaFin().isAfter(ahora)) {
                throw new NegocioException("fechaFin debe ser futura para una oferta activa");
            }
        }
        if (request.idsProductos() == null || request.idsProductos().isEmpty()) {
            throw new NegocioException("oferta requiere al menos un producto");
        }
        if (request.idsProductos().stream().anyMatch(Objects::isNull)) {
            throw new NegocioException("producto no existe para oferta");
        }

        if (tipo == TipoOferta.PORCENTAJE) {
            if (request.valorDescuento() == null
                || request.valorDescuento().compareTo(new java.math.BigDecimal("0.01")) < 0
                || request.valorDescuento().compareTo(new java.math.BigDecimal("100.00")) > 0) {
                throw new NegocioException("porcentaje de oferta debe estar entre 0.01 y 100");
            }
            return;
        }

        if (tipo == TipoOferta.MONTO_FIJO) {
            if (request.valorDescuento() == null || request.valorDescuento().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                throw new NegocioException("monto de descuento debe ser mayor a 0");
            }
            return;
        }

        if (request.precioEspecial() == null || request.precioEspecial().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new NegocioException("precio especial debe ser mayor a 0");
        }
    }

    private java.math.BigDecimal normalizarMonto(java.math.BigDecimal valor) {
        return valor == null ? null : valor.setScale(2, java.math.RoundingMode.HALF_UP);
    }

    private List<OfertaProductoEntity> guardarProductosOferta(OfertaEntity oferta, List<Long> idsProductos) {
        List<OfertaProductoEntity> enlaces = new ArrayList<>();
        Set<Long> idsUnicos = new HashSet<>(idsProductos);
        for (Long idProducto : idsUnicos) {
            ProductoEntity producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new NegocioException("producto no existe para oferta"));
            OfertaProductoEntity enlace = new OfertaProductoEntity();
            enlace.setOferta(oferta);
            enlace.setProducto(producto);
            enlaces.add(ofertaProductoRepository.save(enlace));
        }
        return enlaces;
    }
}
