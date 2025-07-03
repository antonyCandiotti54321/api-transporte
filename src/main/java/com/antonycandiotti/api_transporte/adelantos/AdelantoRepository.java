package com.antonycandiotti.api_transporte.adelantos;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdelantoRepository extends JpaRepository<Adelanto,Long> {
    List<Adelanto> findByUsuarioId(Long usuarioId);

    List<Adelanto> findByOperarioId(Long operarioId);
}
