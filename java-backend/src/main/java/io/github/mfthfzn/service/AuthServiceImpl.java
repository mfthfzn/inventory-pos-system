package io.github.mfthfzn.service;

import io.github.mfthfzn.dto.LoginRequest;
import io.github.mfthfzn.dto.LoginResponse;
import io.github.mfthfzn.entity.User;
import io.github.mfthfzn.exception.AuthenticateException;
import io.github.mfthfzn.repository.UserRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;

@Slf4j
public class AuthServiceImpl implements AuthService {

  private final UserRepositoryImpl userRepository;

  private final TokenServiceImpl tokenService;

  public AuthServiceImpl(UserRepositoryImpl userRepository, TokenServiceImpl tokenService) {
    this.userRepository = userRepository;
    this.tokenService = tokenService;
  }

  @Override
  public LoginResponse authenticate(LoginRequest loginRequest) {
    User user = userRepository.findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new AuthenticateException("Email or Password incorrect"));

    if (!BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
      throw new AuthenticateException("Email or Password incorrect");
    }
    return new LoginResponse(null, null, user);
  }

  @Override
  public void changePassword(String email, String newPassword) {
    User user = userRepository.findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new AuthenticateException("Email or Password incorrect"));
  }

}
