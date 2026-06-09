package com.bambino.delivery.service;

import com.bambino.carrito.entity.ZonaDeliveryEntity;
import com.bambino.carrito.repository.ZonaDeliveryRepository;
import com.bambino.delivery.dto.CoberturaDeliveryResponse;
import com.bambino.delivery.dto.UbicacionRestauranteResponse;
import com.bambino.shared.exception.NegocioException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
/**
 * Clase que maneja la funcionalidad de CoberturaDeliveryService.
 */
public class CoberturaDeliveryService {

    private static final double RADIO_TIERRA_KM = 6371.0088;

    private final ZonaDeliveryRepository zonaDeliveryRepository;

    public CoberturaDeliveryService(ZonaDeliveryRepository zonaDeliveryRepository) {
        this.zonaDeliveryRepository = zonaDeliveryRepository;
    }

    /**
     * Metodo que realiza la operacion de evaluarCobertura.
     * @param latitudCliente parametro de entrada para la operacion
     * @param longitudCliente parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public CoberturaDeliveryResponse evaluarCobertura(BigDecimal latitudCliente, BigDecimal longitudCliente) {
        List<ZonaDeliveryEntity> zonasActivas = zonaDeliveryRepository.findByActivoTrueOrderByTarifaBaseAscIdZonaAsc();
        if (zonasActivas.isEmpty()) {
            throw new NegocioException("no existen zonas delivery activas configuradas");
        }

        BigDecimal mejorDistancia = null;
        ZonaDeliveryEntity zonaMasCercana = null;
        ZonaDeliveryEntity zonaCobertura = null;

        for (ZonaDeliveryEntity zona : zonasActivas) {
            if (zona.getLatitudCentro() == null || zona.getLongitudCentro() == null || zona.getRadioKm() == null) {
                throw new NegocioException("zona delivery activa sin coordenadas o radio configurado");
            }

            double latCentro = zona.getLatitudCentro().doubleValue();
            double lonCentro = zona.getLongitudCentro().doubleValue();
            BigDecimal radioZona = zona.getRadioKm();
            BigDecimal distancia = calcularDistanciaKm(latCentro, lonCentro, latitudCliente.doubleValue(), longitudCliente.doubleValue());

            if (mejorDistancia == null || distancia.compareTo(mejorDistancia) < 0) {
                mejorDistancia = distancia;
                zonaMasCercana = zona;
            }
            if (distancia.compareTo(radioZona) <= 0) {
                if (zonaCobertura == null || distancia.compareTo(calcularDistanciaKm(
                    zonaCobertura.getLatitudCentro().doubleValue(),
                    zonaCobertura.getLongitudCentro().doubleValue(),
                    latitudCliente.doubleValue(),
                    longitudCliente.doubleValue()
                )) < 0) {
                    zonaCobertura = zona;
                }
            }
        }

        ZonaDeliveryEntity zonaAplicada = zonaCobertura != null ? zonaCobertura : zonaMasCercana;
        boolean dentro = zonaCobertura != null;
        return new CoberturaDeliveryResponse(
            zonaAplicada == null ? null : zonaAplicada.getIdZona(),
            dentro,
            mejorDistancia,
            zonaAplicada == null ? null : zonaAplicada.getRadioKm(),
            zonaAplicada == null ? null : zonaAplicada.getTarifaBase(),
            zonaAplicada == null ? null : zonaAplicada.getMontoMinimo(),
            dentro ? "punto dentro de cobertura" : "punto fuera de cobertura"
        );
    }

    /**
     * Metodo que realiza la operacion de obtenerUbicacionPrincipal.
     * @return resultado de la operacion
     */
    public UbicacionRestauranteResponse obtenerUbicacionPrincipal() {
        ZonaDeliveryEntity zona = zonaDeliveryRepository.findByActivoTrueOrderByTarifaBaseAscIdZonaAsc()
            .stream()
            .findFirst()
            .orElseThrow(() -> new NegocioException("no existen zonas delivery activas configuradas"));

        if (zona.getLatitudCentro() == null || zona.getLongitudCentro() == null) {
            throw new NegocioException("la zona principal no tiene coordenadas configuradas");
        }

        return new UbicacionRestauranteResponse(
            zona.getIdZona(),
            zona.getNombre(),
            zona.getLatitudCentro(),
            zona.getLongitudCentro(),
            zona.getMapaEmbedUrl()
        );
    }

    /**
     * Metodo que realiza la operacion de calcularDistanciaKm.
     * @param latitudDestino parametro de entrada para la operacion
     * @param longitudDestino parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public BigDecimal calcularDistanciaKm(double latitudDestino, double longitudDestino) {
        throw new UnsupportedOperationException("use calcularDistanciaKm(origen,destino) con coordenadas explicitas");
    }

    /**
     * Metodo que realiza la operacion de calcularDistanciaKm.
     * @param latitudOrigen parametro de entrada para la operacion
     * @param longitudOrigen parametro de entrada para la operacion
     * @param latitudDestino parametro de entrada para la operacion
     * @param longitudDestino parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public BigDecimal calcularDistanciaKm(double latitudOrigen, double longitudOrigen, double latitudDestino, double longitudDestino) {
        double lat1 = Math.toRadians(latitudOrigen);
        double lon1 = Math.toRadians(longitudOrigen);
        double lat2 = Math.toRadians(latitudDestino);
        double lon2 = Math.toRadians(longitudDestino);

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.pow(Math.sin(dLat / 2), 2)
            + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLon / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return BigDecimal.valueOf(RADIO_TIERRA_KM * c).setScale(3, RoundingMode.HALF_UP);
    }
}
