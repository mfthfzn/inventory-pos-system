package io.github.mfthfzn.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class LoginRequest {

  @NotBlank(message = "Email tidak boleh kosong")
  @Email(message = "Email yang diinputkan harus valid")
  private String email;

  @NotBlank(message = "Password tidak boleh kosong")
  private String password;

  public LoginRequest(String email, String password) {
    this.email = email;
    this.password = password;
  }
}
