package com.antonycandiotti.api_transporte.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.security.Principal;

@Data
@Builder
@AllArgsConstructor
public class WebSocketUserPrincipal implements Principal {
    private final String name;
    private final String role;
}
