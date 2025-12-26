package io.github.mfthfzn.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.UUID;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class SessionRequest {

  @NotBlank(message = "Email tidak boleh kosong")
  @Email(message = "Email yang diinputkan harus valid")
  private String email;

  @NotBlank(message = "Token tidak boleh kosong")
  @UUID(message = "Token harus valid")
  private String token;

  public SessionRequest(String email, String token) {
    this.email = email;
    this.token = token;
  }

  public SessionRequest(String email) {
    this.email = email;
  }
}
