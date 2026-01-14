package io.github.mfthfzn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "refresh_tokens")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Token {

  @Id
  @Column(nullable = false)
  private String email;

  @Column(nullable = false, length = 36)
  private String token;

  @OneToOne
  @MapsId
  @JoinColumn(name = "email", referencedColumnName = "email")
  private User user;

}
