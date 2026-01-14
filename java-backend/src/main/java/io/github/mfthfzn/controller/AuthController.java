package io.github.mfthfzn.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mfthfzn.dto.AuthRequest;
import io.github.mfthfzn.dto.AuthResponse;
import io.github.mfthfzn.dto.JwtPayload;
import io.github.mfthfzn.dto.UserResponse;
import io.github.mfthfzn.exception.AccessTokenExpiredException;
import io.github.mfthfzn.exception.RefreshTokenExpiredException;
import io.github.mfthfzn.exception.TokenRequiredException;
import io.github.mfthfzn.repository.TokenRepositoryImpl;
import io.github.mfthfzn.repository.UserRepositoryImpl;
import io.github.mfthfzn.service.AuthServiceImpl;
import io.github.mfthfzn.service.TokenServiceImpl;
import io.github.mfthfzn.service.UserServiceImpl;
import io.github.mfthfzn.util.JpaUtil;
import io.github.mfthfzn.util.JsonUtil;
import io.github.mfthfzn.util.ValidatorUtil;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

@WebServlet(urlPatterns = "/api/session")
@Slf4j
public class AuthController extends HttpServlet {

  private final UserServiceImpl userService =
          new UserServiceImpl(
                  new UserRepositoryImpl(JpaUtil.getEntityManagerFactory())
          );

  private final TokenServiceImpl tokenService =
          new TokenServiceImpl(
                  new TokenRepositoryImpl(JpaUtil.getEntityManagerFactory())
          );

  private final AuthServiceImpl authService =
          new AuthServiceImpl(
                  userService, tokenService
          );

  ObjectMapper objectMapper = JsonUtil.getObjectMapper();

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
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String accessToken = null;
    String refreshToken = null;
    resp.setContentType("application/json");
    UserResponse userResponse = new UserResponse();

