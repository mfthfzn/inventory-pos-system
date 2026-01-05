package io.github.mfthfzn.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Integer price;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @ManyToOne
  @JoinColumn(
          name = "brand_id",
          referencedColumnName = "id"
  )
  private Brand brand;

  @ManyToOne
  @JoinColumn(
          name = "product_target_id",
          referencedColumnName = "id"
  )
  private ProductTarget productTarget;

  @ManyToOne
  @JoinColumn(
          name = "category_id",
          referencedColumnName = "id"
  )
  private Category category;

  @OneToMany(mappedBy = "product")
  private List<ProductVariant> productVariant;
}
