package io.github.mfthfzn.util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Objects;

public class IpUtil {

  private static final List<String> IP_HEADERS = List.of(
          "X-Forwarded-For",
          "Proxy-Client-IP",
          "WL-Proxy-Client-IP",
          "HTTP_CLIENT_IP",
          "HTTP_X_FORWARDED_FOR");

  public static String getClientIpAddress(HttpServletRequest request) {
    return IP_HEADERS.stream()
            .map(request::getHeader)
            .filter(Objects::nonNull)
            .filter(ip -> !ip.isEmpty() && !ip.equalsIgnoreCase("unknown"))
            .findFirst()
            .orElseGet(request::getRemoteAddr);
  }


}
