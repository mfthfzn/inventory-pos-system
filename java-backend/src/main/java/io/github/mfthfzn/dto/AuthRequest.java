package io.github.mfthfzn.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

  @NotBlank(message = "Email cannot blank")
  @Email(message = "Email must be valid valid")
  private String email;

  @NotBlank(message = "Password cannot blank")
  private String password;
}
