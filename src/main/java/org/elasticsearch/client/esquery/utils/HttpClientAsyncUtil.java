package org.elasticsearch.client.esquery.utils;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;

/**
 * Created by Henry Jeong on 2017. 7. 17
 */
public class HttpClientAsyncUtil {
  private static Map<Integer, CloseableHttpAsyncClient> httpClientAsyncMap = new HashMap<Integer, CloseableHttpAsyncClient>();

  static {
    DefaultConnectingIOReactor ior;
    PoolingNHttpClientConnectionManager cm;

    try {
      ior = new DefaultConnectingIOReactor();
      cm = new PoolingNHttpClientConnectionManager(ior);
    } catch (IOReactorException e) {
      throw new RuntimeException(e);
    }

    cm.setMaxTotal(2000);
    cm.setDefaultMaxPerRoute(1000);

    CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom().setConnectionManager(cm).build();

    httpClientAsyncMap.put(0, httpclient);
  }

  public static CloseableHttpAsyncClient getClient() {
    synchronized(httpClientAsyncMap) {
      CloseableHttpAsyncClient client = httpClientAsyncMap.get(0);

      return client;
    }
  }

  public static CloseableHttpAsyncClient getClient(int poolKey) {
    synchronized(httpClientAsyncMap) {
      CloseableHttpAsyncClient client = httpClientAsyncMap.get(poolKey);

      if(client == null) {
        try {
          DefaultConnectingIOReactor ior = new DefaultConnectingIOReactor();
          PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ior);
          cm.setMaxTotal(2000);
          cm.setDefaultMaxPerRoute(1000);
          client = HttpAsyncClients.custom().setConnectionManager(cm).build();

          httpClientAsyncMap.put(poolKey, client);
        } catch (IOReactorException e) {
          throw new RuntimeException(e);
        }
      }
      return client;
    }
  }

  @Override
  protected void finalize() throws Throwable {
    CloseableHttpAsyncClient httpclient = getClient();

    httpclient.close();
  }
}
