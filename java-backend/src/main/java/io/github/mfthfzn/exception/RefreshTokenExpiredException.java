package io.github.mfthfzn.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;

public class RefreshTokenExpiredException extends JWTVerificationException {
  public RefreshTokenExpiredException(String message) {
    super(message);
  }
}
