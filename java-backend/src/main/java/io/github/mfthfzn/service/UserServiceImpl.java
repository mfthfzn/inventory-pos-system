package io.github.mfthfzn.service;

import io.github.mfthfzn.entity.User;
import io.github.mfthfzn.exception.AuthenticateException;
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

  @Override
  public void changePassword(String email, String newPassword) {
    Optional<User> userOptional = userRepository.findByEmail(email);
    userOptional.ifPresentOrElse(user -> {
      user.setPassword(newPassword);
      userRepository.update(user);
    }, () -> new AuthenticateException(""));
  }

}
