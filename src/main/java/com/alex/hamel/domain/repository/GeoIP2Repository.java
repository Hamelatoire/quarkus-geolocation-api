package com.alex.hamel.domain.repository;

import com.alex.hamel.domain.entities.Geolocation;
import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.Subdivision;
import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Path;

/** Repository that fetches data from the GeoIP2 database */
@ApplicationScoped
public class GeoIP2Repository {
  private static final Logger logger = Logger.getLogger(GeoIP2Repository.class);

  final String databaseFilePath =
      ConfigProvider.getConfig()
          .getOptionalValue("geoip2.database.file-path", String.class)
          .orElse("src/main/resources/");
  final String databaseFileName =
      ConfigProvider.getConfig()
          .getOptionalValue("geoip2.database.file-name", String.class)
          .orElse("src/main/resources/");

  DatabaseReader reader;

  public GeoIP2Repository() throws IOException {
    Path databasePath = Path.of(databaseFilePath);
    File file = databasePath.toAbsolutePath().resolve(databaseFileName).toFile();
    synchronized (this) {
      logger.info("Loading database into memory");
      this.reader = new DatabaseReader.Builder(file).withCache(new CHMCache()).build();
    }
    logger.info("Loaded database into memory successfully");
  }

  /**
   * Reads the GeoIP2 database and returns the geolocalization entity associated to an IP address
   *
   * @param ip the IP address in string format
   * @return the geolocalization that we received by reading the database using the IP address
   */
  public Geolocation getGeolocalization(String ip) throws IOException, GeoIp2Exception {
    logger.info("fetching entity with the following ip: " + ip);

    InetAddress inetAddress = InetAddress.getByName(ip);
    CityResponse response = reader.city(inetAddress);
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
