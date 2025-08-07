package com.antonycandiotti.api_transporte.usuarios;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    long countByRol(Rol rol);

    // Métodos para filtrado con paginación
    Page<Usuario> findByNombreCompletoContainingIgnoreCase(String nombreCompleto, Pageable pageable);
    Page<Usuario> findByRol(Rol rol, Pageable pageable);

    @Query("SELECT u FROM Usuario u WHERE " +
            "(:nombreCompleto IS NULL OR LOWER(u.nombreCompleto) LIKE LOWER(CONCAT('%', :nombreCompleto, '%'))) AND " +
            "(:rol IS NULL OR u.rol = :rol)")
    Page<Usuario> findByNombreCompletoAndRol(@Param("nombreCompleto") String nombreCompleto,
                                             @Param("rol") Rol rol,
                                             Pageable pageable);
}