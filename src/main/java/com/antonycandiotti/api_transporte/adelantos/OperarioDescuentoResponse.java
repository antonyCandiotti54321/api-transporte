package com.antonycandiotti.api_transporte.adelantos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OperarioDescuentoResponse {
    private Long operarioId;
    private String nombreCompleto;
    private List<DescuentoSemanalResponse> semanas;
}
