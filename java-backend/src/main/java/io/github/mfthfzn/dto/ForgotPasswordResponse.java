package io.github.mfthfzn.dto;

import io.github.mfthfzn.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordResponse {

  String linkResetPassword;

  User user;

}