    if (req.getCookies() != null) {
      try {
        for (Cookie cookie : req.getCookies()) {
          if (cookie.getName().equals("access_token")) accessToken = cookie.getValue();
          if (cookie.getName().equals("refresh_token")) refreshToken = cookie.getValue();
        }

        if (accessToken == null || refreshToken == null || accessToken.isEmpty() || refreshToken.isEmpty()) {
          throw new TokenRequiredException("Access token and refresh token required");
        }

        tokenService.verifyAccessToken(accessToken);

        JwtPayload jwtPayload = tokenService.getUserFromToken(accessToken);
        userResponse.setMessage("Access token is valid");
        userResponse.setData(Map.of(
                "email", jwtPayload.getEmail(),
                "name", jwtPayload.getName(),
                "role", jwtPayload.getRole(),
                "store_name", jwtPayload.getStoreName()
        ));
        String response = objectMapper.writeValueAsString(userResponse);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(response);
      } catch (AccessTokenExpiredException accessTokenExpiredException) {
        try {

          tokenService.verifyRefreshToken(refreshToken);

          JwtPayload user = tokenService.getUserFromToken(refreshToken);

          // cek ke database
          String refreshTokenFromDatabase = tokenService.getRefreshToken(user.getEmail());

          if (!refreshTokenFromDatabase.equals(refreshToken)) throw new JWTVerificationException("Refresh Token Invalid");

          JwtPayload jwtPayload = tokenService.getUserFromToken(accessToken);
          String newAccessToken = tokenService.generateAccessToken(jwtPayload);

          // Cookie for access-token
          Cookie acessCookie = new Cookie("access_token", newAccessToken);
          acessCookie.setHttpOnly(true);
          acessCookie.setSecure(false);
          acessCookie.setPath("/");
          acessCookie.setMaxAge(60 * 60 * 24);
          resp.addCookie(acessCookie);

          userResponse.setMessage("Access token is valid");
          userResponse.setData(Map.of(
                  "email", jwtPayload.getEmail(),
                  "name", jwtPayload.getName(),
                  "role", jwtPayload.getRole(),
                  "store_name", jwtPayload.getStoreName()
          ));
          String response = objectMapper.writeValueAsString(userResponse);
          resp.setStatus(HttpServletResponse.SC_OK);
          resp.getWriter().println(response);
        }
        catch (RefreshTokenExpiredException refreshTokenExpiredException) {

          JwtPayload jwtPayload = tokenService.getUserFromToken(refreshToken);

          tokenService.removeRefreshToken(jwtPayload, refreshToken);

          Cookie acessCookie = new Cookie("access_token", "");
          acessCookie.setHttpOnly(true);
          acessCookie.setSecure(false);
          acessCookie.setPath("/");
          acessCookie.setMaxAge(0);
          resp.addCookie(acessCookie);

          // Cookie for refresh-token
          Cookie refreshCookie = new Cookie("refresh_token", "");
          refreshCookie.setHttpOnly(true);
          refreshCookie.setSecure(false);
          refreshCookie.setPath("/");
          refreshCookie.setMaxAge(0);
          resp.addCookie(refreshCookie);

          userResponse.setMessage("Failed to get data");
          userResponse.setError(Map.of(
                  "message", refreshTokenExpiredException.getMessage()
          ));
          resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          String response = objectMapper.writeValueAsString(userResponse);
          resp.getWriter().println(response);
        } catch (JWTVerificationException jwtVerificationException) {

          JwtPayload jwtPayload = tokenService.getUserFromToken(refreshToken);

          tokenService.removeRefreshToken(jwtPayload, refreshToken);

          Cookie acessCookie = new Cookie("access_token", "");
          acessCookie.setHttpOnly(true);
          acessCookie.setSecure(false);
          acessCookie.setPath("/");
          acessCookie.setMaxAge(0);
          resp.addCookie(acessCookie);

          // Cookie for refresh-token
          Cookie refreshCookie = new Cookie("refresh_token", "");
          refreshCookie.setHttpOnly(true);
          refreshCookie.setSecure(false);
          refreshCookie.setPath("/");
          refreshCookie.setMaxAge(0);
          resp.addCookie(refreshCookie);

          userResponse.setMessage("Failed to get data");
          userResponse.setError(Map.of(
                  "message", jwtVerificationException.getMessage()
          ));
          resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          String response = objectMapper.writeValueAsString(userResponse);
          resp.getWriter().println(response);
        }
      } catch (TokenRequiredException tokenRequiredException) {

        userResponse.setMessage("Failed to get data");
        userResponse.setError(Map.of(
                "message", tokenRequiredException.getMessage()
        ));
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String response = objectMapper.writeValueAsString(userResponse);
        resp.getWriter().println(response);

      } catch (JWTVerificationException jwtVerificationException) {

        JwtPayload jwtPayload = tokenService.getUserFromToken(refreshToken);

        tokenService.removeRefreshToken(jwtPayload, refreshToken);

        Cookie acessCookie = new Cookie("access_token", "");
        acessCookie.setHttpOnly(true);
        acessCookie.setSecure(false);
        acessCookie.setPath("/");
        acessCookie.setMaxAge(0);
        resp.addCookie(acessCookie);

        // Cookie for refresh-token
        Cookie refreshCookie = new Cookie("refresh_token", "");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0);
        resp.addCookie(refreshCookie);

        userResponse.setMessage("Failed to get data");
        userResponse.setError(Map.of(
                "message", jwtVerificationException.getMessage()
        ));
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String response = objectMapper.writeValueAsString(userResponse);
        resp.getWriter().println(response);
      }
    } else {
      userResponse.setMessage("Failed to get data");
      userResponse.setError(Map.of(
              "message", "Access token and refresh token required"
      ));
      resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      String response = objectMapper.writeValueAsString(userResponse);
      resp.getWriter().println(response);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    AuthRequest authRequest = new AuthRequest(req.getParameter("email"), req.getParameter("password"));
    Set<ConstraintViolation<Object>> constraintViolations = ValidatorUtil.validate(authRequest);
    AuthResponse authResponse = new AuthResponse();
    String response;
    PrintWriter writer = resp.getWriter();
    resp.setContentType("application/json");

    if (!constraintViolations.isEmpty()) {
      for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
        authResponse.setMessage("Data request invalid");
        authResponse.setError(Map.of(
                "message", constraintViolation.getMessage()
        ));
        break;
      }
      response = objectMapper.writeValueAsString(authResponse);
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      writer.println(response);
      return;
    }
    authResponse.setMessage("");

    try {
      authResponse = authService.authenticate(authRequest);
      if (!authResponse.isAuth()) {
        authResponse.setMessage("Login failed");
        authResponse.setError(Map.of(
                "message", "Email or password incorrect!"
        ));
        response = objectMapper.writeValueAsString(authResponse);
        writer.println(response);
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      } else {
        String refreshToken = authResponse.getRefreshToken();
        String accessToken = authResponse.getAccessToken();
        if (accessToken != null && refreshToken != null) {

          // Cookie for access-token
          Cookie acessCookie = new Cookie("access_token", accessToken);
          acessCookie.setHttpOnly(true);
          acessCookie.setSecure(false);
          acessCookie.setPath("/");
          acessCookie.setMaxAge(60 * 60 * 24);
          resp.addCookie(acessCookie);

          // Cookie for refresh-token
          Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
          refreshCookie.setHttpOnly(true);
          refreshCookie.setSecure(false);
          refreshCookie.setPath("/");
          refreshCookie.setMaxAge(60 * 60 * 24 * 30);
          resp.addCookie(refreshCookie);

          resp.setStatus(HttpServletResponse.SC_OK);
          authResponse.setMessage("Login Success!");
          authResponse.setData(Map.of(
                  "role", authResponse.getUserType().toString()
          ));

          response = objectMapper.writeValueAsString(authResponse);
          writer.println(response);
        }
      }
    } catch (PersistenceException persistenceException) {
      if (isDuplicateEntryError(persistenceException)) {
        authResponse.setMessage("Failed login");
        authResponse.setError(Map.of(
                "message", "The session token already exists."
        ));
        resp.setStatus(HttpServletResponse.SC_FOUND);

        String refreshToken = tokenService.getRefreshToken(req.getParameter("email"));
        JwtPayload jwtPayload = tokenService.getUserFromToken(refreshToken);
        String accessToken = tokenService.generateAccessToken(jwtPayload);

        // Cookie for refresh-token
        Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(60 * 60 * 24 * 30);
        resp.addCookie(refreshCookie);

        // Cookie for access-token
        Cookie acessCookie = new Cookie("access_token", accessToken);
        acessCookie.setHttpOnly(true);
        acessCookie.setSecure(false);
        acessCookie.setPath("/");
        acessCookie.setMaxAge(60 * 60 * 24);
        resp.addCookie(acessCookie);
      } else {
        authResponse.setMessage("Failed login");
        authResponse.setError(Map.of(
                "message", "An error occurred on the database server."
        ));
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      }
      response = objectMapper.writeValueAsString(authResponse);
      writer.println(response);
    }
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    String accessToken = null;
    String refreshToken = null;
    resp.setContentType("application/json");

    if (req.getCookies() != null) {
      try {
        for (Cookie cookie : req.getCookies()) {
          if (cookie.getName().equals("access_token")) {
            accessToken = cookie.getValue();
          }
          if (cookie.getName().equals("refresh_token")) {
            refreshToken = cookie.getValue();
          }
        }

        if (accessToken == null || refreshToken == null) {
          throw new JWTVerificationException("Access token and refresh token required");
        }

        JwtPayload jwtPayload = tokenService.getUserFromToken(refreshToken);
        tokenService.removeRefreshToken(jwtPayload, refreshToken);

        Cookie acessCookie = new Cookie("access_token", accessToken);
        acessCookie.setHttpOnly(true);
        acessCookie.setSecure(false);
        acessCookie.setPath("/");
        acessCookie.setMaxAge(0);
        resp.addCookie(acessCookie);

        // Cookie for refresh-token
        Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0);
        resp.addCookie(refreshCookie);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("Success to logout");
        authResponse.setData(Map.of(
                "message", "Success to delete jwt token"
        ));

        String response = objectMapper.writeValueAsString(authResponse);
        resp.getWriter().println(response);
        resp.setStatus(HttpServletResponse.SC_OK);
      } catch (JWTVerificationException | PersistenceException exception) {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("Failed to logout");
        authResponse.setError(Map.of(
                "message", exception.getMessage()
        ));
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String response = objectMapper.writeValueAsString(authResponse);
        resp.getWriter().println(response);
      }
    } else {
      AuthResponse authResponse = new AuthResponse();
      authResponse.setMessage("Failed to logout");
      authResponse.setError(Map.of(
              "message", "Access token and refresh token required"
      ));
      resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      String response = objectMapper.writeValueAsString(authResponse);
      resp.getWriter().println(response);
    }

  }
}
