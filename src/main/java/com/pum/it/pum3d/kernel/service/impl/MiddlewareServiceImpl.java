package com.pum.it.pum3d.kernel.service.impl;

import com.pum.it.pum3d.kernel.model.entity.Module;
import com.pum.it.pum3d.kernel.service.IMiddlewareService;
import com.pum.it.pum3d.kernel.service.IModuleService;
import com.pum.it.pum3d.kernel.utils.exception.HttpResponseException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Component
public class MiddlewareServiceImpl implements IMiddlewareService {

  private static final Logger LOGGER = LogManager.getLogger(MiddlewareServiceImpl.class);

  @Autowired
  private IModuleService moduleService;

  @Override
  public ResponseEntity<String> exchange(
      String body,
      HttpMethod httpMethod,
      HttpServletRequest request,
      String targetId)
      throws HttpResponseException {
    String requestPath = request.getRequestURI();
    String routingPath = "/routing/" + targetId;
    requestPath = requestPath.substring(routingPath.length());

    Module hostModule = moduleService.findById(targetId);
    if (hostModule == null) {
      throw new HttpResponseException(HttpStatus.NOT_FOUND);
    }

    URI uri;
    try {
      uri = new URI(hostModule.getUrl() + requestPath + "?" + request.getQueryString());
    } catch (URISyntaxException e) {
      throw new HttpResponseException("Error in URI creation.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    LOGGER.info(uri.toString());

    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    ResponseEntity<String> responseEntity;
    try {
      responseEntity = restTemplate.exchange(uri, httpMethod, new HttpEntity<>(body),
          String.class);
    } catch (RestClientResponseException e) {
      responseEntity = ResponseEntity
          .status(e.getRawStatusCode())
          .body(e.getResponseBodyAsString());
    }
    return responseEntity;
  }
}
