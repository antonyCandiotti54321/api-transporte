package com.antonycandiotti.api_transporte.test;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ConexionTester {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void probarConexion() {
        try {
            String version = jdbcTemplate.queryForObject("SELECT VERSION()", String.class);
            System.out.println("✅ Conexión exitosa a MySQL en Aiven. Versión: " + version);
        } catch (Exception e) {
            System.err.println("❌ Error al conectar con la base de datos:");
            e.printStackTrace();
        }
    }
}