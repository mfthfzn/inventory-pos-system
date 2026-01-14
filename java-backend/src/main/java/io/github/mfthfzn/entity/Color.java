package io.github.mfthfzn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "colors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Color {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(length = 25, nullable = false)
  private String name;

  @Column(name = "hex_code", length = 8, nullable = false)
  private String hexCode;

  @OneToMany(mappedBy = "color")
  private List<ProductVariant> productVariant;

}
