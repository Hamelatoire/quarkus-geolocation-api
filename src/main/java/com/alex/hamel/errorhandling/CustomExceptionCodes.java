package com.alex.hamel.errorhandling;

import javax.enterprise.context.ApplicationScoped;

/** Class used to export the messages of the custom exceptions */
@ApplicationScoped
public class CustomExceptionCodes {
  public static final String LOADING_DATABASE_INTO_MEMORY_FAILED =
      "Loading database into memory failed";
  public static final String BAD_IP = "Ip is either invalid, null or empty";
  public static final String ERROR_WHILE_READING_DATABASE =
      "An error occured while trying to reading the database";
  public static final String GEOLOCATION_FETCHED_SUCCESSFULLY = "Geolocation fetched successfully";

  private CustomExceptionCodes() {}
}
