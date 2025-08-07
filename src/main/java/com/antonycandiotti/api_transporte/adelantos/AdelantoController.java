package com.antonycandiotti.api_transporte.adelantos;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adelantos")
@RequiredArgsConstructor
public class AdelantoController {

    private final AdelantoService adelantoService;

    @GetMapping("/choferes/{id}")
    public ResponseEntity<Page<AdelantoResponse>> getAdelantosPorChofer(
            @PathVariable Long id,
            @PageableDefault(size = 10, sort = "fechaHora") Pageable pageable,
            @RequestParam(required = false) String operarioNombre,
            @RequestParam(required = false) Double cantidadMin,
            @RequestParam(required = false) Double cantidadMax
    ) {
        Page<AdelantoResponse> adelantos = adelantoService.getAdelantosPorUsuario(
                id, operarioNombre, cantidadMin, cantidadMax, pageable);
        return ResponseEntity.ok(adelantos);
    }

    @GetMapping("/descuentos")
    public ResponseEntity<Page<OperarioDescuentoResponse>> getDescuentosTotalesPorOperario(
            @PageableDefault(size = 10, sort = "nombreCompleto") Pageable pageable,
            @RequestParam(required = false) Long operarioId,
            @RequestParam(required = false) String nombreCompleto
    ) {
        Page<OperarioDescuentoResponse> descuentos = adelantoService.getTotalDescuentosPorOperario(
                operarioId, nombreCompleto, pageable);
        return ResponseEntity.ok(descuentos);
    }

    @GetMapping
    public ResponseEntity<Page<AdelantoResponse>> getAll(
            @PageableDefault(size = 10, sort = "fechaHora") Pageable pageable,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String usuarioNombre,
            @RequestParam(required = false) String operarioNombre,
            @RequestParam(required = false) Double cantidadMin,
            @RequestParam(required = false) Double cantidadMax
    ) {
        Page<AdelantoResponse> adelantos = adelantoService.findAll(
                id, usuarioNombre, operarioNombre, cantidadMin, cantidadMax, pageable);
        return ResponseEntity.ok(adelantos);
    }

    // Mantener endpoints originales existentes
    @PostMapping
    public ResponseEntity<AdelantoResponse> create(@RequestBody @Valid AdelantoRequest request) {
        return ResponseEntity.ok(adelantoService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdelantoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(adelantoService.findById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AdelantoResponse> update(@PathVariable Long id, @RequestBody @Valid AdelantoRequest request) {
        return ResponseEntity.ok(adelantoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adelantoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/descuentos")
    public ResponseEntity<Void> deleteAdelantosPorSemana(@RequestBody EliminarSemanaRequest request) {
        adelantoService.deleteAdelantosPorSemana(request.getFechaInicio(), request.getFechaFin());
        return ResponseEntity.noContent().build();
    }
}