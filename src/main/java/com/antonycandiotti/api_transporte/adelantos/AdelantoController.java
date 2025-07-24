package com.antonycandiotti.api_transporte.adelantos;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adelantos")
@RequiredArgsConstructor
public class AdelantoController {

    private final AdelantoService adelantoService;

    @PostMapping
    public ResponseEntity<AdelantoResponse> create(@RequestBody @Valid AdelantoRequest request) {
        return ResponseEntity.ok(adelantoService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<AdelantoResponse>> getAll() {
        return ResponseEntity.ok(adelantoService.findAll());
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

    @GetMapping("/choferes/{id}")
    public ResponseEntity<List<AdelantoResponse>> getAdelantosPorChofer(@PathVariable Long id) {
        List<AdelantoResponse> adelantos = adelantoService.getAdelantosPorUsuario(id);
        return ResponseEntity.ok(adelantos);
    }

    @GetMapping("/descuentos")
    public ResponseEntity<List<OperarioDescuentoResponse>> getDescuentosTotalesPorOperario() {
        return ResponseEntity.ok(adelantoService.getTotalDescuentosPorOperario());
    }

    @DeleteMapping("/descuentos")
    public ResponseEntity<Void> deleteAdelantosPorSemana(@RequestBody EliminarSemanaRequest request) {
        adelantoService.deleteAdelantosPorSemana(request.getFechaInicio(), request.getFechaFin());
        return ResponseEntity.noContent().build();
    }

}
