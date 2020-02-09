package com.laioffer.routepack;

public class Point {
  private String lon;
  private String lat;

  public Point(String lon, String lat) {
    this.lon = lon;
    this.lat = lat;
  }

  public String getLon() {
    return lon;
  }

  public void setLon(String lon) {
    this.lon = lon;
  }

  public String getLat() {
    return lat;
  }

  public void setLat(String lat) {
    this.lat = lat;
  }
}
