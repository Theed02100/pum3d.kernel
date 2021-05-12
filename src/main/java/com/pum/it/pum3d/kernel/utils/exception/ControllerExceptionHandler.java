package com.pum.it.pum3d.kernel.utils.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = {HttpResponseException.class})
  public ResponseEntity<RestExceptionResponse>
      handleHttpResponseException(HttpResponseException exception) {
    return new ResponseEntity<>(new RestExceptionResponse(exception), exception.getHttpStatus());
  }
}
