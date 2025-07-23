package com.antonycandiotti.api_transporte.usuarios;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

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
        usuarioRepository.deleteById(id);
    }
}
