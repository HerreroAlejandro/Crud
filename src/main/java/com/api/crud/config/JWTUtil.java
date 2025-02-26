package com.api.crud.config;

import com.zaxxer.hikari.pool.HikariProxyCallableStatement;
import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JWTUtil {


    public static final String JWT_KEY = "andintheendtheloveyoutakeisequaltotheloveyoumake";


    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, JWT_KEY)
                .compact();
    }


    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public boolean isTokenExpired(String token) {

        return getExpirationDate(token).before(new Date());
    }

    private Date getExpirationDate(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    public boolean validateToken(String token) {
        boolean isValid = false;
        try {
            Jwts.parserBuilder()
                    .setSigningKey(JWT_KEY)
                    .build()
                    .parseClaimsJws(token);
            isValid = true;
        } catch (Exception e) {
            isValid = false;
            System.out.println("Error de validación del token: " + e.getMessage());
        }
        System.out.println("Token recibido: " + token);
        System.out.println("Token válido: " + isValid);
        return isValid;
    }

    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        // Aquí asumimos que los roles están almacenados en el campo "roles" como una lista de cadenas
        return claims.get("roles", List.class);  // Extrae el campo 'roles' y lo convierte en una lista
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

}