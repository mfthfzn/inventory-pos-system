package io.github.mfthfzn.listener;

import io.github.mfthfzn.repository.ResetPasswordTokenRepository;
import io.github.mfthfzn.repository.ResetPasswordTokenRepositoryImpl;
import io.github.mfthfzn.util.JpaUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@WebListener
public class ResetPasswordTokenContextListener implements ServletContextListener {

  private final ResetPasswordTokenRepository resetPasswordTokenRepository = new ResetPasswordTokenRepositoryImpl(
          JpaUtil.getEntityManagerFactory()
  );

  private ScheduledExecutorService scheduledExecutorService;

  final static long PERIODIC_TIME = 12L;

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    log.info("Run ResetPasswordTokenCleanUp");

    scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    scheduledExecutorService.scheduleAtFixedRate(
            resetPasswordTokenRepository::cleanUpTokenExpired,
            0, PERIODIC_TIME, TimeUnit.HOURS);
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    log.info("Stop ResetPasswordTokenCleanUp");

    if (scheduledExecutorService != null) scheduledExecutorService.shutdown();
  }
}
