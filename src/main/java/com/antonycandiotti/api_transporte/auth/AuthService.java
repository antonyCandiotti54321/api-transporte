package com.antonycandiotti.api_transporte.auth;


import com.antonycandiotti.api_transporte.jwt.JwtService;
import com.antonycandiotti.api_transporte.usuarios.Rol;
import com.antonycandiotti.api_transporte.usuarios.Usuario;
import com.antonycandiotti.api_transporte.usuarios.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;

    public AuthResponse login(LoginRequest request) {
        // 1. Autenticación
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException ex) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        // 2. Cargar Usuario completo
        Usuario usuario = usuarioRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // 3. Extraer datos
        Long idUsuario = usuario.getId();
        String nombreCompleto = usuario.getNombreCompleto();
        Rol rol = usuario.getRol();

        // 4. Generar token
        String token = jwtService.getToken(usuario);

        // 5. Construir respuesta
        return AuthResponse.builder()
                .token(token)
                .idUsuario(idUsuario)
                .nombreCompleto(nombreCompleto)
                .rol(rol) // O usar rol.toString()
                .build();
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // 1. Crear el Usuario
        Usuario usuario = Usuario.builder()
                .nombreCompleto(request.getNombreCompleto())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(request.getRol()) // Suponiendo que viene un Rol directamente (enum)
                .build();

        usuario = usuarioRepository.save(usuario);

        // 2. Generar token
        String token = jwtService.getToken(usuario);

        // 3. Devolver AuthResponse
        return AuthResponse.builder()
                .token(token)
                .idUsuario(usuario.getId())
                .nombreCompleto(usuario.getNombreCompleto())
                .rol(usuario.getRol())
                .build();
    }
}
