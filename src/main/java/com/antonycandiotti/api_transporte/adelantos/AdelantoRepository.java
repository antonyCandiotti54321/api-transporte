package com.antonycandiotti.api_transporte.adelantos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

public interface AdelantoRepository extends JpaRepository<Adelanto,Long> {

    // Métodos originales sin cambios
    List<Adelanto> findByUsuarioId(Long usuarioId);
    List<Adelanto> findByOperarioId(Long operarioId);
    long count();

    @Modifying
    @Transactional
    @Query("DELETE FROM Adelanto a WHERE a.fechaHora BETWEEN :inicio AND :fin")
    void deleteByFechaHoraBetween(@Param("inicio") ZonedDateTime inicio, @Param("fin") ZonedDateTime fin);

    // Nuevos métodos con paginación
    Page<Adelanto> findByUsuarioId(Long usuarioId, Pageable pageable);

    @Query("SELECT a FROM Adelanto a WHERE a.usuario.id = :usuarioId " +
            "AND (:operarioNombre IS NULL OR LOWER(a.operario.nombreCompleto) LIKE LOWER(CONCAT('%', :operarioNombre, '%'))) " +
            "AND (:cantidadMin IS NULL OR a.cantidad >= :cantidadMin) " +
            "AND (:cantidadMax IS NULL OR a.cantidad <= :cantidadMax)")
    Page<Adelanto> findByUsuarioIdWithFilters(@Param("usuarioId") Long usuarioId,
                                              @Param("operarioNombre") String operarioNombre,
                                              @Param("cantidadMin") Double cantidadMin,
                                              @Param("cantidadMax") Double cantidadMax,
                                              Pageable pageable);

    @Query("SELECT a FROM Adelanto a WHERE " +
            "(:id IS NULL OR a.id = :id) " +
            "AND (:usuarioNombre IS NULL OR LOWER(a.usuario.nombreCompleto) LIKE LOWER(CONCAT('%', :usuarioNombre, '%'))) " +
            "AND (:operarioNombre IS NULL OR LOWER(a.operario.nombreCompleto) LIKE LOWER(CONCAT('%', :operarioNombre, '%'))) " +
            "AND (:cantidadMin IS NULL OR a.cantidad >= :cantidadMin) " +
            "AND (:cantidadMax IS NULL OR a.cantidad <= :cantidadMax)")
    Page<Adelanto> findAllWithFilters(@Param("id") Long id,
                                      @Param("usuarioNombre") String usuarioNombre,
                                      @Param("operarioNombre") String operarioNombre,
                                      @Param("cantidadMin") Double cantidadMin,
                                      @Param("cantidadMax") Double cantidadMax,
                                      Pageable pageable);
}