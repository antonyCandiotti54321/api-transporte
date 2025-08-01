package com.antonycandiotti.api_transporte.database;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DatabaseStatsService {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseStatsResponse getDatabaseStats() {
        try {
            // Obtener nombre de la base de datos actual
            String databaseName = getCurrentDatabaseName();

            // Obtener versión de MySQL
            String version = getMySQLVersion();

            // Obtener tamaño total de la base de datos
            Map<String, Object> sizeInfo = getTotalDatabaseSize(databaseName);

            // Obtener información de las tablas
            List<DatabaseStatsResponse.TableInfo> tables = getTablesInfo(databaseName);

            // Obtener información del buffer pool
            String bufferPoolSize = getBufferPoolSize();
            String innodbBufferPoolSize = getInnodbBufferPoolSize();

            // Calcular total de filas
            Long totalRows = tables.stream()
                    .mapToLong(table -> table.getRows() != null ? table.getRows() : 0L)
                    .sum();

            // Obtener charset y collation de la base de datos
            Map<String, String> dbInfo = getDatabaseCharsetCollation(databaseName);

            return DatabaseStatsResponse.builder()
                    .databaseName(databaseName)
                    .version(version)
                    .totalSizeMB(sizeInfo.get("size_mb").toString())
                    .totalSizeGB(sizeInfo.get("size_gb").toString())
                    .tables(tables)
                    .bufferPoolSize(bufferPoolSize)
                    .innodbBufferPoolSize(innodbBufferPoolSize)
                    .totalRows(totalRows)
                    .charset(dbInfo.get("charset"))
                    .collation(dbInfo.get("collation"))
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener estadísticas de la base de datos: " + e.getMessage());
        }
    }

    private String getCurrentDatabaseName() {
        return jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
    }

    private String getMySQLVersion() {
        return jdbcTemplate.queryForObject("SELECT VERSION()", String.class);
    }

    private Map<String, Object> getTotalDatabaseSize(String databaseName) {
        String query = """
            SELECT 
                ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS size_mb,
                ROUND(SUM(data_length + index_length) / 1024 / 1024 / 1024, 4) AS size_gb
            FROM information_schema.tables 
            WHERE table_schema = ?
            """;

        return jdbcTemplate.queryForMap(query, databaseName);
    }

    private List<DatabaseStatsResponse.TableInfo> getTablesInfo(String databaseName) {
        String query = """
            SELECT 
                table_name,
                ROUND(((data_length + index_length) / 1024 / 1024), 2) AS size_mb,
                table_rows,
                engine,
                table_collation
            FROM information_schema.tables
            WHERE table_schema = ?
            ORDER BY (data_length + index_length) DESC
            """;

        RowMapper<DatabaseStatsResponse.TableInfo> rowMapper = (rs, rowNum) ->
                DatabaseStatsResponse.TableInfo.builder()
                        .tableName(rs.getString("table_name"))
                        .sizeMB(rs.getString("size_mb"))
                        .rows(rs.getLong("table_rows"))
                        .engine(rs.getString("engine"))
                        .collation(rs.getString("table_collation"))
                        .build();

        return jdbcTemplate.query(query, rowMapper, databaseName);
    }

    private String getBufferPoolSize() {
        try {
            String query = "SHOW STATUS LIKE 'Innodb_buffer_pool_size'";
            Map<String, Object> result = jdbcTemplate.queryForMap(query);
            Object value = result.get("Value");

            if (value != null) {
                long bytes = Long.parseLong(value.toString());
                BigDecimal mb = BigDecimal.valueOf(bytes).divide(BigDecimal.valueOf(1024 * 1024), 2, RoundingMode.HALF_UP);
                return mb + " MB";
            }
            return "No disponible";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String getInnodbBufferPoolSize() {
        try {
            String query = "SHOW VARIABLES LIKE 'innodb_buffer_pool_size'";
            Map<String, Object> result = jdbcTemplate.queryForMap(query);
            Object value = result.get("Value");

            if (value != null) {
                long bytes = Long.parseLong(value.toString());
                BigDecimal mb = BigDecimal.valueOf(bytes).divide(BigDecimal.valueOf(1024 * 1024), 2, RoundingMode.HALF_UP);
                return mb + " MB";
            }
            return "No disponible";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private Map<String, String> getDatabaseCharsetCollation(String databaseName) {
        try {
            String query = """
            SELECT 
                default_character_set_name as charset,
                default_collation_name as collation
            FROM information_schema.schemata 
            WHERE schema_name = ?
            """;

            Map<String, Object> result = jdbcTemplate.queryForMap(query, databaseName);

            return Map.of(
                    "charset", result.get("charset").toString(),
                    "collation", result.get("collation").toString()
            );

        } catch (Exception e) {
            return Map.of(
                    "charset", "Error: " + e.getMessage(),
                    "collation", "Error: " + e.getMessage()
            );
        }
    }


    public DatabaseMemoryUsageResponse getSimplifiedMemoryUsage() {
        try {
            String databaseName = getCurrentDatabaseName();
            Map<String, Object> sizeInfo = getTotalDatabaseSize(databaseName);

            return DatabaseMemoryUsageResponse.builder()
                    .databaseName(databaseName)
                    .totalSizeMB(sizeInfo.get("sizeMB").toString())
                    .totalSizeGB(sizeInfo.get("sizeGB").toString())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener uso de memoria: " + e.getMessage());
        }
    }
}