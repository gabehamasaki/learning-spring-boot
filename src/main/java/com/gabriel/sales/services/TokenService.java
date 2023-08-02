package com.gabriel.sales.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.gabriel.sales.models.UserModel;

@Service
public class TokenService {

  @Value("${api.security.token.secret}")
  private String secret;


  public String generateToken(UserModel user) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      return JWT.create()
              .withIssuer("learning-spring-api")
              .withSubject(user.getLogin())
              .withExpiresAt(this.genExpirationDate())
              .sign(algorithm);
    } catch (JWTCreationException e) {
      throw new RuntimeException("Error while generating token", e);
    }
  }

  public String validateToken(String token) {
      try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      return JWT
              .require(algorithm)
              .withIssuer("learning-spring-api")
              .build()
              .verify(token)
              .getSubject();
    } catch (JWTVerificationException e) {
      throw new RuntimeException("Error while validate token", e);
    }
  }

  private Instant genExpirationDate() {
    return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
  }
}
