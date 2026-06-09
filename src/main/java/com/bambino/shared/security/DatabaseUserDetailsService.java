package com.bambino.shared.security;

import com.bambino.seguridad.entity.EstadoUsuario;
import com.bambino.seguridad.entity.UsuarioEntity;
import com.bambino.seguridad.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
/**
 * Clase que maneja la funcionalidad de DatabaseUserDetailsService.
 */
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public DatabaseUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    /**
     * Metodo que realiza la operacion de loadUserByUsername.
     * @param username parametro de entrada para la operacion
     * @return resultado de la operacion
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String email = username == null ? "" : username.trim().toLowerCase(Locale.ROOT);

        UsuarioEntity usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("usuario no encontrado"));

        String rolNombre = usuario.getRol() != null ? usuario.getRol().getNombre() : null;
        if (rolNombre == null || rolNombre.isBlank()) {
            throw new UsernameNotFoundException("usuario sin rol");
        }

        boolean enabled = EstadoUsuario.ACTIVO.equals(usuario.getEstado());
        List<SimpleGrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority("ROLE_" + rolNombre.trim().toUpperCase(Locale.ROOT))
        );

        return User.withUsername(usuario.getEmail())
            .password(usuario.getPasswordHash())
            .authorities(authorities)
            .disabled(!enabled)
            .build();
    }
}
