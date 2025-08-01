package com.antonycandiotti.api_transporte.database;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseMemoryUsageResponse {
    private String databaseName;
    private String totalSizeMB;
    private String totalSizeGB;
}