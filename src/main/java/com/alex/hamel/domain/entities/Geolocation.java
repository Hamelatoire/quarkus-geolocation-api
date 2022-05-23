package com.alex.hamel.domain.entities;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Builder;
import lombok.Getter;

/** Entity model that is used to as the return type of the geolocation resource's methods */
@Builder
@Getter
@RegisterForReflection
public class Geolocation {
  private String ip;
  private String country;
  private String subdivision;
  private String city;
  private String postal;
  private String location;
}
