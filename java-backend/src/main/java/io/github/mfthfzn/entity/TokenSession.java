package io.github.mfthfzn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "token_sessions")
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class TokenSession {

  @Id
  @Column(nullable = false)
  private String email;

  @Column(nullable = false, length = 36)
  private String token;

  @Column(name = "expired_at")
  private LocalDateTime expiredAt;

  @OneToOne
  @MapsId
  @JoinColumn(name = "email", referencedColumnName = "email")
  private User user;

  @Override
  public boolean equals(Object object) {
    if (object == null || getClass() != object.getClass()) return false;
    TokenSession that = (TokenSession) object;
    return Objects.equals(email, that.email) && Objects.equals(token, that.token) && Objects.equals(expiredAt, that.expiredAt) && Objects.equals(user, that.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, token, expiredAt, user);
  }
}
