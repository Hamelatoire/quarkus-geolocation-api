package com.alex.hamel.errorhandling;

import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;

/**
 * Class that is used to for error handling
 * https://stackoverflow.com/questions/60008540/quarkus-exception-handler
 */
@ApplicationScoped
public class CustomException extends RuntimeException implements Serializable {
  private static final long serialVersionUID = 1L;

  public CustomException() {}

  public CustomException(String message) {
    super(message);
  }

  public CustomException(String message, Throwable cause) {
    super(message, cause);
  }

  public CustomException(Throwable cause) {
    super(cause);
  }

  public CustomException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
