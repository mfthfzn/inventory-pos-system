package io.github.mfthfzn.entity;

import io.github.mfthfzn.enums.StockMovementType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_movements")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class StockMovement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private Integer quantity;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private StockMovementType stockMovementType;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @ManyToOne
  @JoinColumn(
          name = "stock_id",
          referencedColumnName = "id"
  )
  private Stock stock;

  @ManyToOne
  @JoinColumn(
          name = "email_inventory",
          referencedColumnName = "email"
  )
  private User user;
}
