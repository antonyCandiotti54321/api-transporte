
package com.antonycandiotti.api_transporte.usuarios;

import com.antonycandiotti.api_transporte.jwt.JwtService;
import com.antonycandiotti.api_transporte.jwt.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final SecurityUtils securityUtils;
    private final JwtService jwtService;

    @GetMapping
    public ResponseEntity<Page<Usuario>> getAllUsuarios(
            @PageableDefault(size = 10, sort = "id") Pageable pageable,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String nombreCompleto,
            @RequestParam(required = false) Rol rol
    ) {
        Page<Usuario> page = usuarioService.getAllUsuarios(id, nombreCompleto, rol, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuario(@PathVariable Long id) {
        return usuarioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchUsuario(
            @PathVariable Long id,
            @RequestBody @Valid UsuarioUpdateRequest updateRequest,
            HttpServletRequest request) {

        // Verificar si el usuario está editando su propio perfil
        boolean isEditingSelf = securityUtils.isCurrentUserEditingSelf(id, request);

        // Actualizar el usuario
        Usuario actualizado = usuarioService.updatePartial(id, updateRequest);

        // Si está editando su propio perfil, generar un nuevo token
        if (isEditingSelf) {
            String nuevoToken = jwtService.getToken(actualizado);

            // Crear respuesta con el nuevo token
            Map<String, Object> response = new HashMap<>();
            response.put("usuario", actualizado);
            response.put("newToken", nuevoToken);
            response.put("message", "Perfil actualizado. Se ha generado un nuevo token de autenticación.");

            return ResponseEntity.ok(response);
        }

        // Si no está editando su propio perfil, respuesta normal
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}