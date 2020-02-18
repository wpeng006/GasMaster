package com.laioffer.GasMaster;

import org.json.JSONException;
import org.json.JSONObject;

public class GasStation {
    public String name;
    public double lat;
    public double lng;
    public String address;
    public double rating;
    public boolean isOpen;


    public GasStation(final String name, final double lat, final double lng, final String address, final double rating, final boolean isOpen) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.rating = rating;
        this.isOpen = isOpen;

    }

    public GasStation(final GasStation.GasStationBuilder gasStationBuilder) {
        this.name = gasStationBuilder.name;
        this.lat = gasStationBuilder.lat;
        this.lng = gasStationBuilder.lng;
        this.address = gasStationBuilder.address;
        this.rating = gasStationBuilder.rating;
        this.isOpen = gasStationBuilder.isOpen;
    }


    public JSONObject toJSONObject() {
        final JSONObject obj = new JSONObject();
        try {
            obj.put("name", (Object)this.name);
            obj.put("rating", this.rating);
            obj.put("lat", this.lat);
            obj.put("lng", this.lng);
            obj.put("address", (Object)this.address);
            obj.put("isOpen", this.isOpen);

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
        public boolean isOpen;

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

        public void setIsOpen(final boolean isOpen) {
            this.isOpen = isOpen;
        }

        public GasStation build() {
            return new GasStation(this);
        }
    }
}