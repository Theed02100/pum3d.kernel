package com.pum.it.pum3d.kernel.service;

import com.pum.it.pum3d.kernel.utils.exception.HttpResponseException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public interface IMiddlewareService {
  ResponseEntity<String> exchange(String body,
                                  HttpMethod httpMethod,
                                  HttpServletRequest request,
                                  String targetId
  )
      throws HttpResponseException;
}
