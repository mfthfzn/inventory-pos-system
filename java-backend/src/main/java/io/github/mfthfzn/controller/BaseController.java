package io.github.mfthfzn.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mfthfzn.dto.WebResponse;
import io.github.mfthfzn.util.JsonUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class BaseController extends HttpServlet {

  ObjectMapper objectMapper = JsonUtil.getObjectMapper();

  protected void removeCookie(HttpServletResponse resp, String cookieName) {
    Cookie refreshCookie = new Cookie(cookieName, "");
    refreshCookie.setHttpOnly(true);
    refreshCookie.setSecure(false);
    refreshCookie.setPath("/");
    refreshCookie.setMaxAge(0);
    resp.addCookie(refreshCookie);
  }

  protected String getCookieValue(HttpServletRequest req, String cookieName) {
    if (req.getCookies() == null) return null;
    for (Cookie reqCookie : req.getCookies()) {
      if (reqCookie.getName().equals(cookieName)) return reqCookie.getValue();
    }
    return null;
  }

  protected void addCookie(HttpServletResponse resp, String cookieName, String value, int maxAge) {
    Cookie acessCookie = new Cookie(cookieName, value);
    acessCookie.setHttpOnly(true);
    acessCookie.setSecure(false);
    acessCookie.setPath("/");
    acessCookie.setMaxAge(maxAge);
    resp.addCookie(acessCookie);
  }

  protected <T> void sendSuccess(HttpServletResponse resp, int statusCode, String message, T data) throws IOException {
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    resp.setStatus(statusCode);

    WebResponse<T> webResponse = new WebResponse<>(message, data, null);
    resp.getWriter().println(objectMapper.writeValueAsString(webResponse));
  }

  protected void sendError(HttpServletResponse resp, int statusCode, String message, Object error) throws IOException {
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    resp.setStatus(statusCode);

    WebResponse<Object> webResponse = new WebResponse<>(message, null, error);
    resp.getWriter().println(objectMapper.writeValueAsString(webResponse));
  }

}
