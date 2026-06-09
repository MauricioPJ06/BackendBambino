package com.bambino.chatbot.service;

import com.bambino.catalogo.service.CatalogoClienteService;
import com.bambino.carrito.entity.ZonaDeliveryEntity;
import com.bambino.carrito.repository.ZonaDeliveryRepository;
import com.bambino.chatbot.dto.ChatbotConsultaRequest;
import com.bambino.chatbot.dto.ChatbotConsultaResponse;
import com.bambino.chatbot.dto.ChatbotOpcionResponse;
import com.bambino.pedidos.dto.PedidoEstadoHistorialResponse;
import com.bambino.pedidos.dto.PedidoResponse;
import com.bambino.pedidos.service.PedidoEstadoService;
import com.bambino.shared.exception.NegocioException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
/**
 * Clase que maneja la funcionalidad de ChatbotService.
 */
public class ChatbotService {

    private final PedidoEstadoService pedidoEstadoService;
    private final CatalogoClienteService catalogoClienteService;
    private final ZonaDeliveryRepository zonaDeliveryRepository;

    public ChatbotService(PedidoEstadoService pedidoEstadoService,
                          CatalogoClienteService catalogoClienteService,
                          ZonaDeliveryRepository zonaDeliveryRepository) {
        this.pedidoEstadoService = pedidoEstadoService;
        this.catalogoClienteService = catalogoClienteService;
        this.zonaDeliveryRepository = zonaDeliveryRepository;
    }

    /**
     * Metodo que realiza la operacion de listarOpciones.
     * @return resultado de la operacion
     */
    public List<ChatbotOpcionResponse> listarOpciones() {
        return List.of(
            new ChatbotOpcionResponse("MENU", "ver opciones disponibles"),
            new ChatbotOpcionResponse("CATALOGO", "consultar productos activos"),
            new ChatbotOpcionResponse("OFERTAS", "consultar ofertas vigentes"),
            new ChatbotOpcionResponse("HORARIO", "consultar horario de atencion"),
            new ChatbotOpcionResponse("COBERTURA_DELIVERY", "consultar cobertura de delivery"),
            new ChatbotOpcionResponse("MEDIOS_PAGO", "consultar medios y flujo de pago"),
            new ChatbotOpcionResponse("ESTADO_PEDIDO", "consultar estado de un pedido por id o codigo"),
            new ChatbotOpcionResponse("HISTORIAL_PEDIDOS", "consultar historial de pedidos del cliente")
        );
    }

