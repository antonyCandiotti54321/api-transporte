package com.antonycandiotti.api_transporte.operarios;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OperarioRepository extends JpaRepository<Operario,Long> {
    Optional<Operario> findByNombreCompleto(String nombreCompleto);
}
