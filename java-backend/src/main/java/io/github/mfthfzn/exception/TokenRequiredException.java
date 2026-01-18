package io.github.mfthfzn.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;

public class TokenRequiredException extends RuntimeException {

  public TokenRequiredException(String message) {
    super(message);
  }

}
