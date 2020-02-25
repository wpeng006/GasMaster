package com.laioffer.GasMaster;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import static com.laioffer.GasMaster.Utility.SERVICE_URL;
import static com.laioffer.GasMaster.Utility.DEFAULT_KEYWORD;
import static com.laioffer.GasMaster.Utility.DEFAULT_LAT;
import static com.laioffer.GasMaster.Utility.DEFAULT_LNG;
import static com.laioffer.GasMaster.Utility.DEFAULT_RADIUS;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class MapsMarkerActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.

        LatLng curr = new LatLng(DEFAULT_LAT, DEFAULT_LNG);
        try {
            getGasStation(googleMap, DEFAULT_LAT, DEFAULT_LNG, DEFAULT_RADIUS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curr, 10));
    }

    void getGasStation(final GoogleMap googleMap, double lat, double lng, double radius) throws IOException {
        //get a list of gas station with given geolocation and raidus and show them on mapview


        //1. build connection with gasmaster service
        HttpUrl.Builder urlBuilder = HttpUrl.parse(SERVICE_URL + "/search")
                .newBuilder();
        urlBuilder.addQueryParameter("query", DEFAULT_KEYWORD);
        urlBuilder.addQueryParameter("lat", String.valueOf(DEFAULT_LAT));
        urlBuilder.addQueryParameter("lng", String.valueOf(DEFAULT_LNG));
        urlBuilder.addQueryParameter("radius", String.valueOf(DEFAULT_RADIUS));
        String url = urlBuilder.build().toString();
        Log.i("url", url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        //2. call gasmaster service
        client.newCall(request).enqueue(new Callback() {
            final List<GasStation> list = new LinkedList<>();
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("onFailure", "fail");
                call.cancel();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    //3. get gas station list in the format of JSON Array
                    final JSONArray myResponse = new JSONArray(response.body().string());
                    MapsMarkerActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //4. iterate JSON Array and get Information
                            for (int i = 0; i < myResponse.length(); i++) {
                                try {
                                    JSONObject entry = myResponse.getJSONObject(i);
                                    //LatLng latLng = new LatLng(entry.getDouble("lat"), entry.getDouble("lng"));
                                    GasStation.GasStationBuilder builder = new GasStation.GasStationBuilder();
                                    builder.setAddress(entry.getString("address"));
                                    builder.setLat(entry.getDouble("lat"));
                                    builder.setLng(entry.getDouble("lng"));
                                    builder.setName(entry.getString("name"));
                                    builder.setRating(entry.getDouble("rating"));
                                    GasStation gasStation = builder.build();
                                    list.add(gasStation);
                                    setMarker(gasStation, googleMap);
                                    Log.i(Integer.toString(i), gasStation.name);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    public void setMarker(GasStation gasStation, GoogleMap googleMap){
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(gasStation.lat, gasStation.lng))
                .title(gasStation.name)
                .snippet(String.valueOf(gasStation.rating))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.gas_station)));
    }
}