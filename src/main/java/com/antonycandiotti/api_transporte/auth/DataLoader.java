package com.antonycandiotti.api_transporte.auth;
import com.antonycandiotti.api_transporte.usuarios.Rol;
import com.antonycandiotti.api_transporte.usuarios.Usuario;
import com.antonycandiotti.api_transporte.usuarios.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;

    @Bean
    CommandLineRunner initData() {
        return args -> {
            // Crear usuarios si no existen
            crearUsuario("luis", "123", "Luis Pérez", Rol.CHOFER);
            crearUsuario("ana", "123", "Ana Ruiz", Rol.CHOFER);
            crearUsuario("pedro", "123", "Pedro Díaz", Rol.ADMIN);
        };
    }

    private Usuario crearUsuario(String username, String password, String nombre, Rol rol) {
        return usuarioRepository.findByUsername(username).map(usuario -> {
            System.out.println("Usuario '" + username + "' ya existe.");
            return usuario;
        }).orElseGet(() -> {
            RegisterRequest request = RegisterRequest.builder()
                    .username(username)
                    .password(password)
                    .nombreCompleto(nombre)
                    .rol(rol)
                    .build();
            authService.register(request);
            Usuario nuevo = usuarioRepository.findByUsername(username).get();
            System.out.println("Usuario creado: " + nuevo.getNombreCompleto() + " (" + nuevo.getUsername() + ")");
            return nuevo;
        });
    }
}
