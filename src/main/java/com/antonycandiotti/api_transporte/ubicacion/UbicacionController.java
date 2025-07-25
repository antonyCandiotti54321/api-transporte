package com.antonycandiotti.api_transporte.ubicacion;


import jakarta.validation.Valid;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class UbicacionController {

    @MessageMapping("/ubicacion")  // Recibe mensajes en /app/ubicacion
    @SendTo("/topic/ubicacion")    // Reenvía a todos suscritos en /topic/ubicacion
    public UbicacionesPaqueteDTO recibirUbicaciones(@Valid UbicacionesPaqueteDTO paquete) {
        System.out.println("📦 Paquete recibido del ID: " + paquete.getId());
        // Ya no se imprimen las ubicaciones individuales
        return paquete;
    }
}
