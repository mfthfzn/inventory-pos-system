package io.github.mfthfzn.entity;

import io.github.mfthfzn.enums.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "users"
)
@Getter
@Setter
@NoArgsConstructor
public class User {

  @Id
  @Column (nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Embedded
  private Name name;

  @Enumerated(EnumType.STRING)
  private UserType role;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @ManyToOne
  @JoinColumn(
          name = "store_id",
          referencedColumnName = "id"
  )
  private Store store;

  @OneToMany(mappedBy = "user")
  private List<StockMovement> stockMovements;

  @Override
  public String toString() {
    return getClass().getSimpleName() + "(" +
            "email = " + email + ", " +
            "password = " + password + ", " +
            "name = " + name + ", " +
            "role = " + role + ", " +
            "createdAt = " + createdAt + ", " +
            "updatedAt = " + updatedAt + ", " +
            "store = " + store + ")";
  }
}
