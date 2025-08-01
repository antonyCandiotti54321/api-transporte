package com.antonycandiotti.api_transporte.database;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/database")
@RequiredArgsConstructor
public class DatabaseStatsController {

    private final DatabaseStatsService databaseStatsService;

    /**
     * Endpoint completo con todas las estadísticas de la base de datos
     */
    @GetMapping("/stats")
    public ResponseEntity<DatabaseStatsResponse> getDatabaseStats() {
        try {
            DatabaseStatsResponse stats = databaseStatsService.getDatabaseStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener estadísticas de la base de datos: " + e.getMessage());
        }
    }

    /**
     * Endpoint simplificado solo con información de memoria/tamaño
     */
    @GetMapping("/memory-usage")
    public ResponseEntity<DatabaseMemoryUsageResponse> getMemoryUsage() {
        try {
            DatabaseMemoryUsageResponse memoryUsage = databaseStatsService.getSimplifiedMemoryUsage();
            return ResponseEntity.ok(memoryUsage);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener uso de memoria: " + e.getMessage());
        }
    }
}