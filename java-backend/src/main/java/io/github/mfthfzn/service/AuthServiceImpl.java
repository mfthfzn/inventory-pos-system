package io.github.mfthfzn.service;

import io.github.mfthfzn.dto.LoginRequest;
import io.github.mfthfzn.dto.LoginResponse;
import io.github.mfthfzn.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class AuthServiceImpl implements AuthService {

  private final UserServiceImpl userService;

  private final SessionServiceImpl sessionService;

  public AuthServiceImpl(UserServiceImpl userService, SessionServiceImpl sessionService) {
    this.userService = userService;
    this.sessionService = sessionService;
  }

  @Override
  public LoginResponse login(LoginRequest loginRequest) {
    LoginResponse loginResponse = new LoginResponse();
    Optional<User> optionalUser = Optional.ofNullable(userService.getUser(loginRequest.getEmail()));
    log.info(String.valueOf(optionalUser.isPresent()));

    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      if (loginRequest.getEmail().equals(user.getEmail()) && loginRequest.getPassword().equals(user.getPassword())) {
        String token = sessionService.generateToken(optionalUser.get());

        loginResponse.setAuth(true);
        loginResponse.setRole(user.getRole());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setName(user.getName());
        loginResponse.setToken(token);
      } else {
        loginResponse.setAuth(false);
      }
    } else {
      loginResponse.setAuth(false);
    }
    return loginResponse;
  }


}
