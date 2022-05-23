package com.alex.hamel.domain.repository;

import com.alex.hamel.domain.entities.Geolocation;
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.Subdivision;
import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.net.InetAddress;

/** Repository that fetches data from the GeoIP2 webservice */
@ApplicationScoped
public class GeoIP2Repository {
  private static final Logger logger = Logger.getLogger(GeoIP2Repository.class);
  final Integer accountId =
      ConfigProvider.getConfig()
          .getOptionalValue("geoip2.account.id", Integer.class)
          .orElse(721678);
  final String licenseKey =
      ConfigProvider.getConfig()
          .getOptionalValue("geoip2.liscence.key", String.class)
          .orElse("JgUmqYYhJj0fmxB3");
  private WebServiceClient client;

  public GeoIP2Repository() {
    logger.info("Creating the web service client");
    client = new WebServiceClient.Builder(accountId, licenseKey).host("geolite.info").build();
    logger.info("Client created successfully");
  }

  /**
   * Lookup to the webservice and returns the geolocalization entity associated to an IP address
   *
   * @param ip the IP address in string format
   * @return the geolocalization that we received by reading the webservice using the IP address
   */
  public Geolocation getGeolocalization(String ip) throws IOException, GeoIp2Exception {
    logger.info("fetching entity with the following ip: " + ip);

    InetAddress inetAddress = InetAddress.getByName(ip);
    CityResponse response = client.city(inetAddress);
    Country country = response.getCountry();
    Subdivision subdivision = response.getMostSpecificSubdivision();
    Postal postal = response.getPostal();
    Location location = response.getLocation();

    logger.info("Entity fetched successfully. Ip:" + ip);
    return Geolocation.builder()
        .ip(ip)
        .city(response.getCity().getName())
        .country(country.getName())
        .subdivision(subdivision.getName())
        .postal(postal.getCode())
        .location("Longitude: " + location.getLongitude() + " Latitude: " + location.getLatitude())
        .build();
  }
}
