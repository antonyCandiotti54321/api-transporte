package com.antonycandiotti.api_transporte.config;

import com.antonycandiotti.api_transporte.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1) CORS
                .cors(withDefaults())
                // 2) CSRF off
                .csrf(AbstractHttpConfigurer::disable)
                // 3) permitir recursos estáticos de Vaadin
                .authorizeHttpRequests(auth -> auth
                        // rutas de Vaadin UI y estáticos
                        .requestMatchers(
                                "/",
                                "/VAADIN/**",
                                "/frontend/**",
                                "/webjars/**",
                                "/resources/**",
                                "/static/**",
                                "/icons/**",
                                "/favicon.ico"
                        ).permitAll()
                        // tus APIs
                        .requestMatchers("/index/**").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/register").hasRole("ADMIN")
                        .requestMatchers("/api/health/**").permitAll()
                        .requestMatchers("/ping/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/usuarios/**").hasAnyRole("ADMIN", "CHOFER")
                        .requestMatchers(HttpMethod.DELETE,"/api/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH,"/api/usuarios/**").hasRole("ADMIN")
                        .requestMatchers("/api/admins/**").hasRole("ADMIN")
                        .requestMatchers("/api/choferes/**").hasAnyRole("ADMIN", "CHOFER")
                        .requestMatchers("/api/operarios/**").hasAnyRole("ADMIN", "CHOFER")
                        .requestMatchers("/ws/**").permitAll()
                        // resto debe autenticarse
                        .anyRequest().authenticated()
                )
                // 4) Stateless JWT
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:8081","http://localhost:8080"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
