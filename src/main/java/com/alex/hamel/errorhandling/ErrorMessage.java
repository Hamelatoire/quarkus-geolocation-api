package com.alex.hamel.errorhandling;

import javax.enterprise.context.ApplicationScoped;

/** Class used to represent the message in the custom error class */
@ApplicationScoped
public class ErrorMessage {
  private String message;
  private Boolean status;

  public ErrorMessage(String message, Boolean status) {
    super();
    this.message = message;
    this.status = status;
  }

  public ErrorMessage() {
    super();
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }
}
