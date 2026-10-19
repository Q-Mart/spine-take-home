package org.spine.utils.exception;

public class ApiEndpointCallException extends RuntimeException {
  int statusCode;

  public ApiEndpointCallException(int statusCode, String message) {
    super(message);
    this.statusCode = statusCode;
  }

  public int getStatusCode() {
    return statusCode;
  }
}
