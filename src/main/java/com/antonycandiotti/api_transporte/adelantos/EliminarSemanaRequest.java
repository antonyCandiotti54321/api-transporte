package com.antonycandiotti.api_transporte.adelantos;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class EliminarSemanaRequest {
    private ZonedDateTime fechaInicio; // sábado
    private ZonedDateTime fechaFin;    // viernes
}
