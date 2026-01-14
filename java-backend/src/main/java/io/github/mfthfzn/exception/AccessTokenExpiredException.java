package io.github.mfthfzn.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;

public class AccessTokenExpiredException extends JWTVerificationException {
  public AccessTokenExpiredException(String message) {
    super(message);
  }
}
