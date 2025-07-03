package com.antonycandiotti.api_transporte.auth;

import com.antonycandiotti.api_transporte.usuarios.Rol;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponse {
    String token;
    Long idUsuario;
    String nombreCompleto;
    Rol rol;


}
