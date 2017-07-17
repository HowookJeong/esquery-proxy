package org.elasticsearch.client.esquery.model;

import java.io.Serializable;

/**
 * Created by Henry Jeong on 2017. 7. 17
 */
public class SearchRequestByQueryStringModel implements Serializable {
  private String target;
  private Object query;
  private String routing;
  private String preference;

  public SearchRequestByQueryStringModel () {
    this.routing = "";
    this.preference = "";
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public Object getQuery() {
    return query;
  }

  public void setQuery(Object query) {
    this.query = query;
  }

  public String getRouting() {
    return routing;
  }

  public void setRouting(String routing) {
    this.routing = routing;
  }

  public String getPreference() {
    return preference;
  }

  public void setPreference(String preference) {
    this.preference = preference;
  }
}
