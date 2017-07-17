package org.elasticsearch.client.esquery.bo;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.elasticsearch.client.esquery.model.HttpRequestConfigModel;
import org.elasticsearch.client.esquery.model.SearchRequestByQueryStringModel;
import org.elasticsearch.client.esquery.utils.HttpClientAsyncUtil;
import org.elasticsearch.client.esquery.utils.HttpClientUtil;
import org.elasticsearch.client.esquery.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by Henry Jeong on 2017. 7. 17
 */

@Service("QueryRequestBO")
public class QueryRequestBOImpl implements QueryRequestBO {

  private static final Logger LOGGER = LoggerFactory.getLogger(QueryRequestBOImpl.class);

  private HttpRequestConfigModel httpRequestConfigModel;

  /**
   * @param searchRequestByQueryStringModel request parameter's model
   * @return response object.
   */
  public String actionQueryRequest(SearchRequestByQueryStringModel searchRequestByQueryStringModel) throws IOException {
    CloseableHttpResponse response;
    HttpPost method = new HttpPost(makeTargetParameter(searchRequestByQueryStringModel));
    InputStreamReader reader = null;
    String result;

    if (httpRequestConfigModel == null) {
      return "null";
    }

    StringEntity entity = new StringEntity(
      StringUtil.getObjectMapper().writeValueAsString(searchRequestByQueryStringModel.getQuery()),
      ContentType.create(httpRequestConfigModel.getMimeTypeJson(), Consts.UTF_8));
    RequestConfig requestConfig = RequestConfig.custom()
      .setSocketTimeout(httpRequestConfigModel.getSocketTimeout())
      .setConnectTimeout(httpRequestConfigModel.getConnectionTimeout())
      .setConnectionRequestTimeout(httpRequestConfigModel.getConnectionRequestTimeout())
      .build();

    method.setConfig(requestConfig);
    method.setHeader("Content-type", httpRequestConfigModel.getHeaderContentTypeJsonUtf8());
    entity.setChunked(true);
    method.setEntity(entity);

    response = HttpClientUtil.getClient(0).execute(method);

    try {
      reader = new InputStreamReader(response.getEntity().getContent());
      result = HttpClientUtil.readFully(reader);
    } finally {
      if (reader != null) {
        reader.close();
      }
    }

    method.releaseConnection();

    return result;
  }

  public List<Map<String, Object>> actionMultiQueryRequest(List<SearchRequestByQueryStringModel> models) {
    List<HttpPost> requests = new ArrayList<>();
    List<Map<String, Object>> contents = new ArrayList<>();

    try {
      RequestConfig requestConfig = RequestConfig.custom()
        .setSocketTimeout(httpRequestConfigModel.getSocketTimeout())
        .setConnectTimeout(httpRequestConfigModel.getConnectionTimeout())
        .setConnectionRequestTimeout(httpRequestConfigModel.getConnectionRequestTimeout())
        .build();

      for ( SearchRequestByQueryStringModel model : models ) {
        HttpPost method = new HttpPost(makeTargetParameter(model));

        StringEntity entity = new StringEntity(
          StringUtil.getObjectMapper().writeValueAsString(model.getQuery()),
          ContentType.create(httpRequestConfigModel.getMimeTypeJson(), Consts.UTF_8));

        method.setConfig(requestConfig);
        method.setHeader("Content-type", httpRequestConfigModel.getHeaderContentTypeJsonUtf8());
        entity.setChunked(true);
        method.setEntity(entity);

        requests.add(method);
      }

      CloseableHttpAsyncClient client = HttpClientAsyncUtil.getClient();
      client.start();

      final CountDownLatch latch = new CountDownLatch(requests.size());
      for (final HttpPost request : requests) {
        client.execute(request, new FutureCallback<HttpResponse>() {

          @Override
          public void completed(final HttpResponse response) {
            try {
              InputStreamReader reader;
              reader = new InputStreamReader(response.getEntity().getContent());
              contents.add(StringUtil.getObjectMapper().readValue(reader, new TypeReference<Map<String, Object>>(){}));
            } catch (Exception ex) {
            } finally {
              latch.countDown();
            }
          }

          @Override
          public void failed(final Exception ex) {
            latch.countDown();
          }

          @Override
          public void cancelled() {
            latch.countDown();
          }

        });
      }

      latch.await();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return contents;
  }

  private String makeTargetParameter(SearchRequestByQueryStringModel searchRequestByQueryStringModel) {
    StringBuffer target = new StringBuffer();

    target.append(searchRequestByQueryStringModel.getTarget());
    target.append("?");

    try {
      if (!searchRequestByQueryStringModel.getRouting().isEmpty()) {
        target.append("routing=");
        target.append(URLEncoder.encode(searchRequestByQueryStringModel.getRouting(), "UTF-8"));
        target.append("&");
      }

      if (!searchRequestByQueryStringModel.getPreference().isEmpty()) {
        target.append("preference=");
        target.append(URLEncoder.encode(searchRequestByQueryStringModel.getPreference(), "UTF-8"));
      }

      searchRequestByQueryStringModel.setTarget(target.toString());
    } catch (UnsupportedEncodingException uee) {

    }

    return searchRequestByQueryStringModel.getTarget();
  }

  public HttpRequestConfigModel getHttpRequestConfigModel() {
    return httpRequestConfigModel;
  }

  public void setHttpRequestConfigModel(HttpRequestConfigModel httpRequestConfigModel) {
    this.httpRequestConfigModel = httpRequestConfigModel;
  }
}
