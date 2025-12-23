package io.github.mfthfzn.dto;

import io.github.mfthfzn.entity.Name;
import io.github.mfthfzn.enums.UserType;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
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

  @Override
  public boolean equals(Object object) {
    if (object == null || getClass() != object.getClass()) return false;
    LoginResponse that = (LoginResponse) object;
    return Objects.equals(message, that.message) && Objects.equals(name, that.name) && Objects.equals(email, that.email) && role == that.role;
  }

  @Override
  public int hashCode() {
    return Objects.hash(message, name, email, role);
  }
}
