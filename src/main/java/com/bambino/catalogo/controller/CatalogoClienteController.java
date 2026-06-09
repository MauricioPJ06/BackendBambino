package com.bambino.catalogo.controller;

import com.bambino.catalogo.dto.CategoriaResponse;
import com.bambino.catalogo.dto.OfertaResponse;
import com.bambino.catalogo.dto.ProductoResponse;
import com.bambino.catalogo.service.CatalogoClienteService;
import com.bambino.shared.exception.NegocioException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/catalogo")
/**
 * Clase que maneja la funcionalidad de CatalogoClienteController.
 */
public class CatalogoClienteController {

    private final CatalogoClienteService catalogoClienteService;

    public CatalogoClienteController(CatalogoClienteService catalogoClienteService) {
        this.catalogoClienteService = catalogoClienteService;
    }

    @GetMapping("/categorias")
    public List<CategoriaResponse> categorias() { return catalogoClienteService.listarCategoriasActivas(); }

    @GetMapping("/productos")
    public List<ProductoResponse> productos(@RequestParam(required = false) Long idCategoria,
                                            @RequestParam(required = false) String q,
                                            @RequestParam(required = false) String filtro) {
        if ("mas-pedidos".equalsIgnoreCase(filtro)) {
            return catalogoClienteService.listarProductosMasPedidosWeb(4);
        }
        if (idCategoria == null && (q == null || q.isBlank())) {
            return catalogoClienteService.listarProductosWeb();
        }
        return catalogoClienteService.listarProductosWebFiltrado(idCategoria, q);
    }

    @GetMapping("/ofertas")
    public List<OfertaResponse> ofertas() { return catalogoClienteService.listarOfertasActivas(); }

    @GetMapping("/productos/{idProducto}")
    /**
     * Metodo que realiza la operacion de productoDetalle.
     * @param idProducto parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ProductoResponse productoDetalle(@PathVariable Long idProducto) {
        ProductoResponse producto = catalogoClienteService.obtenerProductoWebPorId(idProducto);
        if (producto == null) {
            throw new NegocioException("producto no encontrado");
        }
        return producto;
    }

    @GetMapping("/productos/slug/{slug}")
    /**
     * Metodo que realiza la operacion de productoDetallePorSlug.
     * @param slug parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ProductoResponse productoDetallePorSlug(@PathVariable String slug) {
        ProductoResponse producto = catalogoClienteService.obtenerProductoWebPorSlug(slug);
        if (producto == null) {
            throw new NegocioException("producto no encontrado");
        }
        return producto;
    }

    @GetMapping("/productos-adicionales")
    /**
     * Metodo que realiza la operacion de productosAdicionales.
     * @return resultado de la operacion
     */
    public List<ProductoResponse> productosAdicionales() {
        return catalogoClienteService.listarProductosAdicionalesWeb();
    }
}
