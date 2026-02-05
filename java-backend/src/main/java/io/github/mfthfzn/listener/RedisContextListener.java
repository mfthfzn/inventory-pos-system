package io.github.mfthfzn.listener;

import io.github.mfthfzn.util.RedisUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class RedisContextListener implements ServletContextListener {

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    RedisUtil.shutdown();
  }
}
