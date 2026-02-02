package io.github.mfthfzn.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import io.github.mfthfzn.dto.UserResponse;
import io.github.mfthfzn.enums.InternalErrorCode;
import io.github.mfthfzn.exception.RefreshTokenExpiredException;
import io.github.mfthfzn.exception.TokenRequiredException;
import io.github.mfthfzn.repository.RefreshTokenRepositoryImpl;
import io.github.mfthfzn.service.TokenService;
import io.github.mfthfzn.service.TokenServiceImpl;
import io.github.mfthfzn.util.JpaUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(urlPatterns = "/api/auth/refresh")
public class RefreshTokenController extends BaseController {

  private final TokenService tokenService =
          new TokenServiceImpl(
                  new RefreshTokenRepositoryImpl(JpaUtil.getEntityManagerFactory())
          );

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      String accessToken = getCookieValue(req, "access_token");
      String refreshToken = getCookieValue(req, "refresh_token");

      if (accessToken == null || refreshToken == null || accessToken.isEmpty() || refreshToken.isEmpty()) {
        throw new TokenRequiredException("Access token and refresh token required");
      }
      tokenService.verifyRefreshToken(refreshToken);

      // cek ke database
      String refreshTokenFromDatabase = tokenService.getRefreshToken(
              tokenService.getUserFromToken(refreshToken).getEmail()
      );

      if (!refreshTokenFromDatabase.equals(refreshToken)) throw new JWTVerificationException("Refresh Token Invalid");

      UserResponse userFromToken = tokenService.getUserFromToken(accessToken);
      String newAccessToken = tokenService.generateAccessToken(userFromToken);

      // Cookie for access-token
      addCookie(resp, "access_token", newAccessToken, 60 * 60);
      sendSuccess(resp, HttpServletResponse.SC_OK, "Success get access token", Map.of(
              "message", "Success add cookie access_token"
      ));
    } catch (TokenRequiredException tokenRequiredException) {
      sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Failed to get data", Map.of(
              "message", tokenRequiredException.getMessage()
      ));
    } catch (RefreshTokenExpiredException refreshTokenExpiredException) {
      sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Failed to get data", Map.of(
              "message", refreshTokenExpiredException.getMessage(),
              "internal_error_code", InternalErrorCode.REFRESH_TOKEN_EXPIRED
      ));
    } catch (JWTVerificationException jwtVerificationException) {
      String refreshToken = getCookieValue(req, "refresh_token");
      tokenService.removeRefreshToken(tokenService.getUserFromToken(refreshToken).getEmail());

      removeCookie(resp, "access_token");
      removeCookie(resp, "refresh_token");

      sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Failed to get data", Map.of(
              "message", jwtVerificationException.getMessage(),
              "internal_error_code", InternalErrorCode.TOKEN_INVALID
      ));
    }
  }
}
