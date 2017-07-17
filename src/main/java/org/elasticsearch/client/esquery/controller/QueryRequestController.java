package org.elasticsearch.client.esquery.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.elasticsearch.client.esquery.bo.QueryRequestBO;
import org.elasticsearch.client.esquery.model.SearchRequestByQueryStringModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Henry Jeong on 2017. 7. 17
 */

@Controller
public class QueryRequestController {

  private static final Logger LOGGER = LoggerFactory.getLogger(QueryRequestController.class);

  @Autowired
  private QueryRequestBO queryRequestBO;

  @RequestMapping(value = "/query", method = {RequestMethod.POST})
  @ResponseBody
  public ResponseEntity<String> SearchRequestByQueryString(
    @RequestBody SearchRequestByQueryStringModel searchRequestByQueryStringModel
  ) {
    String result = "";

    try {
      result = queryRequestBO.actionQueryRequest(searchRequestByQueryStringModel);
    } catch (IOException e) {
      e.printStackTrace();
    }

    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

    return new ResponseEntity<>(result, httpHeaders, HttpStatus.OK);
  }

  @RequestMapping(value = "/mquery", method = {RequestMethod.POST})
  @ResponseBody
  public ResponseEntity<List<Map<String, Object>>> MultiSearchRequestByQueryString(
    @RequestBody List<SearchRequestByQueryStringModel> mquery
  ) throws Exception {
    List<Map<String, Object>> result = new ArrayList<>();

    try {
      result = queryRequestBO.actionMultiQueryRequest(mquery);
    } catch (IOException e) {
      e.printStackTrace();
    }

    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

    return new ResponseEntity<>(result, httpHeaders, HttpStatus.OK);
  }
}
