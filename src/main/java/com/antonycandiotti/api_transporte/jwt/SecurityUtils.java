package com.antonycandiotti.api_transporte.jwt;

import com.antonycandiotti.api_transporte.usuarios.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final JwtService jwtService;

    /**
     * Obtiene el usuario actualmente autenticado desde el SecurityContext
     */
    public Usuario getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Usuario) {
            return (Usuario) authentication.getPrincipal();
        }

        return null;
    }

    /**
     * Obtiene el ID del usuario desde el token JWT actual
     */
    public Long getCurrentUserIdFromToken(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token != null) {
            return jwtService.getUserIdFromToken(token);
        }
        return null;
    }

    /**
     * Verifica si el usuario actual está intentando modificar su propio perfil
     */
    public boolean isCurrentUserEditingSelf(Long targetUserId, HttpServletRequest request) {
        Long currentUserId = getCurrentUserIdFromToken(request);
        return currentUserId != null && currentUserId.equals(targetUserId);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}