package io.github.mfthfzn.repository;

import io.github.mfthfzn.entity.Store;

import java.util.Optional;

public interface StoreRepository {

  void saveStore(Store store);

  Optional<Store> findStoreByName(String name);

  void removeStore(Store store);
}
