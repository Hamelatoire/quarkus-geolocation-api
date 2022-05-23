package com.alex.hamel.resource;

import com.alex.hamel.domain.entities.Geolocation;
import com.alex.hamel.errorhandling.CustomException;
import com.alex.hamel.errorhandling.CustomExceptionCodes;
import com.alex.hamel.service.GeolocationService;
import com.google.common.net.InetAddresses;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

/**
 * The geolocation resource is where the routes of the geolocation api lives. We use the Jackson
 * library to serialize and deserialize the application/json that is consumed or produced by this
 * api.
 */
@Tag(name = "GeolocationResource", description = "Api that return Geolocations from an IP address")
@Path("/v1/geolocation")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class GeolocationResource {
  private static final Logger logger = Logger.getLogger(GeolocationResource.class);
  @Inject GeolocationService geolocationService;

  private void validateIP(String ip) {
    if (ip.isBlank() || !InetAddresses.isInetAddress(ip)) {
      logger.error(CustomExceptionCodes.BAD_IP);
      throw new CustomException(CustomExceptionCodes.BAD_IP);
    }
  }

  @GET
  @Path("/{ip}")
  @Operation(summary = "Return the geolocation linked to a ip address")
  @APIResponse(
      responseCode = "200",
      description = CustomExceptionCodes.GEOLOCATION_FETCHED_SUCCESSFULLY)
  @APIResponse(responseCode = "400", description = CustomExceptionCodes.BAD_IP)
  @APIResponse(
      responseCode = "500",
      description = CustomExceptionCodes.ERROR_WHILE_USING_THE_WEB_SERVICE)
  public Response getGeolocation(@PathParam("ip") String ip) {
    this.validateIP(ip);
    Geolocation geolocation = null;
    try {
      geolocation = geolocationService.getGeolocalization(ip);
    } catch (IOException e) {
      logger.error(e.getMessage());
      throw new CustomException(CustomExceptionCodes.IO_ERROR_WHILE_USING_THE_WEB_SERVICE);
    } catch (GeoIp2Exception e) {
      logger.error(e.getMessage());
      throw new CustomException(CustomExceptionCodes.ERROR_WHILE_USING_THE_WEB_SERVICE);
    }
    return Response.status(Response.Status.OK).entity(geolocation).build();
  }

  @POST
  @Operation(summary = "Return the geolocations linked to a list of ip addresses")
  @Consumes(MediaType.APPLICATION_JSON)
  @APIResponse(
      responseCode = "200",
      description = CustomExceptionCodes.GEOLOCATION_FETCHED_SUCCESSFULLY)
  @APIResponse(responseCode = "400", description = CustomExceptionCodes.BAD_IP)
  @APIResponse(
      responseCode = "500",
      description = CustomExceptionCodes.ERROR_WHILE_USING_THE_WEB_SERVICE)
  public Response getGeolocations(List<String> ipList) {
    ipList.forEach(this::validateIP);
    List<Geolocation> geolocationList = null;
    geolocationList = geolocationService.batchGetGeolocalization(ipList);
    return Response.status(Response.Status.OK).entity(geolocationList).build();
  }
}
