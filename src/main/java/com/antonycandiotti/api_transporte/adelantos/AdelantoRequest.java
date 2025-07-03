package com.antonycandiotti.api_transporte.adelantos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdelantoRequest {
    @NotNull
    private Long usuarioId;

    @NotNull
    private Long operarioId;

    @NotNull
    private Double cantidad;

    private String mensaje;

    @NotBlank
    private String nombreCompleto;
}
