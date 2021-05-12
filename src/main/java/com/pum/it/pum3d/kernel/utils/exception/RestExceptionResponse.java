package com.pum.it.pum3d.kernel.utils.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

public class RestExceptionResponse {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
  protected LocalDateTime timestamp;

  protected int status;

  protected String message;

  /**
   * Construit une réponse REST à partir d'un HttpResponseException.
   *
   * @param exception HttpResponseException pour construire la réponse rest
   */
  public RestExceptionResponse(HttpResponseException exception) {
    timestamp = LocalDateTime.now();
    message = exception.getMessage();
    status = exception.getHttpStatus().value();
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "RestExceptionReponse{"
        + "timestamp=" + timestamp
        + ", status=" + status
        + ", message='" + message + '\''
        + '}';
  }
}
