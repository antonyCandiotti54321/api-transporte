package com.antonycandiotti.api_transporte.adelantos;

import com.antonycandiotti.api_transporte.operarios.Operario;
import com.antonycandiotti.api_transporte.usuarios.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "adelantos")
public class Adelanto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "operario_id", referencedColumnName = "id")
    private Operario operario;

    @Column(nullable = false)
    private Double cantidad;

    private String mensaje;

    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;
}
