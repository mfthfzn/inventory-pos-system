package io.github.mfthfzn.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtPayload {

  private String email;

  private String role;

  private String name;

  private Integer storeId;

  private String storeName;

}
