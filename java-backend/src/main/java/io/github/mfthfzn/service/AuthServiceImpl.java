package io.github.mfthfzn.service;

import io.github.mfthfzn.dto.AuthRequest;
import io.github.mfthfzn.dto.AuthResponse;
import io.github.mfthfzn.dto.JwtPayload;
import io.github.mfthfzn.entity.User;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class AuthServiceImpl implements AuthService {

  private final UserServiceImpl userService;

  private final TokenServiceImpl tokenService;

  public AuthServiceImpl(UserServiceImpl userService, TokenServiceImpl tokenService) {
    this.userService = userService;
    this.tokenService = tokenService;
  }

  @Override
  public AuthResponse authenticate(AuthRequest authRequest) {
    try {
      AuthResponse authResponse = new AuthResponse();
      Optional<User> optionalUser = Optional.ofNullable(userService.getUser(authRequest.getEmail()));

      if (optionalUser.isPresent()) {
        User user = optionalUser.get();
        if (authRequest.getEmail().equals(user.getEmail()) && authRequest.getPassword().equals(user.getPassword())) {
          authResponse.setUserType(user.getRole());
          authResponse.setAuth(true);

          JwtPayload jwtPayload =
                  new JwtPayload(
                          user.getEmail(),
                          user.getRole().toString(),
                          user.getName(),
                          user.getStore().getId(),
                          user.getStore().getName()
                  );

          authResponse.setAccessToken(tokenService.generateAccessToken(jwtPayload));
          authResponse.setRefreshToken(tokenService.generateRefreshToken(jwtPayload));
        } else {
          authResponse.setAuth(false);
        }
      } else {
        authResponse.setAuth(false);
      }
      return authResponse;
    } catch (PersistenceException persistenceException) {
      throw new PersistenceException(persistenceException);
    }
  }

}
