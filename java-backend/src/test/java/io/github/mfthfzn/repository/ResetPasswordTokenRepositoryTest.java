package io.github.mfthfzn.repository;

import io.github.mfthfzn.entity.ResetPasswordToken;
import io.github.mfthfzn.entity.User;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@ExtendWith({
        MockitoExtension.class
})
public class ResetPasswordTokenRepositoryTest {

  @Mock
  private EntityManagerFactory entityManagerFactory;

  @Mock
  private EntityManager entityManager;

  @Mock
  private EntityTransaction transaction;

  @Mock
  private TypedQuery<ResetPasswordToken> typedQuery;

  @Mock
  private Query query;

  @InjectMocks
  private ResetPasswordTokenRepositoryImpl resetPasswordTokenRepository;

  private final ResetPasswordToken resetPasswordToken = new ResetPasswordToken();

  private final User user = new User();

  private final String email = "eko@gmail.com";

  private final String token = UUID.randomUUID().toString();

  @BeforeEach
  void setUp() {
    when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
    when(entityManager.getTransaction()).thenReturn(transaction);

    user.setEmail(email);
    resetPasswordToken.setUser(user);
    resetPasswordToken.setEmail(email);
    resetPasswordToken.setToken(token);
  }

  @Test
  void testInsertSuccess() {
    resetPasswordTokenRepository.insert(resetPasswordToken);

    verify(transaction, times(1)).begin();
    verify(entityManager, times(1)).persist(resetPasswordToken);
    verify(transaction, times(1)).commit();
    verify(entityManager, times(1)).close();
  }

  @Test
  void testInsertFailed() {
    when(transaction.isActive()).thenReturn(true);
    doThrow(PersistenceException.class).when(entityManager).persist(resetPasswordToken);

    Assertions.assertThrows(PersistenceException.class, () -> resetPasswordTokenRepository.insert(resetPasswordToken));
    verify(transaction, times(1)).isActive();
    verify(transaction, times(1)).rollback();
    verify(entityManager, times(1)).close();
  }

  @Test
  void testFindByTokenSuccess() {

    String sql = "SELECT t FROM ResetPasswordToken t WHERE t.token = :token";

    when(entityManager.createQuery(ArgumentMatchers.eq(sql), ArgumentMatchers.eq(ResetPasswordToken.class)))
            .thenReturn(typedQuery);

    when(typedQuery.setParameter("token", token)).thenReturn(typedQuery);

    when(typedQuery.getSingleResult()).thenReturn(resetPasswordToken);

    Optional<ResetPasswordToken> byToken = resetPasswordTokenRepository.findByToken(token);
    Assertions.assertEquals(token, byToken.get().getToken());

    verify(transaction, times(1)).begin();
    verify(entityManager, times(1)).createQuery(ArgumentMatchers.eq(sql), ArgumentMatchers.eq(ResetPasswordToken.class));
    verify(transaction, times(1)).commit();
    verify(entityManager, times(1)).close();
  }

  @Test
  void testFindByTokenFailed() {

    String sql = "SELECT t FROM ResetPasswordToken t WHERE t.token = :token";

    when(entityManager.createQuery(ArgumentMatchers.eq(sql), ArgumentMatchers.eq(ResetPasswordToken.class)))
            .thenReturn(typedQuery);

    when(typedQuery.setParameter("token", token)).thenReturn(typedQuery);

    when(transaction.isActive()).thenReturn(true);
    doThrow(PersistenceException.class).when(typedQuery).getSingleResult();

    Assertions.assertThrows(PersistenceException.class, () -> resetPasswordTokenRepository.findByToken(token));
    verify(transaction, times(1)).isActive();
    verify(transaction, times(1)).rollback();
    verify(entityManager, times(1)).close();
  }

  @Test
  void testRemoveByEmailSuccess() {
    when(entityManager.createQuery("DELETE FROM ResetPasswordToken t WHERE t.email = :email")).thenReturn(query);

    when(query.setParameter("email", email)).thenReturn(query);

    when(query.executeUpdate()).thenReturn(1);
    resetPasswordTokenRepository.removeByEmail(email);

    verify(transaction, times(1)).begin();
    verify(entityManager, times(1)).createQuery(contains("DELETE FROM ResetPasswordToken t WHERE t.email = :email"));
    verify(transaction, times(1)).commit();
    verify(entityManager, times(1)).close();
  }

  @Test
  void testRemoveByEmailFailed() {
    doThrow(PersistenceException.class).when(entityManager).createQuery(contains("DELETE FROM ResetPasswordToken t WHERE t.email = :email"));
    when(transaction.isActive()).thenReturn(true);

    Assertions.assertThrows(PersistenceException.class, () -> {
      resetPasswordTokenRepository.removeByEmail(email);
    });
    verify(transaction, times(1)).isActive();
    verify(transaction, times(1)).rollback();
    verify(entityManager, times(1)).close();
  }

}
