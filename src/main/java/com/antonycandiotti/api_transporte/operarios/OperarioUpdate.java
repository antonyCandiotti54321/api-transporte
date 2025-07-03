package com.antonycandiotti.api_transporte.operarios;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperarioUpdate {
    private String nombreCompleto;
}