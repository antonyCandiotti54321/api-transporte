package com.antonycandiotti.api_transporte.auth;


import com.antonycandiotti.api_transporte.jwt.JwtService;
import com.antonycandiotti.api_transporte.usuarios.Rol;
import com.antonycandiotti.api_transporte.usuarios.Usuario;
import com.antonycandiotti.api_transporte.usuarios.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;


    public AuthResponse login(LoginRequest request) {

        System.out.println("➡️ Iniciando login para usuario: " + request.getUsername());

        // 1. Verificar si existe el usuario

        Usuario usuario = usuarioRepository

                .findByUsername(request.getUsername())

                .orElse(null);

        if (usuario == null) {

            System.out.println("❌ Usuario no encontrado: " + request.getUsername());

            throw new UsernameNotFoundException("El usuario no existe");

        }

        System.out.println("✅ Usuario encontrado: " + usuario.getUsername());

        // 2. Verificar contraseña manualmente

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {

            System.out.println("❌ Contraseña incorrecta para usuario: " + request.getUsername());

            throw new BadCredentialsException("Contraseña incorrecta");

        }

        System.out.println("✅ Contraseña correcta");

        // 3. Extraer datos

        Long idUsuario = usuario.getId();

        String nombreCompleto = usuario.getNombreCompleto();

        Rol rol = usuario.getRol();

        System.out.println("📦 Datos extraídos: ID=" + idUsuario + ", nombre=" + nombreCompleto + ", rol=" + rol);

        // 4. Generar token

        String token = jwtService.getToken(usuario);

        System.out.println("🔑 Token generado exitosamente");

        // 5. Construir respuesta

        AuthResponse response = AuthResponse.builder()

                .token(token)

                .idUsuario(idUsuario)

                .nombreCompleto(nombreCompleto)

                .rol(rol)

                .build();

        System.out.println("✅ Login exitoso para " + request.getUsername());

        return response;

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
