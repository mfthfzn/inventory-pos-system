package io.github.mfthfzn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Table(name = "reset_password_tokens")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordToken {

  @Id
  private String email;

  @Column(name = "token", nullable = false, length = 36)
  private String token;

  @Column(name = "expired_at", nullable = false)
  private LocalDateTime localDateTime;

  @OneToOne
  @MapsId
  @JoinColumn(
          name = "email",
          referencedColumnName = "email"
  )
  private User user;

}
