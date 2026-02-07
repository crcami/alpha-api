package com.alphasteel.alphaapi.security.service;

import jakarta.enterprise.context.ApplicationScoped;
import java.security.SecureRandom;
import java.util.Base64;

/** Generates secure random tokens. */
@ApplicationScoped
public class TokenGenerator {

  private final SecureRandom secureRandom = new SecureRandom();

  public String generateUrlSafeToken() {
    byte[] bytes = new byte[32];
    secureRandom.nextBytes(bytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }
}
