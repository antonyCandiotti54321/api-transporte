package com.antonycandiotti.api_transporte.operarios;

import lombok.RequiredArgsConstructor;
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
    public List<Operario> getAllOperarios() {
        return operarioRepository.findAll();
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
