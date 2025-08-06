package com.antonycandiotti.api_transporte.operarios;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OperarioService {

    private final OperarioRepository operarioRepository;

    public Operario createOperario(OperarioCreate request) {
        Operario operario = Operario.builder()
                .nombreCompleto(request.getNombreCompleto())
                .build();
        return operarioRepository.save(operario);
    }

    @Transactional(readOnly = true)
    public Page<Operario> getAllOperarios(Long id, String nombreCompleto, Pageable pageable) {
        if (id != null) {
            return operarioRepository.findById(id)
                    .map(op -> new PageImpl<>(List.of(op), pageable, 1))
                    .orElse(new PageImpl<>(List.of(), pageable, 0));  // Solución correcta aquí
        } else if (nombreCompleto != null && !nombreCompleto.isBlank()) {
            return operarioRepository.findByNombreCompletoContainingIgnoreCase(nombreCompleto, pageable);
        } else {
            return operarioRepository.findAll(pageable);
        }
    }



    @Transactional(readOnly = true)
    public Optional<Operario> getOperarioById(Long id) {
        return operarioRepository.findById(id);
    }

    @Transactional
    public Operario updateOperario(Long id, OperarioUpdate update) {
        Operario operario = operarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operario no encontrado"));

        if (update.getNombreCompleto() != null) {
            operario.setNombreCompleto(update.getNombreCompleto().trim());
        }

        return operarioRepository.save(operario);
    }


    @Transactional
    public void deleteOperario(Long id) {
        Operario operario = operarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operario no encontrado"));
        operarioRepository.delete(operario);
    }
}
