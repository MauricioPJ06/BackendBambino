package com.bambino.comprobantes.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "comprobante")
/**
 * Clase que maneja la funcionalidad de ComprobanteEntity.
 */
public class ComprobanteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comprobante")
    private Long idComprobante;

    @Column(name = "id_empresa", nullable = false)
    private Long idEmpresa;

    @Column(name = "id_pedido", nullable = false, unique = true)
    private Long idPedido;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 10)
    private TipoComprobante tipo;

    @Column(name = "serie", nullable = false, length = 10)
    private String serie;

    @Column(name = "correlativo", nullable = false)
    private Long correlativo;

    @Column(name = "numero_completo", nullable = false, length = 30, unique = true)
    private String numeroCompleto;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 10)
    private EstadoComprobante estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "doc_receptor_tipo", length = 10)
    private TipoDocumentoReceptor docReceptorTipo;

    @Column(name = "doc_receptor_numero", length = 20)
    private String docReceptorNumero;

    @Column(name = "razon_social_receptor", length = 200)
    private String razonSocialReceptor;

    @Column(name = "direccion_fiscal_receptor", length = 300)
    private String direccionFiscalReceptor;

    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "impuesto_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal impuestoTotal;

    @Column(name = "total", nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision;

    @Column(name = "correo_enviado", nullable = false)
    private Boolean correoEnviado = false;

    @Column(name = "correo_destino", length = 190)
    private String correoDestino;

    @Column(name = "fecha_correo_envio")
    private LocalDateTime fechaCorreoEnvio;

    @Column(name = "correo_error", length = 500)
    private String correoError;

    @Column(name = "pdf_path", length = 300)
    private String pdfPath;

    @Column(name = "pdf_token", length = 80, unique = true)
    private String pdfToken;

    @Column(name = "fecha_pdf_generado")
    private LocalDateTime fechaPdfGenerado;

    @Column(name = "usuario_creacion")
    private Long usuarioCreacion;

    @Column(name = "usuario_actualizacion")
    private Long usuarioActualizacion;

    public Long getIdComprobante() { return idComprobante; }
    public void setIdComprobante(Long idComprobante) { this.idComprobante = idComprobante; }
    public Long getIdEmpresa() { return idEmpresa; }
    public void setIdEmpresa(Long idEmpresa) { this.idEmpresa = idEmpresa; }
    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
    public TipoComprobante getTipo() { return tipo; }
    public void setTipo(TipoComprobante tipo) { this.tipo = tipo; }
    public String getSerie() { return serie; }
    public void setSerie(String serie) { this.serie = serie; }
    public Long getCorrelativo() { return correlativo; }
    public void setCorrelativo(Long correlativo) { this.correlativo = correlativo; }
    public String getNumeroCompleto() { return numeroCompleto; }
    public void setNumeroCompleto(String numeroCompleto) { this.numeroCompleto = numeroCompleto; }
    public EstadoComprobante getEstado() { return estado; }
    public void setEstado(EstadoComprobante estado) { this.estado = estado; }
    public TipoDocumentoReceptor getDocReceptorTipo() { return docReceptorTipo; }
    public void setDocReceptorTipo(TipoDocumentoReceptor docReceptorTipo) { this.docReceptorTipo = docReceptorTipo; }
    public String getDocReceptorNumero() { return docReceptorNumero; }
    public void setDocReceptorNumero(String docReceptorNumero) { this.docReceptorNumero = docReceptorNumero; }
    public String getRazonSocialReceptor() { return razonSocialReceptor; }
    public void setRazonSocialReceptor(String razonSocialReceptor) { this.razonSocialReceptor = razonSocialReceptor; }
    public String getDireccionFiscalReceptor() { return direccionFiscalReceptor; }
    public void setDireccionFiscalReceptor(String direccionFiscalReceptor) { this.direccionFiscalReceptor = direccionFiscalReceptor; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getImpuestoTotal() { return impuestoTotal; }
    public void setImpuestoTotal(BigDecimal impuestoTotal) { this.impuestoTotal = impuestoTotal; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }
    public Boolean getCorreoEnviado() { return correoEnviado; }
    public void setCorreoEnviado(Boolean correoEnviado) { this.correoEnviado = correoEnviado; }
    public String getCorreoDestino() { return correoDestino; }
    public void setCorreoDestino(String correoDestino) { this.correoDestino = correoDestino; }
    public LocalDateTime getFechaCorreoEnvio() { return fechaCorreoEnvio; }
    public void setFechaCorreoEnvio(LocalDateTime fechaCorreoEnvio) { this.fechaCorreoEnvio = fechaCorreoEnvio; }
    public String getCorreoError() { return correoError; }
    public void setCorreoError(String correoError) { this.correoError = correoError; }
    public String getPdfPath() { return pdfPath; }
    public void setPdfPath(String pdfPath) { this.pdfPath = pdfPath; }
    public String getPdfToken() { return pdfToken; }
    public void setPdfToken(String pdfToken) { this.pdfToken = pdfToken; }
    public LocalDateTime getFechaPdfGenerado() { return fechaPdfGenerado; }
    public void setFechaPdfGenerado(LocalDateTime fechaPdfGenerado) { this.fechaPdfGenerado = fechaPdfGenerado; }
    public Long getUsuarioCreacion() { return usuarioCreacion; }
    public void setUsuarioCreacion(Long usuarioCreacion) { this.usuarioCreacion = usuarioCreacion; }
    public Long getUsuarioActualizacion() { return usuarioActualizacion; }
    public void setUsuarioActualizacion(Long usuarioActualizacion) { this.usuarioActualizacion = usuarioActualizacion; }
}
