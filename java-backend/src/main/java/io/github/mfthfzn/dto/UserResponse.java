package io.github.mfthfzn.dto;

import lombok.*;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

  private String message;

  private Map<String, Object> data;

  private Map<String, Object> error;
}
