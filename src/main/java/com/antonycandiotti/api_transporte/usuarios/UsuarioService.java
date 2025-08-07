package com.antonycandiotti.api_transporte.usuarios;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<Usuario> getAllUsuarios(Long id, String nombreCompleto, Rol rol, Pageable pageable) {
        // Si se busca por ID específico
        if (id != null) {
            return usuarioRepository.findById(id)
                    .map(usuario -> new PageImpl<>(List.of(usuario), pageable, 1))
                    .orElse(new PageImpl<>(List.of(), pageable, 0));
        }

        // Si hay filtros de nombreCompleto o rol
        if (nombreCompleto != null || rol != null) {
            return usuarioRepository.findByNombreCompletoAndRol(nombreCompleto, rol, pageable);
        }

        // Sin filtros, devolver todos paginados
        return usuarioRepository.findAll(pageable);
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario updatePartial(Long id, UsuarioUpdateRequest update) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (update.getNombreCompleto() != null && !update.getNombreCompleto().isBlank()) {
            usuario.setNombreCompleto(update.getNombreCompleto().trim());
        }

        if (update.getPassword() != null && !update.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(update.getPassword()));
        }

        if (update.getRol() != null) {
            usuario.setRol(update.getRol());
        }

        if (update.getUsername() != null && !update.getUsername().isBlank()) {
            usuario.setUsername(update.getUsername().trim());
        }

        return usuarioRepository.save(usuario);
    }

    public void deleteById(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }

        // Verificar si el usuario a eliminar es ADMIN
        Usuario usuarioAEliminar = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuarioAEliminar.getRol() == Rol.ADMIN) {
            // Contar cuántos administradores existen actualmente
            long cantidadAdmins = usuarioRepository.countByRol(Rol.ADMIN);

            if (cantidadAdmins <= 1) {
                throw new UltimoAdministradorException("No puedes eliminar el último administrador");
            }
        }

        usuarioRepository.deleteById(id);
    }
}