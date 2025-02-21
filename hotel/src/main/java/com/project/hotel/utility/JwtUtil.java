package com.project.hotel.utility;

import com.project.hotel.entity.RoleEntity;
import com.project.hotel.entity.UserEntity;
import com.project.hotel.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Key;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@PropertySource("classpath:application.properties")
public class JwtUtil {
    private final UserRepository clientRepository;

    @Value("${application.security.jwt.secret-key}")
    private String secret_key;

    @Value("${application.security.jwt.expiration}")
    private long accessTokenValidity;

    private static Key key;

    public JwtUtil(UserRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    private Key initializeKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret_key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(UserEntity client) {
        key = initializeKey();

        // Fetch user with authorities
        client = clientRepository.findByEmail(client.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("USER_NOT_FOUND"));

        Set<RoleEntity> authorities = client.getRoles();
        List<String> roles = new ArrayList<>();

        for (RoleEntity authority : authorities) {
            roles.add(authority.getName());  // Keep roles as they are in DB
        }

        // JWT Claims
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("authorities", roles);
        claimsMap.put("username", client.getEmail());
        claimsMap.put("user_id", client.getId());

        // Token Expiration
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));

        // Build JWT
        return Jwts.builder()
                .setSubject(client.getEmail())
                .setIssuedAt(tokenCreateTime)
                .setExpiration(tokenValidity)
                .addClaims(claimsMap)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    private Claims parseJwtClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(initializeKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException ex) {
            log.error("Token expired: {}", ex.getMessage());
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Token invalid: {}", ex.getMessage());
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateClaims(Claims claims) {
        return claims.getExpiration().after(new Date());
    }

    public Integer getUserId(Claims claims) {
        return claims.get("user_id", Integer.class);
    }

    public Collection<GrantedAuthority> extractAuthorities(Claims claims) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (claims.containsKey("authorities")) {
            List<String> roles = claims.get("authorities", List.class);
            for (String role : roles) {
                authorities.add(new SimpleGrantedAuthority(role)); // No "ROLE_" prefix
            }
        }
        return authorities;
    }
}