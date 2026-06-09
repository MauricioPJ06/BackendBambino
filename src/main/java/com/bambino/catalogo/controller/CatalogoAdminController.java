package com.bambino.catalogo.controller;

import com.bambino.catalogo.dto.*;
import com.bambino.catalogo.service.CatalogoAdminService;
import com.bambino.catalogo.service.LocalImageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/catalogo")
/**
 * Clase que maneja la funcionalidad de CatalogoAdminController.
 */
public class CatalogoAdminController {

    private final CatalogoAdminService catalogoAdminService;
    private final LocalImageService localImageService;

    public CatalogoAdminController(CatalogoAdminService catalogoAdminService, LocalImageService localImageService) {
        this.catalogoAdminService = catalogoAdminService;
        this.localImageService = localImageService;
    }

    @GetMapping("/categorias")
    /**
     * Metodo que realiza la operacion de listarCategorias.
     * @return resultado de la operacion
     */
    public java.util.List<CategoriaResponse> listarCategorias() {
        return catalogoAdminService.listarCategoriasAdmin();
    }

    @PostMapping("/categorias")
    @ResponseStatus(HttpStatus.CREATED)
    /**
     * Metodo que realiza la operacion de crearCategoria.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public CategoriaResponse crearCategoria(@Valid @RequestBody CategoriaRequest request) {
        return catalogoAdminService.crearCategoria(request);
    }

    @PutMapping("/categorias/{idCategoria}")
    /**
     * Metodo que realiza la operacion de actualizarCategoria.
     * @param idCategoria parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public CategoriaResponse actualizarCategoria(@PathVariable Long idCategoria, @Valid @RequestBody CategoriaRequest request) {
        return catalogoAdminService.actualizarCategoria(idCategoria, request);
    }

    @PutMapping("/categorias/orden")
    /**
     * Metodo que realiza la operacion de actualizarOrdenCategorias.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public java.util.List<CategoriaResponse> actualizarOrdenCategorias(@Valid @RequestBody CategoriaOrdenRequest request) {
        return catalogoAdminService.actualizarOrdenCategorias(request.idsCategorias());
    }

    @PostMapping("/productos")
    @ResponseStatus(HttpStatus.CREATED)
    /**
     * Metodo que realiza la operacion de crearProducto.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ProductoResponse crearProducto(@Valid @RequestBody ProductoRequest request) {
        return catalogoAdminService.crearProducto(request);
    }

    @GetMapping("/productos")
    public java.util.List<ProductoResponse> listarProductos(@RequestParam(required = false) Long idCategoria,
                                                            @RequestParam(required = false) String estado) {
        return catalogoAdminService.listarProductosAdmin(idCategoria, estado);
    }

    @PutMapping("/productos/{idProducto}")
    /**
     * Metodo que realiza la operacion de actualizarProducto.
     * @param idProducto parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ProductoResponse actualizarProducto(@PathVariable Long idProducto, @Valid @RequestBody ProductoRequest request) {
        return catalogoAdminService.actualizarProducto(idProducto, request);
    }

    @PutMapping("/productos/orden")
    /**
     * Metodo que realiza la operacion de actualizarOrdenProductos.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public java.util.List<ProductoResponse> actualizarOrdenProductos(@Valid @RequestBody ProductoOrdenRequest request) {
        return catalogoAdminService.actualizarOrdenProductos(request.idsProductos());
    }

    @PostMapping("/productos/imagen")
    @ResponseStatus(HttpStatus.CREATED)
    public ImagenUploadResponse subirImagenProducto(@RequestParam("archivo") MultipartFile archivo) {
        String url = localImageService.uploadProductoImagen(archivo);
        return new ImagenUploadResponse(url);
    }

    @PostMapping("/ofertas")
    @ResponseStatus(HttpStatus.CREATED)
    /**
     * Metodo que realiza la operacion de crearOferta.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public OfertaResponse crearOferta(@Valid @RequestBody OfertaRequest request) {
        return catalogoAdminService.crearOferta(request);
    }

    @GetMapping("/ofertas")
    public java.util.List<OfertaResponse> listarOfertas(@RequestParam(required = false) String estado) {
        return catalogoAdminService.listarOfertasAdmin(estado);
    }

    @PutMapping("/ofertas/{idOferta}")
    /**
     * Metodo que realiza la operacion de actualizarOferta.
     * @param idOferta parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public OfertaResponse actualizarOferta(@PathVariable Long idOferta, @Valid @RequestBody OfertaRequest request) {
        return catalogoAdminService.actualizarOferta(idOferta, request);
    }
}
