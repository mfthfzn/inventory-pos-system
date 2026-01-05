package io.github.mfthfzn.repository;

import io.github.mfthfzn.entity.Store;
import io.github.mfthfzn.util.JpaUtil;
import io.github.mfthfzn.util.JpaUtilTest;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class StoreRepositoryTest extends RepositoryTest {

  final StoreRepository storeRepository = new StoreRepositoryImpl(JpaUtilTest.getEntityManagerFactory());

  @BeforeEach
  void setUp() {
    truncateAllTable("stores");
  }

  @AfterEach
  void tearDown() {
    truncateAllTable("stores");
  }

  @Test
  void testSaveAndFindByName() {

    String name = "Matahari Big Mall";
    String address = "Jl. Untung Suropati No.08, Karang Asam Ulu, Kec. Sungai Kunjang, Kota Samarinda, Kalimantan Timur 75243";
    LocalDateTime createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    LocalDateTime updatedAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

    Store store = new Store();
    store.setName(name);
    store.setAddress(address);
    store.setCreatedAt(createdAt);
    store.setUpdatedAt(updatedAt);

    storeRepository.saveStore(store);
    Optional<Store> resultOptional = storeRepository.findStoreByName(store.getName());
    if (resultOptional.isPresent()) {
      Store result = resultOptional.get();
      Assertions.assertEquals(store.getId(), result.getId());
      Assertions.assertEquals(name, result.getName());
      Assertions.assertEquals(address, result.getAddress());
      Assertions.assertEquals(createdAt, result.getCreatedAt());
      Assertions.assertEquals(updatedAt, result.getUpdatedAt());
    }

  }

  @Test
  void testSaveFindAndRemove() {

    String name = "Matahari Big Mall";
    String address = "Jl. Untung Suropati No.08, Karang Asam Ulu, Kec. Sungai Kunjang, Kota Samarinda, Kalimantan Timur 75243";
    LocalDateTime createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    LocalDateTime updatedAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

    Store store = new Store();
    store.setName(name);
    store.setAddress(address);
    store.setCreatedAt(createdAt);
    store.setUpdatedAt(updatedAt);

    storeRepository.saveStore(store);
    Optional<Store> resultOptional = storeRepository.findStoreByName(store.getName());
    if (resultOptional.isPresent()) {
      Store result = resultOptional.get();
      Assertions.assertEquals(store.getId(), result.getId());
      Assertions.assertEquals(name, result.getName());
      Assertions.assertEquals(address, result.getAddress());
      Assertions.assertEquals(createdAt, result.getCreatedAt());
      Assertions.assertEquals(updatedAt, result.getUpdatedAt());

      transaction.begin();
      entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
      transaction.commit();
      storeRepository.removeStore(result);

      transaction.begin();
      entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
      transaction.commit();

      Assertions.assertThrows(PersistenceException.class, () -> {
        storeRepository.findStoreByName(store.getName());
      });

    }



  }
}
