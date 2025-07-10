package com.antonycandiotti.api_transporte.adelantos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdelantoResponse {
    private Long id;
    private Long usuarioId;
    private String usuarioNombre;
    private Long operarioId;
    private String operarioNombre;
    private Double cantidad;
    private String mensaje;
    private String nombreCompleto;
    private LocalDateTime fechaHora;
}
