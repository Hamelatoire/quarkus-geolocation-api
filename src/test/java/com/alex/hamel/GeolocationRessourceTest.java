package com.alex.hamel;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class GeolocationRessourceTest {

  @Test
  @DisplayName("General case: When using a valid IP, should return a valid geolocation")
  void getGeolocation() {
    given()
        .when()
        .pathParam("ip", "76.69.122.95")
        .get("/v1/geolocation/{ip}")
        .then()
        .statusCode(200)
        .body("ip", is("76.69.122.95"))
        .body("country", is("Canada"))
        .body("subdivision", is("Ontario"))
        .body("city", is("Kingston"))
        .body("postal", is("K7M"))
        .body("location", is("Longitude: -76.585 Latitude: 44.2161"));
  }

  @Test
  @DisplayName("Hedge Case: When using an IP that is not in the Database, should return a http 400")
  void getNonExistingGeolocation() {
    given()
        .when()
        .pathParam("ip", "99.99.99.999")
        .get("/v1/geolocation/{ip}")
        .then()
        .statusCode(400);
  }

  @Test
  @DisplayName("Hedge Case: When using an empty IP, should return a http 400")
  void getEmptyGeolocation() {
    given().when().pathParam("ip", " ").get("/v1/geolocation/{ip}").then().statusCode(400);
  }

  @Test
  @DisplayName("Hedge Case: When using an empty IP, should return a http 405")
  void getBlankGeolocation() {
    given().when().pathParam("ip", "").get("/v1/geolocation/{ip}").then().statusCode(405);
  }

  @Test
  @DisplayName("Hedge Case: When using an invalid IP, should return a http 400")
  void getInvalidGeolocation() {
    given().when().pathParam("ip", "invalid ip").get("/v1/geolocation/{ip}").then().statusCode(400);
  }

  @Test
  @DisplayName(
      "General case: When using a list of valid IPs, should return a list of valid geolocation")
  void testPostGeolocation() {
    given()
        .body("[\"76.69.122.95\", \"103.59.72.103\"]")
        .header("Content-Type", "application/json")
        .when()
        .post("/v1/geolocation")
        .then()
        .statusCode(200)
        .body(
            "$.size()",
            is(2),
            "[0].ip",
            is("76.69.122.95"),
            "[0].country",
            is("Canada"),
            "[0].subdivision",
            is("Ontario"),
            "[0].postal",
            is("K7M"),
            "[0].location",
            is("Longitude: -76.585 Latitude: 44.2161"),
            "[0].ip",
            is("76.69.122.95"),
            "[1].country",
            is("India"),
            "[1].subdivision",
            is("West Bengal"),
            "[1].postal",
            is("700054"),
            "[1].location",
            is("Longitude: 88.3832 Latitude: 22.518"));
  }

  @Test
  @DisplayName(
      "Hedge Case: When using a list of valid IPs and on of them is invalid, should return a http"
          + " 400")
  void testPostInvalidGeolocation() {
    given()
        .body("[\"76.69.122.95\", \"invalid\"]")
        .header("Content-Type", "application/json")
        .when()
        .post("/v1/geolocation")
        .then()
        .statusCode(400);
  }

  @Test
  @DisplayName(
      "Hedge Case: When using a list of valid IPs and on of them is empty, should return a http"
          + " 400")
  void testPostEmptyGeolocation() {
    given()
        .body("[\"76.69.122.95\", \"\"]")
        .header("Content-Type", "application/json")
        .when()
        .post("/v1/geolocation")
        .then()
        .statusCode(400);
  }

  @Test
  @DisplayName(
      "Hedge Case: When using a list of valid IPs and on of them is blank, should return a http"
          + " 400")
  void testPostBlankGeolocation() {
    given()
        .body("[\"76.69.122.95\", \" \"]")
        .header("Content-Type", "application/json")
        .when()
        .post("/v1/geolocation")
        .then()
        .statusCode(400);
  }
}
