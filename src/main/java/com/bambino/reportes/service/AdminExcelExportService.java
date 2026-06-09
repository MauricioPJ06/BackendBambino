package com.bambino.reportes.service;

import com.bambino.auditoria.entity.AuditoriaActorTipo;
import com.bambino.auditoria.entity.AuditoriaEventoEntity;
import com.bambino.auditoria.repository.AuditoriaEventoRepository;
import com.bambino.comprobantes.entity.ComprobanteEntity;
import com.bambino.comprobantes.repository.ComprobanteRepository;
import com.bambino.pagos.entity.PagoEntity;
import com.bambino.pagos.repository.PagoRepository;
import com.bambino.pedidos.entity.PedidoEntity;
import com.bambino.pedidos.repository.PedidoRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
public class AdminExcelExportService {

    private static final int BLOCK_SIZE = 500;

    private final PedidoRepository pedidoRepository;
    private final PagoRepository pagoRepository;
    private final ComprobanteRepository comprobanteRepository;
    private final AuditoriaEventoRepository auditoriaEventoRepository;

    public AdminExcelExportService(PedidoRepository pedidoRepository,
                                   PagoRepository pagoRepository,
                                   ComprobanteRepository comprobanteRepository,
                                   AuditoriaEventoRepository auditoriaEventoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.pagoRepository = pagoRepository;
        this.comprobanteRepository = comprobanteRepository;
        this.auditoriaEventoRepository = auditoriaEventoRepository;
    }

