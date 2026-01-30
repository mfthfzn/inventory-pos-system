package io.github.mfthfzn.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordRequest {

  @NotBlank(message = "Email cannot blank")
  @Email(message = "Email must be valid valid")
  private String email;

}
