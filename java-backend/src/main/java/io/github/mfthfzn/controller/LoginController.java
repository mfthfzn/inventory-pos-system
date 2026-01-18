package io.github.mfthfzn.controller;

import io.github.mfthfzn.dto.*;
import io.github.mfthfzn.exception.AuthenticateException;
import io.github.mfthfzn.repository.TokenRepositoryImpl;
import io.github.mfthfzn.repository.UserRepositoryImpl;
import io.github.mfthfzn.service.AuthServiceImpl;
import io.github.mfthfzn.service.TokenServiceImpl;
import io.github.mfthfzn.util.JpaUtil;
import io.github.mfthfzn.util.ValidatorUtil;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@WebServlet(urlPatterns = "/api/auth/login")
public class LoginController extends BaseController {

  private final UserRepositoryImpl userRepository =
          new UserRepositoryImpl(JpaUtil.getEntityManagerFactory());

  private final TokenServiceImpl tokenService =
          new TokenServiceImpl(
                  new TokenRepositoryImpl(JpaUtil.getEntityManagerFactory())
          );

  private final AuthServiceImpl authService =
          new AuthServiceImpl(
                  userRepository, tokenService
          );

  private boolean isDuplicateEntryError(Throwable throwable) {
    Throwable current = throwable;
    while (current != null) {
      String message = current.getMessage();
      if (message != null && (message.contains("Duplicate entry") || message.contains("1062"))) {
        return true;
      }
      current = current.getCause();
    }
    return false;
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    LoginRequest loginRequest = new LoginRequest(req.getParameter("email"), req.getParameter("password"));
    Set<ConstraintViolation<Object>> constraintViolations = ValidatorUtil.validate(loginRequest);

    if (!constraintViolations.isEmpty()) {
      for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
        sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Data request invalid", Map.of(
                "message", constraintViolation.getMessage()
        ));
        break;
      }
      return;
    }

    try {
      LoginResponse loginResponse = authService.authenticate(loginRequest);
      String refreshToken = loginResponse.getRefreshToken();
      String accessToken = loginResponse.getAccessToken();

      if (accessToken != null && refreshToken != null) {
        // Cookie for access-token
        addCookie(resp, "access_token", accessToken, 60 * 60);
        // Cookie for refresh-token
        addCookie(resp, "refresh_token", refreshToken, 60 * 60 * 24 * 7);

        UserResponse userResponse = new UserResponse();
        userResponse.setRole(loginResponse.getUser().getRole().toString());
        sendSuccess(resp, HttpServletResponse.SC_OK, "Login success", userResponse);
      }
    } catch (PersistenceException persistenceException) {
      if (isDuplicateEntryError(persistenceException)) {
        sendError(resp, HttpServletResponse.SC_FOUND, "Redirect", Map.of(
                "message", "The session token already exists."
        ));

        String refreshToken = tokenService.getRefreshToken(req.getParameter("email"));
        JwtPayload jwtPayload = tokenService.getUserFromToken(refreshToken);
        String accessToken = tokenService.generateAccessToken(jwtPayload);

        // Cookie for access-token
        addCookie(resp, "access_token", accessToken, 60 * 60);
        // Cookie for refresh-token
        addCookie(resp, "refresh_token", refreshToken, 60 * 60 * 24 * 7);
      } else {
        sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Login failed", Map.of(
                "message", "An error occurred on the database server."
        ));
      }
    } catch (AuthenticateException authenticateException) {
      sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Login failed", Map.of(
              "message", authenticateException.getMessage()
      ));
    }
  }

}
