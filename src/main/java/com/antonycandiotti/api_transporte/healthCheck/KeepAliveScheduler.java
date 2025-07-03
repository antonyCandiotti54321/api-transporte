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

    // Ejecuta cada 6 horas (cron: segundo, minuto, hora, día, mes, díaSemana)
    @Scheduled(cron = "0 0 */6 * * *")
    public void pingDatabase() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            System.out.println("Ping a la base de datos exitoso");
        } catch (Exception e) {
            System.err.println("Error al hacer ping a la BD: " + e.getMessage());
        }
    }
}