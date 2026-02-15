package io.github.mfthfzn.util;

import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.slf4j.Slf4j;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class RedisUtilTest {

  private RedisCommands<String, String> commands;

  @BeforeEach
  void setUp() {
    commands = RedisUtil.getConnection().sync();
  }

  @Test
  void testGetRedisConnection() {
    assertNotNull(RedisUtil.getConnection());
  }

  @Test
  void testSetAndGetData() {
    commands.set("test", "ini adalah test");
    String result = commands.get("test");
    assertNotNull(result);
    log.info(result);
  }

  @Test
  void testIncrementData() {
    commands.incr("dinpixdinpix@gmail.com");
    String result = commands.get("dinpixdinpix@gmail.com");
    assertNotNull(result);
    log.info(result);
  }
}
