package com.laioffer.routepack;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.laioffer.routepack.Utility.SAMPLE_INTERVAL;



public class Route {

    List<LatLng> points; // Sampling points on a route btw source and dest.

    Route() {
        points = new ArrayList<>();
    }

    /**
     * Sample points from the original route.
     */
    public List<LatLng> getRoute(String data) {
        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try {
            jObject = new JSONObject(data);
            DirectionsJSONParser parser = new DirectionsJSONParser();

            routes = parser.parse(jObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < routes.size(); i++) {

            List<HashMap<String, String>> path = routes.get(i);

            for (int j = 0; j < path.size(); j++) {
                // 13.7 mile has 863 points. 100 points represents for 1.58 mile.
                if (j % SAMPLE_INTERVAL == 0) { // sample at every 1.58 mile
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }
            }
        }
        return points;
    }

    public double findDistance(LatLng a, LatLng b) {
        return Math.sqrt(Math.pow(a.latitude - b.latitude, 2)+Math.pow(a.longitude - b.longitude, 2));
    }

}
