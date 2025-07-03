package com.antonycandiotti.api_transporte.jwt;

import com.antonycandiotti.api_transporte.usuarios.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.lang.Function;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private static final String SECRET_KEY = "MuySeguraClaveDeMasDeTreintaYDosCaracteres!!!";
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String getToken(Usuario usuario) {
        Map<String, Object> extraClaims = new HashMap<>();

        // Agrega el rol (ADMIN, CHOFER, etc.)
        extraClaims.put("role", usuario.getRol().name());

        // Tipo igual al rol, ya que ya no hay relación con Empleado
        extraClaims.put("type", usuario.getRol().name());

        return getToken(extraClaims, usuario);
    }

    private String getToken(Map<String, Object> extraClaims, Usuario usuario) {
        Instant now = Instant.now();
        Instant expiration = now.plus(60, ChronoUnit.DAYS);

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(usuario.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(key)
                .compact();
    }


    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
    }

    private Claims getAllClaims(String token){
        return Jwts
                .parser().setSigningKey(key).build().parseSignedClaims(token).getPayload();
    }

    public <T> T getClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token){
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){
        return getExpiration(token).before(new Date());
    }
    public String getRoleFromToken(String token) {
        return getClaim(token, claims -> claims.get("role", String.class));
    }

    public String getUserTypeFromToken(String token) {
        return getClaim(token, claims -> claims.get("type", String.class));
    }
}
