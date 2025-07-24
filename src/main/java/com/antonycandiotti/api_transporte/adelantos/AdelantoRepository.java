package com.antonycandiotti.api_transporte.adelantos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

public interface AdelantoRepository extends JpaRepository<Adelanto,Long> {
    List<Adelanto> findByUsuarioId(Long usuarioId);

    List<Adelanto> findByOperarioId(Long operarioId);
    long count();

    @Modifying
    @Transactional
    @Query("DELETE FROM Adelanto a WHERE a.fechaHora BETWEEN :inicio AND :fin")
    void deleteByFechaHoraBetween(@Param("inicio") ZonedDateTime inicio, @Param("fin") ZonedDateTime fin);

}
