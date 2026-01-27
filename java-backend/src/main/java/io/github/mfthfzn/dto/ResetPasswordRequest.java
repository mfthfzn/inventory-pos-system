package io.github.mfthfzn.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ResetPasswordRequest {

  @NotBlank(message = "Email cannot blank")
  @Email(message = "Email must be valid valid")
  private String email;

  @NotBlank(message = "New password cannot blank")
  private String newPassword;

}
