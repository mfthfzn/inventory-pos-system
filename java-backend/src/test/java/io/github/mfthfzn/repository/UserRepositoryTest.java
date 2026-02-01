package io.github.mfthfzn.repository;

import io.github.mfthfzn.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
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
@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

  @Mock
  private EntityManagerFactory entityManagerFactory;

  @Mock
  private EntityManager entityManager;

  @Mock
  private EntityTransaction transaction;

  @InjectMocks
  private UserRepositoryImpl userRepository;

  private final User user = new User();

  private final String email = "eko@gmail.com";

  @BeforeEach
  void setUp() {
    when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
    when(entityManager.getTransaction()).thenReturn(transaction);
    user.setEmail(email);
  }

  @Test
  void testInsertSuccess() {

    userRepository.insert(user);

    verify(transaction, times(1)).begin();
    verify(entityManager, times(1)).persist(user);
    verify(transaction, times(1)).commit();
    verify(entityManager, times(1)).close();
  }

  @Test
  void testInsertFailed() {

    when(transaction.isActive()).thenReturn(true);
    doThrow(PersistenceException.class).when(entityManager).persist(user);

    Assertions.assertThrows(PersistenceException.class, () -> userRepository.insert(user));
    verify(transaction, times(1)).isActive();
    verify(transaction, times(1)).rollback();
    verify(entityManager, times(1)).close();

  }

  @Test
  void testFindByEmailSuccess() {

    when(entityManager.find(User.class, email)).thenReturn(user);
    Optional<User> byEmail = userRepository.findByEmail(email);

    Assertions.assertEquals(email, byEmail.get().getEmail());
    verify(transaction, times(1)).begin();
    verify(entityManager, times(1)).find(User.class, email);
    verify(transaction, times(1)).commit();
    verify(entityManager, times(1)).close();
  }

  @Test
  void testFindByEmailFailed() {

    when(transaction.isActive()).thenReturn(true);
    doThrow(PersistenceException.class).when(entityManager).find(User.class, email);

    Assertions.assertThrows(PersistenceException.class, () -> {
      userRepository.findByEmail(email);
    });

    verify(transaction, times(1)).isActive();
    verify(transaction, times(1)).rollback();
    verify(entityManager, times(1)).close();
  }

  @Test
  void testUpdateSuccess() {

    userRepository.update(user);

    verify(transaction, times(1)).begin();
    verify(entityManager, times(1)).merge(user);
    verify(transaction, times(1)).commit();
    verify(entityManager, times(1)).close();
  }

  @Test
  void testUpdateFailed() {

    when(transaction.isActive()).thenReturn(true);
    doThrow(PersistenceException.class).when(entityManager).merge(user);

    Assertions.assertThrows(PersistenceException.class, () -> {
      userRepository.update(user);
    });

    verify(transaction, times(1)).isActive();
    verify(transaction, times(1)).rollback();
    verify(entityManager, times(1)).close();
  }
}
