package io.github.mfthfzn.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transaction_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "current_price", nullable = false)
  private Integer currentPrice;

  @Column(nullable = false)
  private Integer quantity;

  @ManyToOne
  @JoinColumn(
          name = "transaction_id",
          referencedColumnName = "id"
  )
  private Transaction transaction;

  @ManyToOne
  @JoinColumn(
          name = "product_variant_id",
          referencedColumnName = "id"
  )
  private ProductVariant productVariant;

}
