package io.github.mfthfzn.dto;

import io.github.mfthfzn.entity.Name;
import io.github.mfthfzn.enums.UserType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class LoginResponse {

  private boolean auth;

  private String message;

  private Name name;

  private String email;

  private UserType role;

  private String token;

  public LoginResponse() {
  }

  public LoginResponse(String message) {
    this.message = message;
  }
}
