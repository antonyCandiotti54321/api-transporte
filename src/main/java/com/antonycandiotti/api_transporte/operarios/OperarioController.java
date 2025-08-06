package com.antonycandiotti.api_transporte.operarios;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;


@RestController
@RequestMapping("api/operarios")
@RequiredArgsConstructor
public class OperarioController {

    private final OperarioService operarioService;


    @GetMapping
    public ResponseEntity<Page<Operario>> getAllOperarios(
            @PageableDefault(size = 10, sort = "id") Pageable pageable,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String nombreCompleto
    ) {
        Page<Operario> page = operarioService.getAllOperarios(id, nombreCompleto, pageable);
        return ResponseEntity.ok(page);
    }


    @PostMapping
    public ResponseEntity<Operario> createOperario(@Valid @RequestBody OperarioCreate request) {
        Operario creado = operarioService.createOperario(request);
        return ResponseEntity.ok(creado);
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
