package com.alphasteel.alphaapi.security.service;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Set;

/** Issues JWT access tokens. */
@ApplicationScoped
public class JwtService {

  public String issueToken(String email, Set<String> roles) {
    return Jwt.upn(email).groups(roles).sign();
  }
}
