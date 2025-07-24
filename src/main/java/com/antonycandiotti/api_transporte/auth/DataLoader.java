package com.antonycandiotti.api_transporte.auth;
import com.antonycandiotti.api_transporte.adelantos.Adelanto;
import com.antonycandiotti.api_transporte.adelantos.AdelantoRepository;
import com.antonycandiotti.api_transporte.operarios.Operario;
import com.antonycandiotti.api_transporte.operarios.OperarioRepository;
import com.antonycandiotti.api_transporte.usuarios.Rol;
import com.antonycandiotti.api_transporte.usuarios.Usuario;
import com.antonycandiotti.api_transporte.usuarios.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;
    private final OperarioRepository operarioRepository;
    private final AdelantoRepository adelantoRepository;

    @Bean
    CommandLineRunner initData() {
        return args -> {
            crearAdminSiNoExiste();
            crearChoferesSiFaltan();
            crearOperariosSiFaltan();
            crearAdelantosSiFaltan();
        };
    }

    private void crearAdminSiNoExiste() {
        if (usuarioRepository.countByRol(Rol.ADMIN) == 0) {
            crearUsuario("admin", "admin", "admin", Rol.ADMIN);
        }
    }

    private void crearChoferesSiFaltan() {
        if (usuarioRepository.countByRol(Rol.CHOFER) < 2) {
            crearUsuario("luis", "123", "Luis Pérez", Rol.CHOFER);
            crearUsuario("ana", "123", "Ana Ruiz", Rol.CHOFER);
        }
    }

    private void crearOperariosSiFaltan() {
        if (operarioRepository.count() < 2) {
            crearOperario("Antony Candiotti");
            crearOperario("Kelly Uwarai");
        }
    }

    private Usuario crearUsuario(String username, String password, String nombre, Rol rol) {
        return usuarioRepository.findByUsername(username).orElseGet(() -> {
            RegisterRequest request = RegisterRequest.builder()
                    .username(username)
                    .password(password)
                    .nombreCompleto(nombre)
                    .rol(rol)
                    .build();
            authService.register(request);
            return usuarioRepository.findByUsername(username).get();
        });
    }

    private void crearOperario(String nombreCompleto) {
        if (!operarioRepository.existsByNombreCompleto(nombreCompleto)) {
            Operario operario = Operario.builder().nombreCompleto(nombreCompleto).build();
            operarioRepository.save(operario);
        }
    }

    private void crearAdelantosSiFaltan() {
        if (adelantoRepository.count() > 0) return;

        Usuario luis = usuarioRepository.findByUsername("luis").orElseThrow();
        Usuario ana = usuarioRepository.findByUsername("ana").orElseThrow();
        Operario antony = operarioRepository.findByNombreCompleto("Antony Candiotti").orElseThrow();
        Operario kelly = operarioRepository.findByNombreCompleto("Kelly Uwarai").orElseThrow();

        ZoneId limaZone = ZoneId.of("America/Lima");

        // Semana actual (ahora)
        ZonedDateTime semanaActual = ZonedDateTime.now(limaZone).with(DayOfWeek.MONDAY).withHour(10);
        crearAdelanto(luis, antony, 100.0, "Semana actual 1", semanaActual.plusDays(0));
        crearAdelanto(luis, antony, 150.0, "Semana actual 2", semanaActual.plusDays(1));
        crearAdelanto(ana, kelly, 80.0, "Semana actual 3", semanaActual.plusDays(2));

        // Semana anterior
        ZonedDateTime semanaAnterior = semanaActual.minusWeeks(1);
        crearAdelanto(ana, kelly, 120.0, "Semana anterior 1", semanaAnterior.plusDays(0));
        crearAdelanto(ana, kelly, 130.0, "Semana anterior 2", semanaAnterior.plusDays(1));
        crearAdelanto(luis, antony, 90.0, "Semana anterior 3", semanaAnterior.plusDays(2));

        // Dos semanas atrás
        ZonedDateTime semanaMasAntigua = semanaActual.minusWeeks(2);
        crearAdelanto(ana, antony, 110.0, "2 semanas atrás 1", semanaMasAntigua.plusDays(0));
        crearAdelanto(ana, antony, 95.0, "2 semanas atrás 2", semanaMasAntigua.plusDays(1));
    }

    private void crearAdelanto(Usuario creador, Operario operario, Double cantidad, String mensaje, ZonedDateTime fechaHora) {
        Adelanto adelanto = Adelanto.builder()
                .usuario(creador)
                .operario(operario)
                .cantidad(cantidad)
                .mensaje(mensaje)
                .fechaHora(fechaHora)
                .build();
        adelantoRepository.save(adelanto);
        System.out.println("Adelanto creado: " + mensaje + " por " + creador.getNombreCompleto());
    }
}