    /**
     * Metodo que realiza la operacion de consultarPublico.
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ChatbotConsultaResponse consultarPublico(ChatbotConsultaRequest request) {
        String opcion = normalizarOpcion(request.opcion());

        return switch (opcion) {
            case "MENU" -> new ChatbotConsultaResponse(
                opcion,
                "puedes consultar horario, cobertura delivery, medios de pago y estado de pedido",
                Map.of("opciones", listarOpciones())
            );
            case "CATALOGO" -> new ChatbotConsultaResponse(
                opcion,
                "catalogo consultado correctamente",
                Map.of("productos", catalogoClienteService.listarProductosWeb())
            );
            case "OFERTAS" -> new ChatbotConsultaResponse(
                opcion,
                "ofertas consultadas correctamente",
                Map.of("ofertas", catalogoClienteService.listarOfertasActivas())
            );
            case "HORARIO" -> new ChatbotConsultaResponse(
                opcion,
                "horario consultado desde zonas activas",
                Map.of("zonas", obtenerHorariosZonas())
            );
            case "COBERTURA_DELIVERY" -> new ChatbotConsultaResponse(
                opcion,
                "cobertura consultada desde zonas activas",
                Map.of("zonas", obtenerCoberturaZonas())
            );
            case "MEDIOS_PAGO" -> new ChatbotConsultaResponse(
                opcion,
                "puedes pagar en linea o contra entrega. antes de pagar eliges boleta o factura",
                Map.of("tiposComprobante", List.of("BOLETA", "FACTURA"))
            );
            case "ESTADO_PEDIDO" -> new ChatbotConsultaResponse(
                opcion,
                "para consultar estado de pedido inicia sesion y usa el endpoint de cliente",
                Map.of("requiereLogin", true)
            );
            case "HISTORIAL_PEDIDOS" -> new ChatbotConsultaResponse(
                opcion,
                "para consultar historial de pedidos inicia sesion y usa el endpoint de cliente",
                Map.of("requiereLogin", true)
            );
            default -> throw new NegocioException("opcion de chatbot no valida");
        };
    }

    /**
     * Metodo que realiza la operacion de consultarCliente.
     * @param emailCliente parametro de entrada para la operacion
     * @param request parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public ChatbotConsultaResponse consultarCliente(String emailCliente, ChatbotConsultaRequest request) {
        String opcion = normalizarOpcion(request.opcion());

        if ("HISTORIAL_PEDIDOS".equals(opcion)) {
            List<PedidoResponse> pedidos = pedidoEstadoService.listarPedidosCliente(emailCliente);
            return new ChatbotConsultaResponse(
                opcion,
                "historial de pedidos consultado correctamente",
                Map.of("pedidos", pedidos)
            );
        }

        if (!"ESTADO_PEDIDO".equals(opcion)) {
            return consultarPublico(request);
        }

        PedidoResponse pedido;
        if (request.idPedido() != null) {
            pedido = pedidoEstadoService.obtenerPedidoCliente(emailCliente, request.idPedido());
        } else if (request.codigoPedido() != null && !request.codigoPedido().isBlank()) {
            pedido = pedidoEstadoService.obtenerPedidoClientePorCodigo(
                emailCliente,
                request.codigoPedido().trim().toUpperCase(Locale.ROOT)
            );
        } else {
            throw new NegocioException("debe enviar idPedido o codigoPedido para consultar estado");
        }

        List<PedidoEstadoHistorialResponse> historial = pedidoEstadoService.historialPedidoCliente(emailCliente, pedido.idPedido());

        return new ChatbotConsultaResponse(
            opcion,
            "estado de pedido consultado correctamente",
            Map.of(
                "idPedido", pedido.idPedido(),
                "codigoPedido", pedido.codigoPedido(),
                "estadoActual", pedido.estadoActual(),
                "total", pedido.total(),
                "modalidad", pedido.modalidad(),
                "tipoComprobante", pedido.tipoComprobante(),
                "fechaCreacion", pedido.fechaCreacion(),
                "historial", historial
            )
        );
    }

    private String normalizarOpcion(String opcion) {
        return opcion == null ? "" : opcion.trim().toUpperCase();
    }

    private List<Map<String, Object>> obtenerHorariosZonas() {
        return zonaDeliveryRepository.findByActivoTrueOrderByTarifaBaseAscIdZonaAsc().stream()
            .map(z -> {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("idZona", z.getIdZona());
                item.put("nombreZona", z.getNombre());
                item.put("horaInicio", z.getHoraInicioAtencion() == null ? null : z.getHoraInicioAtencion().toString());
                item.put("horaFin", z.getHoraFinAtencion() == null ? null : z.getHoraFinAtencion().toString());
                return item;
            })
            .collect(Collectors.toList());
    }

    private List<Map<String, Object>> obtenerCoberturaZonas() {
        return zonaDeliveryRepository.findByActivoTrueOrderByTarifaBaseAscIdZonaAsc().stream()
            .map(z -> Map.<String, Object>of(
                "idZona", z.getIdZona(),
                "nombreZona", z.getNombre(),
                "radioKm", z.getRadioKm(),
                "tarifaBase", z.getTarifaBase(),
                "montoMinimo", z.getMontoMinimo(),
                "descripcion", z.getCoberturaDescripcion()
            ))
            .collect(Collectors.toList());
    }
}
