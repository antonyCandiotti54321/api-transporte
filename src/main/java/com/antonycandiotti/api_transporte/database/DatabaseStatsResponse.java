package com.antonycandiotti.api_transporte.database;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseStatsResponse {

    // Información general de la base de datos
    private String databaseName;
    private String version;

    // Tamaño de la base de datos
    private String totalSizeMB;
    private String totalSizeGB;

    // Información por tabla
    private java.util.List<TableInfo> tables;

    // Estadísticas de memoria
    private String bufferPoolSize;
    private String innodbBufferPoolSize;

    // Información adicional
    private Long totalRows;
    private String charset;
    private String collation;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TableInfo {
        private String tableName;
        private String sizeMB;
        private Long rows;
        private String engine;
        private String collation;
    }
}