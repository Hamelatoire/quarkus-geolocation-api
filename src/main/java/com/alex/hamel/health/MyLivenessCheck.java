package com.alex.hamel.health;

import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Liveness;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

/** Class that is used verify that the app is alive and well. http://localhost:8080/q/health */
@Liveness
@ApplicationScoped
public class MyLivenessCheck implements HealthCheck {

  private static final List<String> CONFIG_LIST = new ArrayList<>();

  static {
    CONFIG_LIST.add("quarkus.http.port");
    CONFIG_LIST.add("geoip2.database.file-path");
  }

  @Override
  public HealthCheckResponse call() {
    HealthCheckResponseBuilder up =
        HealthCheckResponse.named("The server is up with the following configs: ").up();

    for (String configName : CONFIG_LIST) {
      String configs =
          ConfigProvider.getConfig()
              .getOptionalValue(configName, String.class)
              .orElse("AUCUNE VALEUR PRÉSENTE...");
      up.withData(configName, configs);
    }

    return up.build();
  }
}
