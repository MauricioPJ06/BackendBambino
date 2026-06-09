package com.bambino.catalogo.service;

import com.bambino.catalogo.dto.CategoriaResponse;
import com.bambino.catalogo.dto.OfertaResponse;
import com.bambino.catalogo.dto.ProductoResponse;
import com.bambino.catalogo.entity.EstadoOferta;
import com.bambino.catalogo.entity.EstadoProducto;
import com.bambino.catalogo.entity.OfertaEntity;
import com.bambino.catalogo.entity.OfertaProductoEntity;
import com.bambino.catalogo.entity.ProductoEntity;
import com.bambino.catalogo.entity.TipoOferta;
import com.bambino.catalogo.mapper.CatalogoMapper;
import com.bambino.catalogo.repository.CategoriaProductoRepository;
import com.bambino.catalogo.repository.OfertaProductoRepository;
import com.bambino.catalogo.repository.OfertaRepository;
import com.bambino.catalogo.repository.ProductoRepository;
import com.bambino.pedidos.entity.EstadoPedido;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
/**
 * Clase que maneja la funcionalidad de CatalogoClienteService.
 */
public class CatalogoClienteService {

    private final CategoriaProductoRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final OfertaRepository ofertaRepository;
    private final OfertaProductoRepository ofertaProductoRepository;
    private final CatalogoMapper mapper;

