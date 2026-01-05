package io.github.mfthfzn.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "sizes")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Size {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(length = 7, nullable = false)
  private String name;

  @Column(length = 50, nullable = false)
  private String type;

  @OneToMany(mappedBy = "size")
  private List<ProductVariant> productVariant;

}
