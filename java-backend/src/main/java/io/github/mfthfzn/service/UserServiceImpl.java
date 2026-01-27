package io.github.mfthfzn.service;

import io.github.mfthfzn.entity.User;
import io.github.mfthfzn.repository.UserRepositoryImpl;

import java.util.Optional;

public class UserServiceImpl implements UserService {

  private final UserRepositoryImpl userRepository;

  public UserServiceImpl(UserRepositoryImpl userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User getUser(String email) {
    Optional<User> userByEmail = userRepository.findByEmail(email);
    return userByEmail.orElseThrow();
  }

}