    public CatalogoClienteService(CategoriaProductoRepository categoriaRepository,
                                  ProductoRepository productoRepository,
                                  OfertaRepository ofertaRepository,
                                  OfertaProductoRepository ofertaProductoRepository,
                                  CatalogoMapper mapper) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
        this.ofertaRepository = ofertaRepository;
        this.ofertaProductoRepository = ofertaProductoRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarCategoriasActivas.
     * @return resultado de la operacion
     */
    public List<CategoriaResponse> listarCategoriasActivas() {
        return categoriaRepository.findByActivaTrueOrderByOrdenVisualAscNombreAsc().stream().map(mapper::toCategoriaResponse).toList();
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarProductosWeb.
     * @return resultado de la operacion
     */
    public List<ProductoResponse> listarProductosWeb() {
        return productoRepository.findByEstadoAndVisibleWebTrueAndDisponibleTrueOrderByOrdenVisualAscNombreAsc(EstadoProducto.ACTIVO)
            .stream().map(this::toProductoResponseConOferta).toList();
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de obtenerProductoWebPorId.
     * @param idProducto parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ProductoResponse obtenerProductoWebPorId(Long idProducto) {
        return productoRepository.findByIdProductoAndEstadoAndVisibleWebTrueAndDisponibleTrue(idProducto, EstadoProducto.ACTIVO)
            .map(this::toProductoResponseConOferta)
            .orElse(null);
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de obtenerProductoWebPorSlug.
     * @param slug parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ProductoResponse obtenerProductoWebPorSlug(String slug) {
        String objetivo = slug == null ? "" : slug.trim();
        if (objetivo.isBlank()) {
            return null;
        }
        return productoRepository.findByEstadoAndVisibleWebTrueAndDisponibleTrueOrderByOrdenVisualAscNombreAsc(EstadoProducto.ACTIVO)
            .stream()
            .filter(p -> normalizarSlug(p.getNombre()).equals(objetivo))
            .findFirst()
            .map(this::toProductoResponseConOferta)
            .orElse(null);
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarProductosAdicionalesWeb.
     * @return resultado de la operacion
     */
    public List<ProductoResponse> listarProductosAdicionalesWeb() {
        return productoRepository
            .findByEstadoAndVisibleWebTrueAndDisponibleTrueAndCategoria_NombreIgnoreCaseOrderByOrdenVisualAscNombreAsc(
                EstadoProducto.ACTIVO,
                "Adicionales"
            )
            .stream()
            .map(this::toProductoResponseConOferta)
            .toList();
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarProductosWebFiltrado.
     * @param idCategoria parametro de entrada para la operacion
     * @param textoBusqueda parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public List<ProductoResponse> listarProductosWebFiltrado(Long idCategoria, String textoBusqueda) {
        String q = textoBusqueda == null ? null : textoBusqueda.trim().toLowerCase(Locale.ROOT);
        return productoRepository.findPublicosFiltrados(EstadoProducto.ACTIVO, idCategoria, q)
            .stream()
            .map(this::toProductoResponseConOferta)
            .toList();
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarOfertasActivas.
     * @return resultado de la operacion
     */
    public List<OfertaResponse> listarOfertasActivas() {
        LocalDateTime ahora = LocalDateTime.now();
        List<com.bambino.catalogo.entity.OfertaEntity> ofertas = ofertaRepository
            .findByEstadoAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqualOrderByFechaInicioDesc(EstadoOferta.ACTIVA, ahora, ahora);
        if (ofertas.isEmpty()) {
            return List.of();
        }
        List<Long> idsOferta = ofertas.stream().map(com.bambino.catalogo.entity.OfertaEntity::getIdOferta).toList();
        Map<Long, List<com.bambino.catalogo.entity.OfertaProductoEntity>> enlacesPorOferta = ofertaProductoRepository
            .findByOfertaIdOfertaInWithProducto(idsOferta)
            .stream()
            .collect(Collectors.groupingBy(op -> op.getOferta().getIdOferta(), HashMap::new, Collectors.toList()));
        return ofertas
            .stream()
            .map(o -> mapper.toOfertaResponse(o, enlacesPorOferta.getOrDefault(o.getIdOferta(), List.of())))
            .toList();
    }

    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de listarProductosMasPedidosWeb.
     * @param topN parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public List<ProductoResponse> listarProductosMasPedidosWeb(int topN) {
        int limite = Math.max(topN, 1);
        LocalDateTime fin = LocalDateTime.now();
        List<EstadoPedido> estadosConsiderados = List.of(
            EstadoPedido.PAGO_APROBADO,
            EstadoPedido.CONFIRMADO,
            EstadoPedido.EN_PREPARACION,
            EstadoPedido.LISTO_RECOJO,
            EstadoPedido.LISTO_DESPACHO,
            EstadoPedido.EN_CAMINO,
            EstadoPedido.ENTREGADO
        );

        Set<Long> seleccion = new LinkedHashSet<>();
        for (int semana = 0; semana < 8 && seleccion.size() < limite; semana++) {
            LocalDateTime hasta = fin.minusDays((long) semana * 7);
            LocalDateTime desde = hasta.minusDays(7);
            List<Long> idsSemana = productoRepository.findTopProductoIdsVendidosEntreFechas(desde, hasta, estadosConsiderados);
            for (Long id : idsSemana) {
                if (id != null) {
                    seleccion.add(id);
                    if (seleccion.size() >= limite) break;
                }
            }
        }

        List<Long> idsOrdenados = new ArrayList<>(seleccion);
        if (idsOrdenados.isEmpty()) {
            return listarProductosWeb().stream().limit(limite).toList();
        }

        Map<Long, Integer> ranking = new HashMap<>();
        for (int i = 0; i < idsOrdenados.size(); i++) ranking.put(idsOrdenados.get(i), i);

        Map<Long, ProductoResponse> porId = productoRepository.findByIdProductoIn(idsOrdenados).stream()
            .map(this::toProductoResponseConOferta)
            .collect(Collectors.toMap(ProductoResponse::idProducto, Function.identity()));

        List<ProductoResponse> ordenados = idsOrdenados.stream()
            .map(porId::get)
            .filter(Objects::nonNull)
            .sorted(Comparator.comparingInt(p -> ranking.getOrDefault(p.idProducto(), Integer.MAX_VALUE)))
            .limit(limite)
            .toList();

        if (ordenados.size() >= limite) {
            return ordenados;
        }

        Set<Long> ya = ordenados.stream().map(ProductoResponse::idProducto).collect(Collectors.toSet());
        List<ProductoResponse> fallback = listarProductosWeb().stream()
            .filter(p -> !ya.contains(p.idProducto()))
            .limit(limite - ordenados.size())
            .toList();

        List<ProductoResponse> finalList = new ArrayList<>(ordenados);
        finalList.addAll(fallback);
        return finalList;
    }

    private ProductoResponse toProductoResponseConOferta(ProductoEntity producto) {
        PrecioOfertaCatalogo precio = calcularPrecioOferta(producto);
        return mapper.toProductoResponse(producto, precio.precioFinal(), precio.descuentoAplicado(), precio.oferta());
    }

    private PrecioOfertaCatalogo calcularPrecioOferta(ProductoEntity producto) {
        BigDecimal precioBase = producto.getPrecioBase().setScale(2, RoundingMode.HALF_UP);
        List<OfertaProductoEntity> ofertas = ofertaProductoRepository.findOfertasActivasByProducto(producto.getIdProducto(), LocalDateTime.now());
        if (ofertas.isEmpty()) {
            return new PrecioOfertaCatalogo(precioBase, BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), null);
        }

        OfertaEntity oferta = ofertas.get(0).getOferta();
        BigDecimal descuento = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        if (oferta.getTipo() == TipoOferta.PORCENTAJE && oferta.getValorDescuento() != null) {
            descuento = precioBase.multiply(oferta.getValorDescuento()).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        } else if (oferta.getTipo() == TipoOferta.MONTO_FIJO && oferta.getValorDescuento() != null) {
            descuento = oferta.getValorDescuento().setScale(2, RoundingMode.HALF_UP);
        } else if ((oferta.getTipo() == TipoOferta.PRECIO_ESPECIAL || oferta.getTipo() == TipoOferta.COMBO) && oferta.getPrecioEspecial() != null) {
            descuento = precioBase.subtract(oferta.getPrecioEspecial().setScale(2, RoundingMode.HALF_UP));
        }

        if (descuento.compareTo(BigDecimal.ZERO) < 0) {
            descuento = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        if (descuento.compareTo(precioBase) > 0) {
            descuento = precioBase;
        }
        return new PrecioOfertaCatalogo(precioBase.subtract(descuento).setScale(2, RoundingMode.HALF_UP), descuento, oferta);
    }

    private record PrecioOfertaCatalogo(BigDecimal precioFinal, BigDecimal descuentoAplicado, OfertaEntity oferta) {}

    private String normalizarSlug(String nombre) {
        String base = nombre == null ? "" : nombre.trim();
        if (base.isBlank()) return "";
        String sinTildes = Normalizer.normalize(base, Normalizer.Form.NFD)
            .replaceAll("\\p{M}+", "");
        return sinTildes
            .toLowerCase(Locale.ROOT)
            .replaceAll("[^a-z0-9]+", "-")
            .replaceAll("^-+|-+$", "");
    }
}
