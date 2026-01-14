package io.github.mfthfzn.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.mfthfzn.enums.UserType;
import lombok.*;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({
        "auth", "accessToken", "refreshToken", "userType"
})
public class AuthResponse {

  private String message;

  private Map<String, Object> data;

  private Map<String, Object> error;

  private boolean auth;

  private String accessToken;

  private String refreshToken;

  private UserType userType;
  
}
