package io.github.mfthfzn.service;

import io.github.mfthfzn.entity.User;
import io.github.mfthfzn.exception.UserNotFoundException;
import io.github.mfthfzn.repository.UserRepository;

import java.util.Optional;

public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User getUser(String email) {
    Optional<User> userByEmail = userRepository.findByEmail(email);
    return userByEmail.orElseThrow(() -> new UserNotFoundException("User not found"));
  }

}
