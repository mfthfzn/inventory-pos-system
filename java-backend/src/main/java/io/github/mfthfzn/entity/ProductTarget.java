package io.github.mfthfzn.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "product_targets")
@Getter
@Setter
@NoArgsConstructor
public class ProductTarget {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(length = 25, nullable = false)
  private String name;

  @OneToMany(mappedBy = "productTarget")
  private List<Product> products;

  @Override
  public String toString() {
    return getClass().getSimpleName() + "(" +
            "id = " + id + ", " +
            "name = " + name + ")";
  }
}
