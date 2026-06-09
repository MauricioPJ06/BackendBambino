package com.bambino.seguridad.service;

import com.bambino.auditoria.service.AuditoriaService;
import com.bambino.clientes.repository.ClienteDocumentoRepository;
import com.bambino.clientes.repository.ClientePerfilRepository;
import com.bambino.documentos.dto.ConsultaDocumentoResponse;
import com.bambino.documentos.service.DocumentoConsultaService;
import com.bambino.seguridad.dto.RegistroClienteDocumentoRequest;
import com.bambino.seguridad.dto.RegistroClienteRequest;
import com.bambino.seguridad.entity.RolEntity;
import com.bambino.seguridad.entity.UsuarioEntity;
import com.bambino.seguridad.repository.RecuperacionPasswordCodigoRepository;
import com.bambino.seguridad.repository.RolRepository;
import com.bambino.seguridad.repository.UsuarioRepository;
import com.bambino.shared.exception.IntegracionExternaException;
import com.bambino.shared.exception.NegocioException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthClienteServiceTest {

    @Test
    void registrarClienteBloqueaDniNoExistenteEnFactiliza() {
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        RolRepository rolRepository = mock(RolRepository.class);
        ClientePerfilRepository clientePerfilRepository = mock(ClientePerfilRepository.class);
        ClienteDocumentoRepository clienteDocumentoRepository = mock(ClienteDocumentoRepository.class);
        RecuperacionPasswordCodigoRepository recuperacionRepository = mock(RecuperacionPasswordCodigoRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        CorreoRecuperacionService correoRecuperacionService = mock(CorreoRecuperacionService.class);
        AuditoriaService auditoriaService = mock(AuditoriaService.class);
        DocumentoConsultaService documentoConsultaService = mock(DocumentoConsultaService.class);
        AuthClienteService service = new AuthClienteService(
            usuarioRepository,
            rolRepository,
            clientePerfilRepository,
            clienteDocumentoRepository,
            recuperacionRepository,
            passwordEncoder,
            correoRecuperacionService,
            auditoriaService,
            documentoConsultaService
        );
        when(usuarioRepository.existsByEmail("cliente@gmail.com")).thenReturn(false);
        when(rolRepository.findByNombre("CLIENTE")).thenReturn(Optional.of(rolCliente()));
        when(documentoConsultaService.consultar("11111111"))
            .thenThrow(new IntegracionExternaException(HttpStatus.NOT_FOUND, "No se encontraron datos para el documento ingresado."));

        assertThrows(IntegracionExternaException.class, () -> service.registrarCliente(requestDni("11111111")));

        verify(usuarioRepository, never()).save(any());
        verify(clientePerfilRepository, never()).save(any());
        verify(clienteDocumentoRepository, never()).save(any());
    }

    @Test
    void registrarClienteGuardaCuandoFactilizaConfirmaDni() {
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        RolRepository rolRepository = mock(RolRepository.class);
        ClientePerfilRepository clientePerfilRepository = mock(ClientePerfilRepository.class);
        ClienteDocumentoRepository clienteDocumentoRepository = mock(ClienteDocumentoRepository.class);
        RecuperacionPasswordCodigoRepository recuperacionRepository = mock(RecuperacionPasswordCodigoRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        CorreoRecuperacionService correoRecuperacionService = mock(CorreoRecuperacionService.class);
        AuditoriaService auditoriaService = mock(AuditoriaService.class);
        DocumentoConsultaService documentoConsultaService = mock(DocumentoConsultaService.class);
        AuthClienteService service = new AuthClienteService(
            usuarioRepository,
            rolRepository,
            clientePerfilRepository,
            clienteDocumentoRepository,
            recuperacionRepository,
            passwordEncoder,
            correoRecuperacionService,
            auditoriaService,
            documentoConsultaService
        );
        when(usuarioRepository.existsByEmail("cliente@gmail.com")).thenReturn(false);
        when(rolRepository.findByNombre("CLIENTE")).thenReturn(Optional.of(rolCliente()));
        when(passwordEncoder.encode("Password123")).thenReturn("hash");
        when(documentoConsultaService.consultar("12345678")).thenReturn(new ConsultaDocumentoResponse(
            "DNI",
            "12345678",
            "JUAN PEREZ",
            "JUAN",
            "PEREZ",
            "",
            null,
            null,
            null,
            null,
            null,
            null,
            null
        ));
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenAnswer(invocation -> {
            UsuarioEntity usuario = invocation.getArgument(0);
            usuario.setIdUsuario(10L);
            return usuario;
        });

        service.registrarCliente(requestDni("12345678"));

        verify(usuarioRepository).save(any(UsuarioEntity.class));
        verify(clientePerfilRepository).save(any());
        verify(clienteDocumentoRepository).save(any());
    }

    @Test
    void registrarClienteBloqueaDocumentoDuplicadoAntesDeGuardar() {
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        RolRepository rolRepository = mock(RolRepository.class);
        ClientePerfilRepository clientePerfilRepository = mock(ClientePerfilRepository.class);
        ClienteDocumentoRepository clienteDocumentoRepository = mock(ClienteDocumentoRepository.class);
        RecuperacionPasswordCodigoRepository recuperacionRepository = mock(RecuperacionPasswordCodigoRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        CorreoRecuperacionService correoRecuperacionService = mock(CorreoRecuperacionService.class);
        AuditoriaService auditoriaService = mock(AuditoriaService.class);
        DocumentoConsultaService documentoConsultaService = mock(DocumentoConsultaService.class);
        AuthClienteService service = new AuthClienteService(
            usuarioRepository,
            rolRepository,
            clientePerfilRepository,
            clienteDocumentoRepository,
            recuperacionRepository,
            passwordEncoder,
            correoRecuperacionService,
            auditoriaService,
            documentoConsultaService
        );
        when(usuarioRepository.existsByEmail("cliente@gmail.com")).thenReturn(false);
        when(rolRepository.findByNombre("CLIENTE")).thenReturn(Optional.of(rolCliente()));
        when(documentoConsultaService.consultar("11111111")).thenReturn(new ConsultaDocumentoResponse(
            "DNI",
            "11111111",
            "JUAN PEREZ",
            "JUAN",
            "PEREZ",
            "",
            null,
            null,
            null,
            null,
            null,
            null,
            null
        ));
        when(usuarioRepository.existsByDocumento("11111111")).thenReturn(true);

        assertThrows(NegocioException.class, () -> service.registrarCliente(requestDni("11111111")));

        verify(usuarioRepository, never()).save(any());
        verify(clientePerfilRepository, never()).save(any());
        verify(clienteDocumentoRepository, never()).save(any());
    }

    private RegistroClienteRequest requestDni(String dni) {
        return new RegistroClienteRequest(
            "cliente@gmail.com",
            "Password123",
            "Juan",
            "Perez",
            "987654321",
            "DNI",
            dni,
            List.of(new RegistroClienteDocumentoRequest("DNI", dni))
        );
    }

    private RolEntity rolCliente() {
        RolEntity rol = new RolEntity();
        rol.setIdRol((short) 1);
        rol.setNombre("CLIENTE");
        return rol;
    }
}
