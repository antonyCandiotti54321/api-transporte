package com.antonycandiotti.api_transporte.auth;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "El username es obligatorio")
    @Size(min = 3, max = 20, message = "El username debe tener entre 3 y 20 caracteres")
    String username;
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 3, max = 20, message = "La contraseña debe tener entre 3 y 20 caracteres")
    String password;
}
