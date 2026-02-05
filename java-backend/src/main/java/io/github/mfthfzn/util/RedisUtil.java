package io.github.mfthfzn.util;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
public class RedisUtil {

  private static RedisClient redisClient;

  @Getter
  private static final StatefulRedisConnection<String, String> connection;

  static {
    RedisURI redisURI = RedisURI.builder()
            .withHost("localhost")
            .withPort(6379)
            .withDatabase(0)
            .withTimeout(Duration.ofSeconds(5))
            .build();

    redisClient = RedisClient.create(redisURI);
    connection = redisClient.connect();
  }

  public static void shutdown() {
    connection.close();
    redisClient.close();
    log.info("Close redis connection");
  }

}
