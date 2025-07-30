package com.antonycandiotti.api_transporte.adelantos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @DecimalMin(value = "0.01", message = "La cantidad debe ser mayor que 0")
    private Double cantidad;
    @Size(max = 255, message = "El mensaje no puede exceder los 255 caracteres")
    private String mensaje;
}
