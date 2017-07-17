package org.elasticsearch.client.esquery.bo;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.elasticsearch.client.esquery.model.SearchRequestByQueryStringModel;

/**
 * Created by Henry Jeong on 2017. 7. 17
 */
public interface QueryRequestBO {
  String actionQueryRequest(SearchRequestByQueryStringModel model) throws IOException;
  List<Map<String, Object>> actionMultiQueryRequest(List<SearchRequestByQueryStringModel> models) throws IOException;
}
