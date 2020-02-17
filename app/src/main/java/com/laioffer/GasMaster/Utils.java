package com.laioffer.GasMaster;

import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

public class Utils {
  public static final String BASE_URL = "https://maps.googleapis.com/maps/api/";
  public static final String DIRECTION_URL = "directions/json";
  public static final String API_KEY = "AIzaSyApGfRmjILvEs79jBt8pVDugdGKMQvZfjs";
  public static final String PARAM = "?origin=Toront&destination=Mississauga&key=";
  public static final String MAP_URL = "https://www.google.com/maps/dir/?api=1";


  static String getDirectionUrl(String origin, String destination) {

    StringBuilder urlBuilder = new StringBuilder()
      .append(BASE_URL)
      .append(DIRECTION_URL)
      .append("?")
      .append("origin=")
      .append(origin)
      .append("&")
      .append("destination=")
      .append(destination);
    urlBuilder.append("&key=").append(API_KEY);
    return urlBuilder.toString();
  }

  static String getDirectionUrl(LatLng origin, LatLng destination, List<LatLng> waypoints) {

    StringBuilder urlBuilder = new StringBuilder()
      .append(BASE_URL)
      .append(DIRECTION_URL)
      .append("?")
      .append("origin=")
      .append(origin.latitude).append(',').append(origin.longitude)
      .append("&")
      .append("destination=")
      .append(destination.latitude).append(',').append(destination.longitude);

    if (waypoints.size() > 0) {
      urlBuilder.append("&waypoints=enc:")
        .append(PolyUtil.encode(waypoints))
        .append(":");
    }
    Log.d("Url", PolyUtil.encode(waypoints));
    urlBuilder.append("&key=").append(API_KEY);
    Log.d("Test Url", urlBuilder.toString());
    return urlBuilder.toString();
  }

  static String getDirectionUrl(List<LatLng> points) {
    LatLng origin = points.get(0);
    LatLng destination = points.get(points.size() - 1);
    List<LatLng> waypoints = new ArrayList<>();
    for (int i = 1; i < points.size() - 1; i++) {
      waypoints.add(points.get(i));
    }
    return getDirectionUrl(origin, destination, waypoints);
  }


  static Intent sendToGoogleMapAppIntent(List<LatLng> points) {
    if (points == null || points.size() < 2) {
      Log.d("Error", "Invalid input points calling sendToGoogleMapApp");
      return null;
    }

    // separate origin and destination and waypoints
    LatLng origin = points.get(0);
    LatLng destination = points.get(points.size() - 1);
    List<LatLng> waypoints = new ArrayList<>();
    for (int i = 1; i < points.size() - 1; i++) {
      waypoints.add(points.get(i));
    }

    // Generate Maps Urls for Intent
    Uri location = Uri.parse(getMapsUrl(origin, destination, waypoints));

    Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
    return mapIntent;
  }

  static String getMapsUrl(LatLng origin, LatLng destination, List<LatLng> waypoints) {

    StringBuilder urlBuilder = new StringBuilder()
      .append(MAP_URL)
      .append("&origin=")
      .append(origin.latitude)
      .append(',')
      .append(origin.longitude)
      .append("&destination=")
      .append(destination.latitude)
      .append(',')
      .append(destination.longitude);

    if (waypoints.size() > 0) {
      urlBuilder.append("&waypoints=");
      for (int i = 0; i < waypoints.size(); i++) {
        if (i != 0) {
          urlBuilder.append('|');
        }
        urlBuilder
          .append(waypoints.get(i).latitude)
          .append(',')
          .append(waypoints.get(i).longitude);
      }
    }
    Log.d("Map Url", urlBuilder.toString());
    return urlBuilder.toString();
  }

  static String getMapsUrl(List<LatLng> points) {
    if (points == null || points.size() < 2) {
      Log.d("Error", "Invalid input points calling sendToGoogleMapApp");
      return null;
    }

    // separate origin and destination and waypoints
    LatLng origin = points.get(0);
    LatLng destination = points.get(points.size() - 1);
    List<LatLng> waypoints = new ArrayList<>();
    for (int i = 1; i < points.size() - 1; i++) {
      waypoints.add(points.get(i));
    }
    return getMapsUrl(origin, destination, waypoints);
  }


}
