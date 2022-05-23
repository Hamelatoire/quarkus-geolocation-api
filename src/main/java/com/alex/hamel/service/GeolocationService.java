package com.alex.hamel.service;

import com.alex.hamel.domain.entities.Geolocation;
import com.alex.hamel.domain.repository.GeoIP2Repository;
import com.alex.hamel.errorhandling.CustomException;
import com.alex.hamel.errorhandling.CustomExceptionCodes;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The service that is used to make an abstraction layer between the repository and the geolocation
 * ressource
 */
@ApplicationScoped
public class GeolocationService {
  private static final Logger logger = Logger.getLogger(GeolocationService.class);
  @Inject GeoIP2Repository geoIP2Repository;

  /**
   * Calls the GeoIP2Repository in order to get the geolocalization entity associated to an IP
   * address address
   *
   * @param ip an IP address
   * @return A geolocalization entity
   * @throws IOException
   * @throws GeoIp2Exception
   */
  public Geolocation getGeolocalization(String ip) throws IOException, GeoIp2Exception {
    return geoIP2Repository.getGeolocalization(ip);
  }

  /**
   * Calls the GeoIP2Repository in order to get all the geolocalization associated with a list of ip
   *
   * @param ipList
   * @return A list of geolocaization entities
   */
  public List<Geolocation> batchGetGeolocalization(List<String> ipList) {
    return ipList.stream()
        .map(
            ip -> {
              try {
                return geoIP2Repository.getGeolocalization(ip);
              } catch (IOException e) {
                logger.error(e.getMessage());
                throw new CustomException(CustomExceptionCodes.LOADING_DATABASE_INTO_MEMORY_FAILED);
              } catch (GeoIp2Exception e) {
                logger.error(e.getMessage());
                throw new CustomException(CustomExceptionCodes.ERROR_WHILE_READING_DATABASE);
              }
            })
        .collect(Collectors.toList());
  }
}