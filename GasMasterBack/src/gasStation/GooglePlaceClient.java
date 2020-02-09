package gasStation;

import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import entity.GasStation;
import entity.Contants;
import java.util.List;

public class GooglePlaceClient
{
    public List<GasStation> search(final double lat, final double lon, final double radius, String keyword) {
        try {
            keyword = URLEncoder.encode(keyword, "utf-8");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String query = String.format("query=%s&location=%s,%s&radius=%s&key=%s&", keyword, lat, lon, radius, Contants.API_KEY);
        final String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?" + query;
        final StringBuilder responseBody = new StringBuilder();
        try {
            final HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            final int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                return new ArrayList<GasStation>();
            }
            final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                responseBody.append(line);
            }
            reader.close();
        }
        catch (MalformedURLException e2) {
            e2.printStackTrace();
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
        try {
            final JSONObject obj = new JSONObject(responseBody.toString());
            if (!obj.isNull("results")) {
                final List<GasStation> list = this.getItemList(obj.getJSONArray("results"));
                return this.correctRange(list, lat, lon, radius);
            }
        }
        catch (JSONException e4) {
            e4.printStackTrace();
        }
        return new ArrayList<GasStation>();
    }
    
    public List<GasStation> getItemList(final JSONArray events) throws JSONException {
        final List<GasStation> itemList = new ArrayList<GasStation>();
        for (int i = 0; i < events.length(); ++i) {
            final JSONObject gasStation = events.getJSONObject(i);
            final GasStation.GasStationBuilder builder = new GasStation.GasStationBuilder();
            if (!gasStation.isNull("name")) {
                builder.setName(gasStation.getString("name"));
            }
            if (!gasStation.isNull("rating")) {
                builder.setRating(gasStation.getDouble("rating"));
            }
            if (!gasStation.isNull("formatted_address")) {
                builder.setAddress(gasStation.getString("formatted_address"));
            }
            if (!gasStation.isNull("geometry")) {
                final JSONObject geometry = gasStation.getJSONObject("geometry");
                if (!geometry.isNull("location")) {
                    final JSONObject location = geometry.getJSONObject("location");
                    if (!location.isNull("lat")) {
                        builder.setLat(location.getDouble("lat"));
                    }
                    if (!location.isNull("lng")) {
                        builder.setLng(location.getDouble("lng"));
                    }
                }
            }
            final GasStation station = builder.build();
            itemList.add(station);
        }
        return itemList;
    }
    
    public List<GasStation> correctRange(final List<GasStation> list, final double lat, final double lon, final double radius) {
        final List<GasStation> newList = new ArrayList<GasStation>();
        for (final GasStation gasStation : list) {
            final double dist = distance(lat, lon, gasStation.lat, gasStation.lng, "K");
            final double newRadius = radius / 1000.0;
            if (dist <= newRadius) {
                newList.add(gasStation);
            }
        }
        return newList;
    }
    
    private static double distance(final double lat1, final double lon1, final double lat2, final double lon2, final String unit) {
        if (lat1 == lat2 && lon1 == lon2) {
            return 0.0;
        }
        final double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60.0 * 1.1515;
        if (unit.equals("K")) {
            dist *= 1.609344;
        }
        else if (unit.equals("N")) {
            dist *= 0.8684;
        }
        return dist;
    }
    
    public static void main(final String[] args) {
        final GooglePlaceClient g = new GooglePlaceClient();
        final List<GasStation> list = g.search(37.773972, -122.431297, 1000.0, "gas station");
        System.out.println(list.get(0).lat);
        System.out.println(list.get(0).lng);
        for (int i = 0; i < list.size(); ++i) {
            System.out.println(i);
            System.out.println(distance(37.773972, -122.431297, list.get(i).lat, list.get(i).lng, "K"));
        }
    }
}
