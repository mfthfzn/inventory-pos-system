package io.github.mfthfzn.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.mockito.exceptions.misusing.NotAMockException;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "customer_phone_number", length = 15, nullable = false)
  private String customerPhoneNumber;

  @Column(name = "customer_name", length = 150, nullable = false)
  private String customerName;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @ManyToOne
  @JoinColumn(
          name = "email_cashier",
          referencedColumnName = "email"
  )
  private User user;

  @OneToMany(mappedBy = "transaction")
  private List<TransactionItem> transactionItems;

}