    @Transactional(readOnly = true)
    public void exportarPedidos(OutputStream outputStream) throws IOException {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(100)) {
            Sheet sheet = workbook.createSheet("Pedidos");
            CellStyle headerStyle = headerStyle(workbook);
            writeHeader(sheet, headerStyle, "ID Pedido", "Codigo", "Estado", "Modalidad", "Comprobante", "Subtotal", "Descuento", "IGV", "Total", "Fecha creacion");
            int rowIndex = 1;
            int page = 0;
            Page<PedidoEntity> block;
            do {
                Pageable pageable = PageRequest.of(page, BLOCK_SIZE, Sort.by(Sort.Direction.DESC, "fechaCreacion"));
                block = pedidoRepository.findAll(pageable);
                for (PedidoEntity pedido : block.getContent()) {
                    Row row = sheet.createRow(rowIndex++);
                    write(row, 0, pedido.getIdPedido());
                    write(row, 1, pedido.getCodigoPedido());
                    write(row, 2, enumName(pedido.getEstadoActual()));
                    write(row, 3, enumName(pedido.getModalidad()));
                    write(row, 4, enumName(pedido.getTipoComprobante()));
                    write(row, 5, pedido.getSubtotal());
                    write(row, 6, pedido.getDescuentoTotal());
                    write(row, 7, pedido.getImpuestoTotal());
                    write(row, 8, pedido.getTotal());
                    write(row, 9, pedido.getFechaCreacion());
                }
                page++;
            } while (block.hasNext());
            workbook.write(outputStream);
            workbook.dispose();
        }
    }

    @Transactional(readOnly = true)
    public void exportarPagos(OutputStream outputStream) throws IOException {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(100)) {
            Sheet sheet = workbook.createSheet("Pagos");
            CellStyle headerStyle = headerStyle(workbook);
            writeHeader(sheet, headerStyle, "ID Pago", "ID Pedido", "Metodo", "Estado", "Monto", "Proveedor", "Transaccion", "Idempotency Key", "Fecha creacion", "Fecha actualizacion");
            int rowIndex = 1;
            int page = 0;
            Page<PagoEntity> block;
            do {
                Pageable pageable = PageRequest.of(page, BLOCK_SIZE, Sort.by(Sort.Direction.DESC, "fechaCreacion"));
                block = pagoRepository.findAll(pageable);
                for (PagoEntity pago : block.getContent()) {
                    Row row = sheet.createRow(rowIndex++);
                    write(row, 0, pago.getIdPago());
                    write(row, 1, pago.getIdPedido());
                    write(row, 2, enumName(pago.getMetodo()));
                    write(row, 3, enumName(pago.getEstado()));
                    write(row, 4, pago.getMonto());
                    write(row, 5, pago.getProveedor());
                    write(row, 6, pago.getProveedorTxnId());
                    write(row, 7, pago.getIdempotencyKey());
                    write(row, 8, pago.getFechaCreacion());
                    write(row, 9, pago.getFechaActualizacion());
                }
                page++;
            } while (block.hasNext());
            workbook.write(outputStream);
            workbook.dispose();
        }
    }

    @Transactional(readOnly = true)
    public void exportarComprobantes(OutputStream outputStream) throws IOException {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(100)) {
            Sheet sheet = workbook.createSheet("Comprobantes");
            CellStyle headerStyle = headerStyle(workbook);
            writeHeader(sheet, headerStyle, "ID Comprobante", "ID Pedido", "Tipo", "Serie", "Correlativo", "Numero", "Estado", "Doc tipo", "Doc numero", "Receptor", "Subtotal", "IGV", "Total", "Fecha emision", "Correo enviado", "Correo destino", "Fecha correo envio", "Correo error");
            int rowIndex = 1;
            int page = 0;
            Page<ComprobanteEntity> block;
            do {
                Pageable pageable = PageRequest.of(page, BLOCK_SIZE, Sort.by(Sort.Direction.DESC, "fechaEmision"));
                block = comprobanteRepository.findAll(pageable);
                for (ComprobanteEntity comprobante : block.getContent()) {
                    Row row = sheet.createRow(rowIndex++);
                    write(row, 0, comprobante.getIdComprobante());
                    write(row, 1, comprobante.getIdPedido());
                    write(row, 2, enumName(comprobante.getTipo()));
                    write(row, 3, comprobante.getSerie());
                    write(row, 4, comprobante.getCorrelativo());
                    write(row, 5, comprobante.getNumeroCompleto());
                    write(row, 6, enumName(comprobante.getEstado()));
                    write(row, 7, enumName(comprobante.getDocReceptorTipo()));
                    write(row, 8, comprobante.getDocReceptorNumero());
                    write(row, 9, comprobante.getRazonSocialReceptor());
                    write(row, 10, comprobante.getSubtotal());
                    write(row, 11, comprobante.getImpuestoTotal());
                    write(row, 12, comprobante.getTotal());
                    write(row, 13, comprobante.getFechaEmision());
                    write(row, 14, Boolean.TRUE.equals(comprobante.getCorreoEnviado()) ? "SI" : "NO");
                    write(row, 15, comprobante.getCorreoDestino());
                    write(row, 16, comprobante.getFechaCorreoEnvio());
                    write(row, 17, comprobante.getCorreoError());
                }
                page++;
            } while (block.hasNext());
            workbook.write(outputStream);
            workbook.dispose();
        }
    }

    @Transactional(readOnly = true)
    public void exportarAuditoria(OutputStream outputStream, String entidad, String accion, String actorTipo) throws IOException {
        String entidadNorm = normalizarFiltro(entidad);
        String accionNorm = normalizarFiltro(accion);
        AuditoriaActorTipo actorTipoNorm = parseActorTipo(actorTipo);

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(100)) {
            Sheet sheet = workbook.createSheet("Auditoria");
            CellStyle headerStyle = headerStyle(workbook);
            writeHeader(sheet, headerStyle, "ID Evento", "Fecha", "Entidad", "Entidad ID", "Accion", "Actor tipo", "ID Actor", "Canal", "Metadata");
            int rowIndex = 1;
            int page = 0;
            Page<AuditoriaEventoEntity> block;
            do {
                Pageable pageable = PageRequest.of(page, BLOCK_SIZE, Sort.by(Sort.Direction.DESC, "idEvento"));
                block = auditoriaPage(entidadNorm, accionNorm, actorTipoNorm, pageable);
                for (AuditoriaEventoEntity evento : block.getContent()) {
                    Row row = sheet.createRow(rowIndex++);
                    write(row, 0, evento.getIdEvento());
                    write(row, 1, evento.getFechaCreacion());
                    write(row, 2, evento.getEntidad());
                    write(row, 3, evento.getEntidadId());
                    write(row, 4, evento.getAccion());
                    write(row, 5, enumName(evento.getActorTipo()));
                    write(row, 6, evento.getIdActor());
                    write(row, 7, enumName(evento.getCanal()));
                    write(row, 8, evento.getMetadataJson());
                }
                page++;
            } while (block.hasNext());
            workbook.write(outputStream);
            workbook.dispose();
        }
    }

    private Page<AuditoriaEventoEntity> auditoriaPage(String entidad, String accion, AuditoriaActorTipo actorTipo, Pageable pageable) {
        if (entidad != null && accion != null && actorTipo != null) {
            return auditoriaEventoRepository.findByEntidadAndAccionAndActorTipo(entidad, accion, actorTipo, pageable);
        }
        if (entidad != null && accion != null) {
            return auditoriaEventoRepository.findByEntidadAndAccion(entidad, accion, pageable);
        }
        if (entidad != null && actorTipo != null) {
            return auditoriaEventoRepository.findByEntidadAndActorTipo(entidad, actorTipo, pageable);
        }
        if (accion != null && actorTipo != null) {
            return auditoriaEventoRepository.findByAccionAndActorTipo(accion, actorTipo, pageable);
        }
        if (entidad != null) {
            return auditoriaEventoRepository.findByEntidad(entidad, pageable);
        }
        if (accion != null) {
            return auditoriaEventoRepository.findByAccion(accion, pageable);
        }
        if (actorTipo != null) {
            return auditoriaEventoRepository.findByActorTipo(actorTipo, pageable);
        }
        return auditoriaEventoRepository.findAll(pageable);
    }

    private void writeHeader(Sheet sheet, CellStyle headerStyle, String... headers) {
        Row header = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, Math.min(Math.max(headers[i].length() + 8, 14), 42) * 256);
        }
    }

    private CellStyle headerStyle(SXSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private void write(Row row, int column, Object value) {
        Cell cell = row.createCell(column);
        if (value == null) {
            cell.setBlank();
        } else if (value instanceof Number number) {
            cell.setCellValue(number.doubleValue());
        } else if (value instanceof BigDecimal decimal) {
            cell.setCellValue(decimal.doubleValue());
        } else if (value instanceof LocalDateTime dateTime) {
            cell.setCellValue(dateTime.toString());
        } else {
            cell.setCellValue(String.valueOf(value));
        }
    }

    private String enumName(Enum<?> value) {
        return value == null ? "" : value.name();
    }

    private String normalizarFiltro(String value) {
        return value == null || value.isBlank() ? null : value.trim().toUpperCase(Locale.ROOT);
    }

    private AuditoriaActorTipo parseActorTipo(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return AuditoriaActorTipo.valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            return null;
        }
    }
}
