package org.elasticsearch.client.esquery.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * Created by Henry Jeong on 2017. 7. 17
 */
public class HttpClientUtil {
  private static Map<Integer, CloseableHttpClient> httpClientMap = new HashMap<Integer, CloseableHttpClient>();

  static {
    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    cm.setMaxTotal(2000);
    cm.setDefaultMaxPerRoute(1000);
    CloseableHttpClient httpclient = HttpClients.custom()
      .setConnectionManager(cm)
      .build();
    httpClientMap.put(0, httpclient);
  }

  public static CloseableHttpClient getClient() {
    synchronized(httpClientMap) {
      CloseableHttpClient client = httpClientMap.get(0);
      return client;
    }
  }

  public static CloseableHttpClient getClient(int poolKey) {
    synchronized(httpClientMap) {
      CloseableHttpClient client = httpClientMap.get(poolKey);
      if(client == null) {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(2000);
        cm.setDefaultMaxPerRoute(1000);
        client = HttpClients.custom()
          .setConnectionManager(cm)
          .build();

        httpClientMap.put(poolKey, client);
      }
      return client;
    }
  }

  @Override
  protected void finalize() throws Throwable {
    CloseableHttpClient httpclient = getClient();

    httpclient.close();
  }

  public static String readFully(Reader input) throws IOException {
    BufferedReader bufferedReader = input instanceof BufferedReader ? (BufferedReader) input : new BufferedReader(input);
    StringBuffer result = new StringBuffer();
    char[] buffer = new char[4 * 1024];
    int charsRead;

    while ((charsRead = bufferedReader.read(buffer)) != -1) {
      result.append(buffer, 0, charsRead);
    }
    bufferedReader.close();

    return result.toString();
  }
}
