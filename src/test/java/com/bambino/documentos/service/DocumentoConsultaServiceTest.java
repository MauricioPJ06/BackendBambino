package com.bambino.documentos.service;

import com.bambino.documentos.dto.ConsultaDocumentoResponse;
import com.bambino.documentos.service.factiliza.FactilizaClient;
import com.bambino.documentos.service.factiliza.FactilizaDniData;
import com.bambino.documentos.service.factiliza.FactilizaResponse;
import com.bambino.documentos.service.factiliza.FactilizaRucData;
import com.bambino.shared.exception.NegocioException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DocumentoConsultaServiceTest {

    @Test
    void consultaDniNormalizaDocumentoYMapeaRespuestaFactiliza() {
        FactilizaClient factilizaClient = mock(FactilizaClient.class);
        when(factilizaClient.consultarDni("12345678")).thenReturn(new FactilizaResponse<>(
            200,
            true,
            "ok",
            new FactilizaDniData(
                "12345678",
                "JUAN",
                "PEREZ",
                "ROJAS",
                "JUAN PEREZ ROJAS",
                "LIMA",
                "LIMA",
                "CHORRILLOS",
                "AV. TEST 123",
                "AV. TEST 123, CHORRILLOS"
            )
        ));

        DocumentoConsultaService service = new DocumentoConsultaService(factilizaClient);

        ConsultaDocumentoResponse response = service.consultar("12-345-678");

        assertThat(response.tipoDocumento()).isEqualTo("DNI");
        assertThat(response.numeroDocumento()).isEqualTo("12345678");
        assertThat(response.nombreORazonSocial()).isEqualTo("JUAN PEREZ ROJAS");
        assertThat(response.apellidoPaterno()).isEqualTo("PEREZ");
        assertThat(response.distrito()).isEqualTo("CHORRILLOS");
    }

    @Test
    void consultaRucMapeaDatosTributarios() {
        FactilizaClient factilizaClient = mock(FactilizaClient.class);
        when(factilizaClient.consultarRuc("20123456789")).thenReturn(new FactilizaResponse<>(
            200,
            true,
            "ok",
            new FactilizaRucData(
                "20123456789",
                "EMPRESA TEST SAC",
                "SOCIEDAD ANONIMA CERRADA",
                "ACTIVO",
                "HABIDO",
                "LIMA",
                "LIMA",
                "SURCO",
                "CALLE TEST 456",
                "CALLE TEST 456, SURCO"
            )
        ));

        DocumentoConsultaService service = new DocumentoConsultaService(factilizaClient);

        ConsultaDocumentoResponse response = service.consultar("20123456789");

        assertThat(response.tipoDocumento()).isEqualTo("RUC");
        assertThat(response.numeroDocumento()).isEqualTo("20123456789");
        assertThat(response.nombreORazonSocial()).isEqualTo("EMPRESA TEST SAC");
        assertThat(response.estadoContribuyente()).isEqualTo("ACTIVO");
        assertThat(response.condicionContribuyente()).isEqualTo("HABIDO");
    }

    @Test
    void rechazaDocumentoQueNoEsDniNiRuc() {
        DocumentoConsultaService service = new DocumentoConsultaService(mock(FactilizaClient.class));

        assertThatThrownBy(() -> service.consultar("12345"))
            .isInstanceOf(NegocioException.class)
            .hasMessage("documento debe ser DNI de 8 digitos o RUC de 11 digitos");
    }
}
