package io.github.mfthfzn.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(length = 50, nullable = false)
  private String name;

  @OneToMany(mappedBy = "category")
  private List<Product> products;

  @Override
  public String toString() {
    return getClass().getSimpleName() + "(" +
            "id = " + id + ", " +
            "name = " + name + ")";
  }
}
