package com.laioffer.GasMaster;

import org.json.JSONException;
import org.json.JSONObject;

public class GasStation {
    public String name;
    public double lat;
    public double lng;
    public String address;
    public double rating;


    public GasStation(String name, double lat, double lng, String address, double rating) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.rating = rating;
    }

    public GasStation(GasStationBuilder gasStationBuilder) {
        this.name = gasStationBuilder.name;
        this.lat = gasStationBuilder.lat;
        this.lng = gasStationBuilder.lng;
        this.address = gasStationBuilder.address;
        this.rating = gasStationBuilder.rating;
    }


    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", name);
            obj.put("lat", lat);
            obj.put("lng", lng);
            obj.put("address", address);
            obj.put("rating", rating);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    //we want to new item but dont want to write so many constructors, so we use setters
    //at same time we dont want to change variables in item so we dont put setters in item
    //that;s why we use itemBuilder
    public static class GasStationBuilder {//must put static, otherwise need to new Item before using ItemBuilder
        public String name;
        public double lat;
        public double lng;
        public String address;
        public double rating;

        public GasStationBuilder() {}

        public void setName(String name) {
            this.name = name;
        }
        public void setLat(double lat) {
            this.lat = lat;
        }
        public void setLng(double lng) {
            this.lng = lng;
        }
        public void setAddress(String address) {
            this.address = address;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public GasStation build() {
            return new GasStation(this);
        }
    }

    //why not put ItemBuilder as a separate class??
    //Item item = new Item(builder); do not like to use. when Item constructor is private, this can be avoided
    //Item item = builder.build();
}