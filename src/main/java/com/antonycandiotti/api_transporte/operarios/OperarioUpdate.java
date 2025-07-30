package com.antonycandiotti.api_transporte.operarios;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperarioUpdate {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombreCompleto;
}