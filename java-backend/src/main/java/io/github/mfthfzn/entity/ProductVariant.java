package io.github.mfthfzn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "product_variants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariant {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(length = 16, nullable = false)
  private String sku;

  @ManyToOne
  @JoinColumn(
          name = "product_id",
          referencedColumnName = "id"
  )
  private Product product;

  @ManyToOne
  @JoinColumn(
          name = "size_id",
          referencedColumnName = "id"
  )
  private Size size;

  @ManyToOne
  @JoinColumn(
          name = "color_id",
          referencedColumnName = "id"
  )
  private Color color;

  @OneToMany(mappedBy = "productVariant")
  private List<Stock> stocks;

  @OneToMany(mappedBy = "productVariant")
  private List<TransactionItem> transactionItems;

}
