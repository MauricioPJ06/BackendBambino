package com.bambino.comprobantes.service;

import com.bambino.comprobantes.dto.ComprobantePdfResponse;
import com.bambino.comprobantes.entity.ComprobanteDetalleEntity;
import com.bambino.comprobantes.entity.ComprobanteEntity;
import com.bambino.comprobantes.entity.EmpresaEntity;
import com.bambino.comprobantes.entity.TipoComprobante;
import com.bambino.comprobantes.repository.ComprobanteDetalleRepository;
import com.bambino.comprobantes.repository.ComprobanteRepository;
import com.bambino.comprobantes.repository.EmpresaRepository;
import com.bambino.pagos.entity.PagoEntity;
import com.bambino.pagos.repository.PagoRepository;
import com.bambino.pedidos.entity.PedidoEntity;
import com.bambino.pedidos.repository.PedidoRepository;
import com.bambino.seguridad.repository.UsuarioRepository;
import com.bambino.shared.exception.NegocioException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class ComprobantePdfService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.ROOT);

    private final ComprobanteRepository comprobanteRepository;
    private final ComprobanteDetalleRepository comprobanteDetalleRepository;
    private final EmpresaRepository empresaRepository;
    private final PedidoRepository pedidoRepository;
    private final PagoRepository pagoRepository;
    private final UsuarioRepository usuarioRepository;
    private final Path uploadRoot;
    private final String publicBaseUrl;

    public ComprobantePdfService(ComprobanteRepository comprobanteRepository,
                                 ComprobanteDetalleRepository comprobanteDetalleRepository,
                                 EmpresaRepository empresaRepository,
                                 PedidoRepository pedidoRepository,
                                 PagoRepository pagoRepository,
                                 UsuarioRepository usuarioRepository,
                                 @Value("${app.upload.dir:${UPLOAD_DIR:/app/uploads}}") String uploadDir,
                                 @Value("${app.public-base-url:http://localhost:8080}") String publicBaseUrl) {
        this.comprobanteRepository = comprobanteRepository;
        this.comprobanteDetalleRepository = comprobanteDetalleRepository;
        this.empresaRepository = empresaRepository;
        this.pedidoRepository = pedidoRepository;
        this.pagoRepository = pagoRepository;
        this.usuarioRepository = usuarioRepository;
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.publicBaseUrl = normalizarBaseUrl(publicBaseUrl);
    }

    @Transactional
    public ComprobantePdfResponse generarPdfAdmin(Long idComprobante) {
        ComprobanteEntity comprobante = comprobanteRepository.findById(idComprobante)
            .orElseThrow(() -> new NegocioException("comprobante no encontrado"));
        return obtenerPdfPersistido(comprobante);
    }

    @Transactional
    public ComprobantePdfResponse generarPdfClientePorPedido(String emailAutenticado, Long idPedido) {
        Long idCliente = usuarioRepository.findByEmail(emailAutenticado)
            .orElseThrow(() -> new NegocioException("usuario autenticado no encontrado"))
            .getIdUsuario();
        PedidoEntity pedido = pedidoRepository.findById(idPedido)
            .orElseThrow(() -> new NegocioException("pedido no encontrado"));
        if (!pedido.getIdCliente().equals(idCliente)) {
            throw new NegocioException("el pedido no pertenece al cliente autenticado");
        }

        ComprobanteEntity comprobante = comprobanteRepository.findByIdPedido(idPedido)
            .orElseThrow(() -> new NegocioException("el pedido aun no tiene comprobante emitido"));
        return obtenerPdfPersistido(comprobante);
    }

    @Transactional
    public ComprobantePdfResponse generarPdfPublicoPorToken(String token) {
        if (!hasText(token)) {
            throw new NegocioException("token de comprobante invalido");
        }
        ComprobanteEntity comprobante = comprobanteRepository.findByPdfToken(token.trim())
            .orElseThrow(() -> new NegocioException("comprobante no encontrado"));
        return obtenerPdfPersistido(comprobante);
    }

    private ComprobantePdfResponse obtenerPdfPersistido(ComprobanteEntity comprobante) {
        if (hasText(comprobante.getPdfPath())) {
            Path existente = resolverPathSeguro(comprobante.getPdfPath());
            if (Files.exists(existente)) {
                try {
                    return new ComprobantePdfResponse(Files.readAllBytes(existente), comprobante.getNumeroCompleto() + ".pdf");
                } catch (IOException e) {
                    throw new NegocioException("no se pudo leer el PDF del comprobante");
                }
            }
        }

        asegurarTokenPdf(comprobante);
        EmpresaEntity empresa = empresaRepository.findById(comprobante.getIdEmpresa())
            .orElseThrow(() -> new NegocioException("empresa del comprobante no encontrada"));
        PedidoEntity pedido = pedidoRepository.findById(comprobante.getIdPedido())
            .orElseThrow(() -> new NegocioException("pedido del comprobante no encontrado"));
        List<ComprobanteDetalleEntity> detalle = comprobanteDetalleRepository
            .findByIdComprobanteOrderByIdComprobanteDetalleAsc(comprobante.getIdComprobante());
        if (detalle.isEmpty()) {
            throw new NegocioException("el comprobante no tiene detalle para generar PDF");
        }

        String cliente = resolverCliente(comprobante, pedido);
        String metodoPago = pagoRepository.findByIdPedidoOrderByFechaCreacionDesc(comprobante.getIdPedido())
            .stream()
            .findFirst()
            .map(PagoEntity::getMetodo)
            .map(Enum::name)
            .map(this::labelEnum)
            .orElse("No registrado");

        byte[] contenido = generarDocumento(comprobante, empresa, detalle, cliente, metodoPago, urlPublica(comprobante));
        String relativePath = relativePdfPath(comprobante);
        Path destino = resolverPathSeguro(relativePath);
        try {
            Files.createDirectories(destino.getParent());
            Files.write(destino, contenido);
        } catch (IOException e) {
            throw new NegocioException("no se pudo guardar el PDF del comprobante");
        }
        comprobante.setPdfPath(relativePath);
        comprobante.setFechaPdfGenerado(LocalDateTime.now());
        comprobanteRepository.save(comprobante);
        return new ComprobantePdfResponse(contenido, comprobante.getNumeroCompleto() + ".pdf");
    }

    private byte[] generarDocumento(ComprobanteEntity comprobante,
                                    EmpresaEntity empresa,
                                    List<ComprobanteDetalleEntity> detalle,
                                    String cliente,
                                    String metodoPago,
                                    String qrUrl) {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Rectangle pageSize = new Rectangle(250, 860);
            Document document = new Document(pageSize, 16, 16, 18, 18);
            PdfWriter.getInstance(document, output);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
            Font tableFont = FontFactory.getFont(FontFactory.HELVETICA, 7);
            Font tableBoldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7);
            Font strongFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);

            addCentered(document, textoPreferido(empresa.getNombreComercial(), "BAMBINO CHICKEN").toUpperCase(Locale.ROOT), titleFont, 0, 4);
            addCentered(document, textoPreferido(empresa.getDireccionFiscal(), "Av. Alfredo Mendiola 6789 Chorrillos Lima").toUpperCase(Locale.ROOT), normalFont, 0, 12);
            addLine(document);
            addCentered(document, tituloComprobante(comprobante.getTipo()), normalFont, 7, 5);
            addCentered(document, comprobante.getSerie() + " - " + String.format(Locale.ROOT, "%06d", comprobante.getCorrelativo()), strongFont, 0, 16);

            addLabel(document, "Fecha de creacion: ", comprobante.getFechaEmision().format(DATE_FORMAT), boldFont, normalFont);
            addLabel(document, "Cliente: ", cliente, boldFont, normalFont);
            addLabel(document, "Forma de pago: ", metodoPago, boldFont, normalFont);
            addLabel(document, documentoLabel(comprobante) + ": ", textoPreferido(comprobante.getDocReceptorNumero(), "No registrado"), boldFont, normalFont);

            addLine(document);
            document.add(itemsTable(detalle, tableBoldFont, tableFont));
            addLine(document);
            addTotal(document, "O.P GRAVADA", comprobante.getSubtotal(), boldFont);
            addTotal(document, "I.G.V (18%)", comprobante.getImpuestoTotal(), boldFont);
            addTotal(document, "TOTAL A PAGAR", comprobante.getTotal(), strongFont);
            addLine(document);
            Paragraph words = new Paragraph("SON: " + montoEnLetras(comprobante.getTotal()), boldFont);
            words.setSpacingAfter(28);
            document.add(words);
            addCentered(document, "ESCANEA EL QR PARA ABRIR TU PDF", boldFont, 4, 5);
            addQr(document, qrUrl);
            addCentered(document, "GRACIAS POR SU COMPRA", strongFont, 8, 0);

            document.close();
            return output.toByteArray();
        } catch (DocumentException | IOException | WriterException e) {
            throw new NegocioException("no se pudo generar el PDF del comprobante");
        }
    }

    private void addQr(Document document, String qrUrl) throws WriterException, IOException, DocumentException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(qrUrl, BarcodeFormat.QR_CODE, 120, 120);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", output);
        Image image = Image.getInstance(output.toByteArray());
        image.scaleAbsolute(74, 74);
        image.setAlignment(Element.ALIGN_CENTER);
        document.add(image);
    }

    private PdfPTable itemsTable(List<ComprobanteDetalleEntity> detalle, Font boldFont, Font normalFont) {
        PdfPTable table = new PdfPTable(new float[] { 0.8f, 3.25f, 1.15f, 1.25f });
        table.setWidthPercentage(100);
        addHeaderCell(table, "CANT", boldFont);
        addHeaderCell(table, "DESCRIPCION", boldFont);
        addHeaderCell(table, "PRECIO", boldFont);
        addHeaderCell(table, "IMPORTE", boldFont);

        for (ComprobanteDetalleEntity item : detalle) {
            addBodyCell(table, decimal(item.getCantidad(), 2), normalFont, Element.ALIGN_LEFT);
            addBodyCell(table, item.getDescripcionItem(), normalFont, Element.ALIGN_LEFT);
            addBodyCell(table, money(precioCobrado(item)), normalFont, Element.ALIGN_RIGHT);
            addBodyCell(table, money(item.getSubtotalLinea()), normalFont, Element.ALIGN_RIGHT);
        }
        return table;
    }

    private void addHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = cell(text, font);
        cell.setPaddingBottom(4);
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderWidth(0.6f);
        table.addCell(cell);
    }

    private void addBodyCell(PdfPTable table, String text, Font font, int align) {
        PdfPCell cell = cell(text, font);
        cell.setHorizontalAlignment(align);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(2);
        table.addCell(cell);
    }

    private PdfPCell cell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(textoPreferido(text, "-"), font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(1.5f);
        return cell;
    }

    private void addCentered(Document document, String text, Font font, float before, float after) throws DocumentException {
        Paragraph paragraph = new Paragraph(text, font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingBefore(before);
        paragraph.setSpacingAfter(after);
        document.add(paragraph);
    }

    private void addLabel(Document document, String label, String value, Font labelFont, Font valueFont) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Phrase(label, labelFont));
        paragraph.add(new Phrase(textoPreferido(value, "-"), valueFont));
        paragraph.setSpacingAfter(4);
        document.add(paragraph);
    }

    private void addTotal(Document document, String label, BigDecimal value, Font font) throws DocumentException {
        Paragraph paragraph = new Paragraph(label + " S/ " + decimal(value, 2), font);
        paragraph.setAlignment(Element.ALIGN_RIGHT);
        paragraph.setSpacingAfter(5);
        document.add(paragraph);
    }

    private void addLine(Document document) throws DocumentException {
        Paragraph paragraph = new Paragraph("------------------------------------------------------", FontFactory.getFont(FontFactory.HELVETICA, 8));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingBefore(3);
        paragraph.setSpacingAfter(7);
        document.add(paragraph);
    }

    private String resolverCliente(ComprobanteEntity comprobante, PedidoEntity pedido) {
        if (comprobante.getTipo() == TipoComprobante.FACTURA && hasText(comprobante.getRazonSocialReceptor())) {
            return comprobante.getRazonSocialReceptor();
        }
        return usuarioRepository.findById(pedido.getIdCliente())
            .map(usuario -> textoPreferido((usuario.getNombres() + " " + usuario.getApellidos()).trim(), usuario.getEmail()))
            .orElse("Cliente");
    }

    private String tituloComprobante(TipoComprobante tipo) {
        return tipo == TipoComprobante.FACTURA
            ? "FACTURA DE VENTA ELECTRONICA"
            : "BOLETA DE VENTA ELECTRONICA";
    }

    private String documentoLabel(ComprobanteEntity comprobante) {
        if (comprobante.getTipo() == TipoComprobante.FACTURA) {
            return "RUC";
        }
        if (comprobante.getDocReceptorTipo() != null) {
            return comprobante.getDocReceptorTipo().name();
        }
        return "DNI";
    }

    private BigDecimal precioCobrado(ComprobanteDetalleEntity item) {
        return nvl(item.getPrecioUnitario()).subtract(nvl(item.getDescuentoUnitario())).max(BigDecimal.ZERO);
    }

    private String montoEnLetras(BigDecimal value) {
        BigDecimal total = nvl(value).setScale(2, RoundingMode.HALF_UP);
        long soles = total.longValue();
        int centimos = total.remainder(BigDecimal.ONE).movePointRight(2).abs().intValue();
        return numeroEnLetras(soles).toUpperCase(Locale.ROOT) + " CON " + String.format(Locale.ROOT, "%02d", centimos) + "/100 SOLES";
    }

    private String numeroEnLetras(long value) {
        if (value == 0) return "cero";
        if (value < 0) return "menos " + numeroEnLetras(Math.abs(value));
        if (value <= 29) {
            String[] units = {
                "", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve",
                "diez", "once", "doce", "trece", "catorce", "quince", "dieciseis", "diecisiete", "dieciocho", "diecinueve",
                "veinte", "veintiuno", "veintidos", "veintitres", "veinticuatro", "veinticinco", "veintiseis", "veintisiete", "veintiocho", "veintinueve"
            };
            return units[(int) value];
        }
        if (value < 100) {
            String[] tens = { "", "", "veinte", "treinta", "cuarenta", "cincuenta", "sesenta", "setenta", "ochenta", "noventa" };
            long unit = value % 10;
            return tens[(int) (value / 10)] + (unit == 0 ? "" : " y " + numeroEnLetras(unit));
        }
        if (value < 1000) {
            String[] hundreds = { "", "ciento", "doscientos", "trescientos", "cuatrocientos", "quinientos", "seiscientos", "setecientos", "ochocientos", "novecientos" };
            if (value == 100) return "cien";
            long rest = value % 100;
            return hundreds[(int) (value / 100)] + (rest == 0 ? "" : " " + numeroEnLetras(rest));
        }
        if (value < 1_000_000) {
            long thousands = value / 1000;
            long rest = value % 1000;
            String prefix = thousands == 1 ? "mil" : numeroEnLetras(thousands) + " mil";
            return prefix + (rest == 0 ? "" : " " + numeroEnLetras(rest));
        }
        return String.valueOf(value);
    }

    private String labelEnum(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        String lower = value.replace('_', ' ').toLowerCase(Locale.ROOT);
        return lower.substring(0, 1).toUpperCase(Locale.ROOT) + lower.substring(1);
    }

    private String money(BigDecimal value) {
        return "S/ " + decimal(value, 2);
    }

    private String decimal(BigDecimal value, int scale) {
        return nvl(value).setScale(scale, RoundingMode.HALF_UP).toPlainString();
    }

    private BigDecimal nvl(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private String textoPreferido(String value, String fallback) {
        return hasText(value) ? value.trim() : fallback;
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private void asegurarTokenPdf(ComprobanteEntity comprobante) {
        if (hasText(comprobante.getPdfToken())) {
            return;
        }
        comprobante.setPdfToken(UUID.randomUUID().toString().replace("-", ""));
        comprobanteRepository.save(comprobante);
    }

    private String urlPublica(ComprobanteEntity comprobante) {
        return publicBaseUrl + "/api/public/comprobantes/t/" + comprobante.getPdfToken();
    }

    private String relativePdfPath(ComprobanteEntity comprobante) {
        LocalDateTime fecha = comprobante.getFechaEmision() == null ? LocalDateTime.now() : comprobante.getFechaEmision();
        String filename = comprobante.getNumeroCompleto()
            .replaceAll("[^A-Za-z0-9._-]", "_") + ".pdf";
        return String.format(Locale.ROOT, "comprobantes/%04d/%02d/%s", fecha.getYear(), fecha.getMonthValue(), filename);
    }

    private Path resolverPathSeguro(String relativePath) {
        Path path = uploadRoot.resolve(relativePath).normalize();
        if (!path.startsWith(uploadRoot)) {
            throw new NegocioException("ruta de PDF invalida");
        }
        return path;
    }

    private String normalizarBaseUrl(String value) {
        String base = value == null || value.isBlank() ? "http://localhost:8080" : value.trim();
        while (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        return base;
    }
}
