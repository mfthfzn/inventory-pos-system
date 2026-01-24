package io.github.mfthfzn.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import io.github.mfthfzn.dto.JwtPayload;
import io.github.mfthfzn.exception.TokenRequiredException;
import io.github.mfthfzn.repository.TokenRepositoryImpl;
import io.github.mfthfzn.service.TokenServiceImpl;
import io.github.mfthfzn.util.JpaUtil;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

@Slf4j
@WebServlet(urlPatterns = "/api/auth/logout")
public class LogoutController extends BaseController {

  private final TokenServiceImpl tokenService =
          new TokenServiceImpl(
                  new TokenRepositoryImpl(JpaUtil.getEntityManagerFactory())
          );

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String refreshToken = getCookieValue(req, "refresh_token");
    if (refreshToken != null && !refreshToken.isBlank()) {
      try {
        JwtPayload jwtPayload = tokenService.getUserFromToken(refreshToken);
        tokenService.removeRefreshToken(jwtPayload.getEmail());
      } catch (JWTVerificationException | PersistenceException exception) {
        log.error(exception.getMessage());
      }
    }

    removeCookie(resp, "access_token");
    removeCookie(resp, "refresh_token");

    sendSuccess(resp, HttpServletResponse.SC_OK, "Success to logout", Map.of(
            "message", "Success to delete jwt token"
    ));
  }

}
