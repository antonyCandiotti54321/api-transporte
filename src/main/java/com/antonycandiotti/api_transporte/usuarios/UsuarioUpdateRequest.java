package com.antonycandiotti.api_transporte.usuarios;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioUpdateRequest {

    @Size(min = 3, max = 20, message = "El nombre completo debe tener entre 3 y 20 caracteres")
    private String nombreCompleto;

    @Size(min = 3, max = 20, message = "El nombre de usuario debe tener entre 3 y 20 caracteres")
    private String username; // 🔥 Este campo estaba faltando

    @Size(min = 3, max = 20, message = "La contraseña debe tener entre 3 y 20 caracteres")
    private String password;

    private Rol rol;


}
