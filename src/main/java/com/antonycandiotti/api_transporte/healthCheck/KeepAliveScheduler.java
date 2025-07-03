package com.antonycandiotti.api_transporte.healthCheck;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KeepAliveScheduler {

    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void initPing() {
        pingDatabase("Inicio");
    }

    // 🔁 Ejecutar cada 4 horas (segundo, minuto, hora, día, mes, díaSemana)
    @Scheduled(cron = "0 0 */4 * * *")
    public void scheduledPing() {
        pingDatabase("Cada 4 horas");
    }

    private void pingDatabase(String motivo) {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            System.out.println("✔ Ping a la base de datos exitoso (" + motivo + ")");
        } catch (Exception e) {
            System.err.println("✖ Error al hacer ping a la BD (" + motivo + "): " + e.getMessage());
        }
    }
}