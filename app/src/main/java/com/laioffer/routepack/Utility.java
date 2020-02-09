package com.laioffer.routepack;

public class Utility {
    public final static String SERVICE_URL = "http://54.153.77.205/GasMasterBackend"; // url of service
    public final static double DEFAULT_LAT = 37.773972;
    public final static double DEFAULT_LNG = -122.431297;
    public final static double DEFAULT_RADIUS = 3000;
    public final static String DEFAULT_KEYWORD = "gas station";

    public final static int SAMPLE_INTERVAL = 300; // Sample a point every 100 points.
    public final static String GOOGLE_MAP_API = "AIzaSyCL4xJcHXUIPcH8plT340HLcmN9pehLPtU";
    public final static String DIRECTION_PREFIX = "https://maps.googleapis.com/maps/api/directions/json?";
    public final static int LONG_DIS = 1; // If a linear distance is larger than 111km, such distance will be defined as 'long'.
}
