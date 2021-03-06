package com.alex.hamel.errorhandling;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/** Handler for the custom exception */
@Provider
@ApplicationScoped
public class CustomExceptionHandler implements ExceptionMapper<CustomException> {

  @Override
  public Response toResponse(CustomException e) {
    switch (e.getMessage()) {
      case CustomExceptionCodes.IO_ERROR_WHILE_USING_THE_WEB_SERVICE:
      case CustomExceptionCodes.ERROR_WHILE_USING_THE_WEB_SERVICE:
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(new ErrorMessage(e.getMessage(), false))
            .build();
      default:
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(new ErrorMessage(e.getMessage(), false))
            .build();
    }
  }
}
