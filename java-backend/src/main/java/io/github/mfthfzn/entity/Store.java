package io.github.mfthfzn.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "stores")
@Getter
@Setter
@NoArgsConstructor
public class Store {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  private String address;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "store")
  private List<User> users;

  @OneToMany(mappedBy = "store")
  private List<Stock> stocks;

  @Override
  public String toString() {
    return getClass().getSimpleName() + "(" +
            "id = " + id + ", " +
            "name = " + name + ", " +
            "address = " + address + ", " +
            "createdAt = " + createdAt + ", " +
            "updatedAt = " + updatedAt + ")";
  }
}
