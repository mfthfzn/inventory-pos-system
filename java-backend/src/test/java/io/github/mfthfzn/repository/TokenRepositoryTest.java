package io.github.mfthfzn.repository;

import io.github.mfthfzn.entity.Store;
import io.github.mfthfzn.entity.Token;
import io.github.mfthfzn.entity.User;
import io.github.mfthfzn.enums.UserType;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(
        MockitoExtension.class
)
public class TokenRepositoryTest {

  @Mock
  EntityManagerFactory entityManagerFactory;

  @Mock
  EntityManager entityManager;

  @Mock
  EntityTransaction transaction;

  @Mock
  Query query;

  @InjectMocks
  TokenRepositoryImpl tokenRepository;

  @BeforeEach
  void setUp() {
    when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
    when(entityManager.getTransaction()).thenReturn(transaction);
  }

  @Test
  void testInsertSuccess() {
    User user = new User();
    String email = "eko@gmail.com";
    user.setEmail(email);
    user.setPassword("rahasia");
    user.setName("Eko Kurniawan Khannedy");
    user.setRole(UserType.CASHIER);
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(LocalDateTime.now());
    user.setStore(new Store(1, "Big Mall", "Jl untung", LocalDateTime.now(), LocalDateTime.now(), null, null));

    Token token = new Token();
    token.setUser(user);
    token.setEmail(email);
    token.setToken(UUID.randomUUID().toString());

    tokenRepository.insert(token);

    verify(transaction, times(1)).begin();
    verify(entityManager, times(1)).persist(token);
    verify(transaction, times(1)).commit();
    verify(entityManager, times(1)).close();
  }

  @Test
  void testInsertFailed() {
    User user = new User();
    String email = "eko@gmail.com";
    user.setEmail(email);
    user.setPassword("rahasia");
    user.setName("Eko Kurniawan Khannedy");
    user.setRole(UserType.CASHIER);
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(LocalDateTime.now());
    user.setStore(new Store(1, "Big Mall", "Jl untung", LocalDateTime.now(), LocalDateTime.now(), null, null));

    Token token = new Token();
    token.setUser(user);
    token.setEmail(email);
    token.setToken(UUID.randomUUID().toString());

    when(transaction.isActive()).thenReturn(true);
    doThrow(PersistenceException.class).when(entityManager).persist(token);

    Assertions.assertThrows(PersistenceException.class, () -> tokenRepository.insert(token));
    verify(transaction, times(1)).isActive();
    verify(transaction, times(1)).rollback();
    verify(entityManager, times(1)).close();
  }

  @Test
  void testFindByEmailSuccess() {
    User user = new User();
    String email = "eko@gmail.com";
    user.setEmail(email);
    user.setPassword("rahasia");
    user.setName("Eko Kurniawan Khannedy");
    user.setRole(UserType.CASHIER);
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(LocalDateTime.now());
    user.setStore(new Store(1, "Big Mall", "Jl untung", LocalDateTime.now(), LocalDateTime.now(), null, null));

    Token token = new Token();
    token.setUser(user);
    token.setEmail(email);
    token.setToken(UUID.randomUUID().toString());

    when(entityManager.find(Token.class, email)).thenReturn(token);
    Optional<Token> byEmail = tokenRepository.findByEmail(email);
    Assertions.assertEquals(email, byEmail.get().getEmail());

    verify(transaction, times(1)).begin();
    verify(entityManager, times(1)).find(Token.class, email);
    verify(transaction, times(1)).commit();
    verify(entityManager, times(1)).close();
  }

  @Test
  void testFindByEmailFailed() {
    User user = new User();
    String email = "eko@gmail.com";
    user.setEmail(email);
    user.setPassword("rahasia");
    user.setName("Eko Kurniawan Khannedy");
    user.setRole(UserType.CASHIER);
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(LocalDateTime.now());
    user.setStore(new Store(1, "Big Mall", "Jl untung", LocalDateTime.now(), LocalDateTime.now(), null, null));

    Token token = new Token();
    token.setUser(user);
    token.setEmail(email);
    token.setToken(UUID.randomUUID().toString());

    when(transaction.isActive()).thenReturn(true);
    doThrow(PersistenceException.class).when(entityManager).find(Token.class, email);

    Assertions.assertThrows(PersistenceException.class, () -> tokenRepository.findByEmail(email));
    verify(transaction, times(1)).isActive();
    verify(transaction, times(1)).rollback();
    verify(entityManager, times(1)).close();
  }

  @Test
  void testRemoveByEmailSuccess() {
    User user = new User();
    String email = "eko@gmail.com";
    user.setEmail(email);
    user.setPassword("rahasia");
    user.setName("Eko Kurniawan Khannedy");
    user.setRole(UserType.CASHIER);
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(LocalDateTime.now());
    user.setStore(new Store(1, "Big Mall", "Jl untung", LocalDateTime.now(), LocalDateTime.now(), null, null));

    Token token = new Token();
    token.setUser(user);
    token.setEmail(email);
    token.setToken(UUID.randomUUID().toString());

    when(entityManager.createQuery("DELETE FROM Token t WHERE t.email = :email")).thenReturn(query);

    when(query.setParameter("email", email)).thenReturn(query);

    when(query.executeUpdate()).thenReturn(1);
    tokenRepository.removeByEmail(email);

    verify(transaction, times(1)).begin();
    verify(entityManager, times(1)).createQuery(contains("DELETE FROM Token t WHERE t.email = :email"));
    verify(transaction, times(1)).commit();
    verify(entityManager, times(1)).close();
  }

  @Test
  void testRemoveByEmailFailed() {
    User user = new User();
    String email = "eko@gmail.com";
    user.setEmail(email);
    user.setPassword("rahasia");
    user.setName("Eko Kurniawan Khannedy");
    user.setRole(UserType.CASHIER);
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(LocalDateTime.now());
    user.setStore(new Store(1, "Big Mall", "Jl untung", LocalDateTime.now(), LocalDateTime.now(), null, null));

    Token token = new Token();
    token.setUser(user);
    token.setEmail(email);
    token.setToken(UUID.randomUUID().toString());

    doThrow(PersistenceException.class).when(entityManager).createQuery(contains("DELETE FROM Token t WHERE t.email = :email"));
    when(transaction.isActive()).thenReturn(true);

    Assertions.assertThrows(PersistenceException.class, () -> {
      tokenRepository.removeByEmail(email);
    });
    verify(transaction, times(1)).isActive();
    verify(transaction, times(1)).rollback();
    verify(entityManager, times(1)).close();
  }
}
