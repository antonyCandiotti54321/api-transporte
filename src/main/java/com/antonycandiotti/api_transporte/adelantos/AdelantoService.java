package com.antonycandiotti.api_transporte.adelantos;

import com.antonycandiotti.api_transporte.operarios.Operario;
import com.antonycandiotti.api_transporte.operarios.OperarioRepository;
import com.antonycandiotti.api_transporte.usuarios.Usuario;
import com.antonycandiotti.api_transporte.usuarios.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdelantoService {

    private final AdelantoRepository adelantoRepository;
    private final UsuarioRepository usuarioRepository;
    private final OperarioRepository operarioRepository;

    public AdelantoResponse create(AdelantoRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Operario operario = operarioRepository.findById(request.getOperarioId())
                .orElseThrow(() -> new RuntimeException("Operario no encontrado"));

        Adelanto adelanto = Adelanto.builder()
                .usuario(usuario)
                .operario(operario)
                .cantidad(request.getCantidad())
                .mensaje(request.getMensaje())
                .fechaHora(ZonedDateTime.now(ZoneId.of("America/Lima"))) // <--- zona horaria fija
                .build();

        return toResponse(adelantoRepository.save(adelanto));
    }


    public List<AdelantoResponse> findAll() {
        return adelantoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public AdelantoResponse findById(Long id) {
        Adelanto adelanto = adelantoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adelanto no encontrado"));
        return toResponse(adelanto);
    }

    public AdelantoResponse update(Long id, AdelantoRequest request) {
        Adelanto adelanto = adelantoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adelanto no encontrado"));

        adelanto.setCantidad(request.getCantidad());
        adelanto.setMensaje(request.getMensaje());

        if (!adelanto.getUsuario().getId().equals(request.getUsuarioId())) {
            Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            adelanto.setUsuario(usuario);
        }

        if (!adelanto.getOperario().getId().equals(request.getOperarioId())) {
            Operario operario = operarioRepository.findById(request.getOperarioId())
                    .orElseThrow(() -> new RuntimeException("Operario no encontrado"));
            adelanto.setOperario(operario);
        }

        return toResponse(adelantoRepository.save(adelanto));
    }


    public void delete(Long id) {
        if (!adelantoRepository.existsById(id)) {
            throw new RuntimeException("Adelanto no encontrado");
        }
        adelantoRepository.deleteById(id);
    }

    private AdelantoResponse toResponse(Adelanto adelanto) {
        return AdelantoResponse.builder()
                .id(adelanto.getId())
                .usuarioId(adelanto.getUsuario().getId())
                .usuarioNombre(adelanto.getUsuario().getNombreCompleto())
                .operarioId(adelanto.getOperario().getId())
                .operarioNombre(adelanto.getOperario().getNombreCompleto())
                .cantidad(adelanto.getCantidad())
                .mensaje(adelanto.getMensaje())
                .fechaHora(adelanto.getFechaHora())
                .build();
    }



    public List<AdelantoResponse> getAdelantosPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Adelanto> adelantos = adelantoRepository.findByUsuarioId(usuarioId);

        return adelantos.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<OperarioDescuentoResponse> getTotalDescuentosPorOperario() {
        List<Operario> operarios = operarioRepository.findAll();

        return operarios.stream().map(operario -> {
            List<Adelanto> adelantos = adelantoRepository.findByOperarioId(operario.getId());

            // Agrupar por semana laboral (sábado a viernes)
            Map<String, List<Adelanto>> agrupadoPorSemana = adelantos.stream()
                    .collect(Collectors.groupingBy(adelanto -> {
                        ZonedDateTime fecha = adelanto.getFechaHora();
                        ZonedDateTime sabado = fecha.with(TemporalAdjusters.previousOrSame(DayOfWeek.SATURDAY));
                        ZonedDateTime viernes = sabado.plusDays(6);
                        return sabado.toLocalDate() + "_" + viernes.toLocalDate();
                    }));

            List<DescuentoSemanalResponse> semanas = agrupadoPorSemana.entrySet().stream()
                    .map(entry -> {
                        String[] partes = entry.getKey().split("_");
                        LocalDate inicioSemana = LocalDate.parse(partes[0]);
                        LocalDate finSemana = LocalDate.parse(partes[1]);
                        double total = entry.getValue().stream()
                                .mapToDouble(Adelanto::getCantidad)
                                .sum();
                        return new DescuentoSemanalResponse(inicioSemana, finSemana, total);
                    })
                    .sorted(Comparator.comparing(DescuentoSemanalResponse::getInicioSemana)) // opcional
                    .collect(Collectors.toList());

            return OperarioDescuentoResponse.builder()
                    .operarioId(operario.getId())
                    .nombreCompleto(operario.getNombreCompleto())
                    .semanas(semanas)
                    .build();
        }).collect(Collectors.toList());
    }



}
