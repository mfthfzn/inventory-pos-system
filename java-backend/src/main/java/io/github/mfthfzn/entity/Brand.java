package io.github.mfthfzn.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "brands")
@Getter
@Setter
@NoArgsConstructor
public class Brand {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(length = 50, nullable = false)
  private String name;

  @OneToMany(mappedBy = "brand")
  private List<Product> products;

  @Override
  public String toString() {
    return getClass().getSimpleName() + "(" +
            "id = " + id + ", " +
            "name = " + name + ")";
  }
}
