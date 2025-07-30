package com.antonycandiotti.api_transporte.operarios;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/operarios")
@RequiredArgsConstructor
public class OperarioController {

    private final OperarioService operarioService;

    @PostMapping
    public ResponseEntity<Operario> createOperario(@Valid @RequestBody OperarioCreate request) {
        Operario creado = operarioService.createOperario(request);
        return ResponseEntity.ok(creado);
    }

    @GetMapping
    public ResponseEntity<List<Operario>> getAllOperarios() {
        return ResponseEntity.ok(operarioService.getAllOperarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Operario> getOperarioById(@PathVariable Long id) {
        return operarioService.getOperarioById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Operario> updateOperario(@PathVariable Long id, @RequestBody OperarioUpdate request) {
        return ResponseEntity.ok(operarioService.updateOperario(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOperario(@PathVariable Long id) {
        operarioService.deleteOperario(id);
        return ResponseEntity.noContent().build();
    }
}
