package com.antonycandiotti.api_transporte.adelantos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DescuentoSemanalResponse {
    private LocalDate inicioSemana;
    private LocalDate finSemana;
    private Double totalDescuento;
}
