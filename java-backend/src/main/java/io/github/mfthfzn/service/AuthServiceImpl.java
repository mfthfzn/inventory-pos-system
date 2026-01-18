package io.github.mfthfzn.service;

import io.github.mfthfzn.dto.LoginRequest;
import io.github.mfthfzn.dto.LoginResponse;
import io.github.mfthfzn.dto.JwtPayload;
import io.github.mfthfzn.entity.User;
import io.github.mfthfzn.exception.AuthenticateException;
import io.github.mfthfzn.repository.UserRepositoryImpl;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

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
    try {
      LoginResponse loginResponse = new LoginResponse();
      Optional<User> userByEmail = userRepository.findUserByEmail(loginRequest.getEmail());

      if (userByEmail.isPresent()) {
        User user = userByEmail.get();
        if (loginRequest.getEmail().equals(user.getEmail()) && loginRequest.getPassword().equals(user.getPassword())) {
          JwtPayload jwtPayload =
                  new JwtPayload(
                          user.getEmail(),
                          user.getRole().toString(),
                          user.getName(),
                          user.getStore().getId(),
                          user.getStore().getName()
                  );
          loginResponse.setAccessToken(tokenService.generateAccessToken(jwtPayload));
          loginResponse.setRefreshToken(tokenService.generateRefreshToken(jwtPayload));
          loginResponse.setUser(user);
          return loginResponse;
        }
      }
      throw new AuthenticateException("Email or Password incorrect!");
    } catch (PersistenceException persistenceException) {
      throw new PersistenceException(persistenceException);
    } catch (AuthenticateException authenticateException) {
      throw new AuthenticateException(authenticateException.getMessage());
    }
  }

}
