package com.pum.it.pum3d.kernel.utils.exception;

import org.springframework.http.HttpStatus;

public class HttpResponseException extends Exception {
  private static final long serialVersionUID = -5211910442961668775L;

  protected HttpStatus httpStatus;

  public HttpResponseException() {
    super("Une erreur interne est survenue.");
    httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
  }

  public HttpResponseException(String message) {
    super(message);
    httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
  }

  public HttpResponseException(HttpStatus httpStatus) {
    super(httpStatus.getReasonPhrase());
    this.httpStatus = httpStatus;
  }

  public HttpResponseException(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public void setHttpStatus(HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
  }
}
