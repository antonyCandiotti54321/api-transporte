package com.antonycandiotti.api_transporte.ubicacion;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UbicacionesPaqueteDTO {

    @NotNull(message = "El ID es obligatorio")
    private Long id;        // ID del camión o celular

    @NotNull
    @Size(min = 1, message = "Debe haber al menos una ubicación")
    private List<UbicacionDTO> ubicaciones;
}
