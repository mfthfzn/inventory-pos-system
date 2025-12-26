package io.github.mfthfzn.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SessionResponse {

  private boolean expired;

  private boolean removed;

  private String message;
}
