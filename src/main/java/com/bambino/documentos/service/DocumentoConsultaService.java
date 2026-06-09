package com.bambino.documentos.service;

import com.bambino.documentos.dto.ConsultaDocumentoResponse;
import com.bambino.documentos.service.factiliza.FactilizaClient;
import com.bambino.documentos.service.factiliza.FactilizaDniData;
import com.bambino.documentos.service.factiliza.FactilizaResponse;
import com.bambino.documentos.service.factiliza.FactilizaRucData;
import com.bambino.shared.exception.IntegracionExternaException;
import com.bambino.shared.exception.NegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DocumentoConsultaService {

    private final FactilizaClient factilizaClient;

    public DocumentoConsultaService(FactilizaClient factilizaClient) {
        this.factilizaClient = factilizaClient;
    }

    public ConsultaDocumentoResponse consultar(String documento) {
        String normalizado = normalizar(documento);
        if (normalizado.length() == 8) {
            FactilizaResponse<FactilizaDniData> response = factilizaClient.consultarDni(normalizado);
            FactilizaDniData data = response.data();
            if (data == null || isBlank(data.numero())) {
                throw new IntegracionExternaException(HttpStatus.NOT_FOUND, "No se encontraron datos para el documento ingresado.");
            }
            return new ConsultaDocumentoResponse(
                "DNI",
                data.numero(),
                nvl(data.nombreCompleto()),
                data.nombres(),
                data.apellidoPaterno(),
                data.apellidoMaterno(),
                data.direccion(),
                data.direccionCompleta(),
                data.departamento(),
                data.provincia(),
                data.distrito(),
                null,
                null
            );
        }

        if (normalizado.length() == 11) {
            FactilizaResponse<FactilizaRucData> response = factilizaClient.consultarRuc(normalizado);
            FactilizaRucData data = response.data();
            if (data == null || isBlank(data.numero())) {
                throw new IntegracionExternaException(HttpStatus.NOT_FOUND, "No se encontraron datos para el documento ingresado.");
            }
            return new ConsultaDocumentoResponse(
                "RUC",
                data.numero(),
                nvl(data.nombreORazonSocial()),
                null,
                null,
                null,
                data.direccion(),
                data.direccionCompleta(),
                data.departamento(),
                data.provincia(),
                data.distrito(),
                data.estado(),
                data.condicion()
            );
        }

        throw new NegocioException("documento debe ser DNI de 8 digitos o RUC de 11 digitos");
    }

    private String normalizar(String documento) {
        if (documento == null) {
            return "";
        }
        return documento.replaceAll("\\D", "");
    }

    private String nvl(String value) {
        return value == null ? "" : value;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
