package io.github.mfthfzn.entity;

import io.github.mfthfzn.enums.UserType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(
        name = "users"
)
@Getter
@Setter
@NoArgsConstructor
@ToString
public class User {

  @Id
  private String email;

  private String password;

  @Embedded
  private Name name;

  @Enumerated(EnumType.STRING)
  private UserType role;

  @Override
  public boolean equals(Object object) {
    if (object == null || getClass() != object.getClass()) return false;
    User user = (User) object;
    return Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(name, user.name) && role == user.role;
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, password, name, role);
  }
}
