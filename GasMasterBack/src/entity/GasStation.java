package entity;

import org.json.JSONException;
import org.json.JSONObject;

public class GasStation
{
    public String name;
    public double lat;
    public double lng;
    public String address;
    public double rating;
    
    public GasStation(final String name, final double lat, final double lng, final String address, final double rating) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.rating = rating;
    }
    
    public GasStation(final GasStation.GasStationBuilder gasStationBuilder) {
        this.name = gasStationBuilder.name;
        this.lat = gasStationBuilder.lat;
        this.lng = gasStationBuilder.lng;
        this.address = gasStationBuilder.address;
        this.rating = gasStationBuilder.rating;
    }
    
    public JSONObject toJSONObject() {
        final JSONObject obj = new JSONObject();
        try {
            obj.put("name", (Object)this.name);
            obj.put("rating", this.rating);
            obj.put("lat", this.lat);
            obj.put("lng", this.lng);
            obj.put("address", (Object)this.address);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
    
    
    public static class GasStationBuilder
    {
        public String name;
        public double lat;
        public double lng;
        public String address;
        public double rating;
        
        public void setName(final String name) {
            this.name = name;
        }
        
        public void setLat(final double lat) {
            this.lat = lat;
        }
        
        public void setLng(final double lng) {
            this.lng = lng;
        }
        
        public void setAddress(final String address) {
            this.address = address;
        }
        
        public void setRating(final double rating) {
            this.rating = rating;
        }
        
        public GasStation build() {
            return new GasStation(this);
        }
    }
}