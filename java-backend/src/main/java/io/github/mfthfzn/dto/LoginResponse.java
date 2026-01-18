package io.github.mfthfzn.dto;

import io.github.mfthfzn.entity.User;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

  private String accessToken;

  private String refreshToken;

  private User user;
  
}
