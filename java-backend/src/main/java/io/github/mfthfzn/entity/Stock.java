package io.github.mfthfzn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "stocks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private Integer quantity;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @ManyToOne
  @JoinColumn(
          name = "store_id",
          referencedColumnName = "id"
  )
  private Store store;

  @ManyToOne
  @JoinColumn(
          name = "product_variant_id",
          referencedColumnName = "id"
  )
  private ProductVariant productVariant;

  @OneToMany(mappedBy = "stock")
  private List<StockMovement> stockMovements;

}
