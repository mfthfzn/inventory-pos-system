package io.github.mfthfzn.repository;

import io.github.mfthfzn.entity.RefreshToken;
import io.github.mfthfzn.entity.User;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(
        MockitoExtension.class
)
public class RefreshTokenRepositoryTest {

  @Mock
  private EntityManagerFactory entityManagerFactory;

  @Mock
  private EntityManager entityManager;

  @Mock
  private EntityTransaction transaction;

  @Mock
  private Query query;

  private final RefreshToken refreshToken = new RefreshToken();

  private final User user = new User();

  private final String email = "eko@gmail.com";

  @InjectMocks
  RefreshTokenRepositoryImpl tokenRepository;

  @BeforeEach
  void setUp() {
    when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
    when(entityManager.getTransaction()).thenReturn(transaction);
    user.setEmail(email);
    refreshToken.setUser(user);
    refreshToken.setEmail(email);
  }

  @Test
  void testInsertSuccess() {

    tokenRepository.insert(refreshToken);

    verify(transaction, times(1)).begin();
    verify(entityManager, times(1)).persist(refreshToken);
    verify(transaction, times(1)).commit();
    verify(entityManager, times(1)).close();
  }

  @Test
  void testInsertFailed() {

    when(transaction.isActive()).thenReturn(true);
    doThrow(PersistenceException.class).when(entityManager).persist(refreshToken);

    Assertions.assertThrows(PersistenceException.class, () -> tokenRepository.insert(refreshToken));
    verify(transaction, times(1)).isActive();
    verify(transaction, times(1)).rollback();
    verify(entityManager, times(1)).close();
  }

  @Test
  void testFindByEmailSuccess() {

    when(entityManager.find(RefreshToken.class, email)).thenReturn(refreshToken);
    Optional<RefreshToken> byEmail = tokenRepository.findByEmail(email);
    Assertions.assertEquals(email, byEmail.get().getEmail());

    verify(transaction, times(1)).begin();
    verify(entityManager, times(1)).find(RefreshToken.class, email);
    verify(transaction, times(1)).commit();
    verify(entityManager, times(1)).close();
  }

  @Test
  void testFindByEmailFailed() {

    when(transaction.isActive()).thenReturn(true);
    doThrow(PersistenceException.class).when(entityManager).find(RefreshToken.class, email);

    Assertions.assertThrows(PersistenceException.class, () -> tokenRepository.findByEmail(email));
    verify(transaction, times(1)).isActive();
    verify(transaction, times(1)).rollback();
    verify(entityManager, times(1)).close();
  }

  @Test
  void testRemoveByEmailSuccess() {

    when(entityManager.createQuery("DELETE FROM RefreshToken t WHERE t.email = :email")).thenReturn(query);

    when(query.setParameter("email", email)).thenReturn(query);

    when(query.executeUpdate()).thenReturn(1);
    tokenRepository.removeByEmail(email);

    verify(transaction, times(1)).begin();
    verify(entityManager, times(1)).createQuery(contains("DELETE FROM RefreshToken t WHERE t.email = :email"));
    verify(transaction, times(1)).commit();
    verify(entityManager, times(1)).close();
  }

  @Test
  void testRemoveByEmailFailed() {

    doThrow(PersistenceException.class).when(entityManager).createQuery(contains("DELETE FROM RefreshToken t WHERE t.email = :email"));
    when(transaction.isActive()).thenReturn(true);

    Assertions.assertThrows(PersistenceException.class, () -> {
      tokenRepository.removeByEmail(email);
    });
    verify(transaction, times(1)).isActive();
    verify(transaction, times(1)).rollback();
    verify(entityManager, times(1)).close();
  }
}
