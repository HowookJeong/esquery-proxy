package org.elasticsearch.client.esquery.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by Henry Jeong on 2017. 7. 17
 */
public class StringUtil {

  private static volatile ObjectMapper objectMapper;

  public static ObjectMapper getObjectMapper() {
    if (objectMapper == null) {
      synchronized (StringUtil.class) {
        if (objectMapper == null) {
          objectMapper = new ObjectMapper();
        }
      }
    }

    return objectMapper;
  }
}
