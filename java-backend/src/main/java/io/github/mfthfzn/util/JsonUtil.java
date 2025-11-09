package io.github.mfthfzn.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Getter;

public class JsonUtil {

  @Getter
  private final static ObjectMapper objectMapper = new ObjectMapper()
          .configure(SerializationFeature.INDENT_OUTPUT, true)
          .setSerializationInclusion(JsonInclude.Include.NON_NULL);

}
