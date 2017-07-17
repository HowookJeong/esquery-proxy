package org.elasticsearch.client.esquery.model;

/**
 * Created by Henry Jeong on 2017. 7. 17
 */
public class HttpRequestConfigModel {
  private int socketTimeout;
  private int connectionTimeout;
  private int connectionRequestTimeout;
  private String headerContentTypeJsonUtf8;
  private String mimeTypeJson;

  public int getSocketTimeout() {
    return socketTimeout;
  }

  public void setSocketTimeout(int socketTimeout) {
    this.socketTimeout = socketTimeout;
  }

  public int getConnectionTimeout() {
    return connectionTimeout;
  }

  public void setConnectionTimeout(int connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  public int getConnectionRequestTimeout() {
    return connectionRequestTimeout;
  }

  public void setConnectionRequestTimeout(int connectionRequestTimeout) {
    this.connectionRequestTimeout = connectionRequestTimeout;
  }

  public String getHeaderContentTypeJsonUtf8() {
    return headerContentTypeJsonUtf8;
  }

  public void setHeaderContentTypeJsonUtf8(String headerContentTypeJsonUtf8) {
    this.headerContentTypeJsonUtf8 = headerContentTypeJsonUtf8;
  }

  public String getMimeTypeJson() {
    return mimeTypeJson;
  }

  public void setMimeTypeJson(String mimeTypeJson) {
    this.mimeTypeJson = mimeTypeJson;
  }
}
