package com.alex.hamel.errorhandling;

import javax.enterprise.context.ApplicationScoped;

/** Class used to export the messages of the custom exceptions */
@ApplicationScoped
public class CustomExceptionCodes {
  public static final String BAD_IP = "Ip is either invalid, null or empty";
  public static final String IO_ERROR_WHILE_USING_THE_WEB_SERVICE =
      "An IO error occurred while trying to use the data source";
  public static final String ERROR_WHILE_USING_THE_WEB_SERVICE =
      "An error occurred while trying to use the data source";
  public static final String GEOLOCATION_FETCHED_SUCCESSFULLY = "Geolocation fetched successfully";

  private CustomExceptionCodes() {}
}
