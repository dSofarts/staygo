package ru.staygo.bookingservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.staygo.bookingservice.entity.User;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private Long expiration;

  public String generateToken(User user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", user.getRole().name());

    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

    return Jwts.builder()
      .claims(claims)
      .subject(user.getUsername())
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + expiration))
      .signWith(key)
      .compact();
  }

  public Claims extractClaims(String token) {
    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
    return Jwts.parser()
      .verifyWith(key)
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }

  public String extractUsername(String token) {
    return extractClaims(token).getSubject();
  }
}
