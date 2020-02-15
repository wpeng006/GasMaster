package com.laioffer.GasMaster;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.laioffer.GasMaster.Utility.DEFAULT_KEYWORD;
import static com.laioffer.GasMaster.Utility.DEFAULT_RADIUS;
import static com.laioffer.GasMaster.Utility.LONG_DIS;
import static com.laioffer.GasMaster.Utility.SERVICE_URL;

public class RouteFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
  private MapView mapView;
  private View view;

  private static final int COLOR_BLACK_ARGB = 0xff000000;
  private static final int POLYLINE_STROKE_WIDTH_PX = 20;
  private List<LatLng> navRoute;

  private String source = "USC";
  private String dest = "UCLA";
  String strUrl = UrlPart.getUrl(source, dest);

  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";
  private String mParam1;
  private String mParam2;

  private GoogleMap mMap;
  private boolean longRoute = false;

  // geo-location of sampling points of a route
  private List<LatLng> points = new ArrayList<>();
  private List<LatLng> source_dest = new ArrayList<>();
  private LatLng sourcePoint;
  private LatLng destPoint;

  OkHttpClient client = new OkHttpClient();
  Route route = new Route();


  public RouteFragment() {
    // Required empty public constructor
  }

  public static RouteFragment newInstance(String param1, String param2) {
    RouteFragment fragment = new RouteFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFrag.getMapAsync(this);

    view = inflater.inflate(R.layout.fragment_route, container,
      false);
    return view;

    //return inflater.inflate(R.layout.fragment_route, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mapView = (MapView) this.view.findViewById(R.id.event_map_view);
    if (mapView != null) {
      mapView.onCreate(null);
      mapView.onResume();// needed to get the map to display immediately
      mapView.getMapAsync(this);
    }
  }

  @Override
  public boolean onMarkerClick(final Marker marker) {
    // Check which Marker has been clicked and return positions.
    LatLng mPos = marker.getPosition();
    Log.e("Marker Click", String.valueOf(mPos.latitude));

    List<LatLng> newRoute = new ArrayList<>();
    newRoute.add(sourcePoint);
    newRoute.add(mPos);
    newRoute.add(destPoint);


    if (longRoute) {
      mMap.clear();
      try {
        getGasStation(mMap, mPos, 20000);
        setRoute(newRoute);
        autoMoveCamera(newRoute);
      } catch (IOException e) {
        e.printStackTrace();
      }
      longRoute = false;
    } else {
      // Show new route.
      mMap.clear();
      mMap.addMarker(new MarkerOptions().position(mPos).icon(BitmapDescriptorFactory.fromResource((R.drawable.station))));
      setRoute(newRoute);
      autoMoveCamera(newRoute);
    }

    return true;
  }

  /**
   * Manipulates the map once available.
   * This callback is triggered when the map is ready to be used.
   * This is where we can add markers or lines, add listeners or move the camera. In this case,
   * we just add a marker near Sydney, Australia.
   * If Google Play services is not installed on the device, the user will be prompted to install
   * it inside the SupportMapFragment. This method will only be triggered once the user has
   * installed Google Play services and returned to the app.
   */
  @Override
  public void onMapReady(GoogleMap googleMap) {

    mMap = googleMap;
    Log.e("Task", "mMap created.");

    // Download route data, sample points, and then
    // show gas stations for short distance or show area spots for long distance.
    DownloadTask downloadTask = new DownloadTask();

    Log.e("Background Task", "Start to download route.");
    downloadTask.execute(strUrl);

    // Click a marker and then show nearby stations or draw new route.
    mMap.setOnMarkerClickListener(this);
    Log.e("Task", "Listen on marker.");


    // Jump to Google Map to navigate.
    FloatingActionButton fab = view.findViewById(R.id.fab);

    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Uri uri = Uri.parse(Utils.getMapsUrl(navRoute));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);

        startActivity(mapIntent);
      }
    });

  }

  private class DownloadTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... url) {

      String data = "";

      try {
        data = UrlPart.downloadUrl(url[0]);
      } catch (Exception e) {
        Log.d("Background Task", e.toString());
      }
      return data;
    }

    @Override
    protected void onPostExecute(String result) {

      // Step 1: Sample points from original route.
      points = route.getRoute(result);

      // Step 3:
      // short distance
      if (route.findDistance(points.get(0), points.get(points.size() - 1)) < LONG_DIS) {
        longRoute = false;
        showGasStation(points, mMap);
      } else { // long distance, need to be modified
        longRoute = true;
        for (int i = 0; i < points.size(); i++) {
          mMap.addMarker(new MarkerOptions().position(points.get(i)));
        }
      }

      sourcePoint = points.get(0);
      destPoint = points.get(points.size() - 1);
      source_dest.add(sourcePoint);
      source_dest.add(destPoint);
      setRoute(source_dest);
      autoMoveCamera(source_dest);

    }
  }

  /********************* Get Gas Station **************************/
  // Show all Gas Stations along the route for short distance.
  public void showGasStation(List<LatLng> points, GoogleMap mMap) {
    mMap.clear();
    for (int i = 0; i < points.size(); i++) {
      try {
        getGasStation(mMap, points.get(i), DEFAULT_RADIUS);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(points.get(0), 10));
  }


  // Get Gas Station
  void getGasStation(final GoogleMap googleMap, LatLng point, double radius) throws IOException {
    //get a list of gas station with given geolocation and raidus and show them on mapview


    //1. build connection with gasmaster service
    HttpUrl.Builder urlBuilder = HttpUrl.parse(SERVICE_URL + "/search")
      .newBuilder();
    urlBuilder.addQueryParameter("query", DEFAULT_KEYWORD);
    urlBuilder.addQueryParameter("lat", String.valueOf(point.latitude));
    urlBuilder.addQueryParameter("lng", String.valueOf(point.longitude));
    urlBuilder.addQueryParameter("radius", String.valueOf(radius));
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
          getActivity().runOnUiThread(new Runnable() {
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
                  setGasMarker(gasStation, googleMap);
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

  public void setGasMarker(GasStation gasStation, GoogleMap googleMap){
    googleMap.addMarker(new MarkerOptions().position(new LatLng(gasStation.lat, gasStation.lng))
      .title(gasStation.name).snippet(String.valueOf(gasStation.rating)));
  }

  /********************* Draw Route **************************/
  public void drawRoute(List<LatLng> input) {

    PolylineOptions options = new PolylineOptions().clickable(false);
    options.addAll(input);
    Polyline polyline = mMap.addPolyline(options);
    polyline.setColor(COLOR_BLACK_ARGB);
    polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
    Log.d("Success", "drawRoute executed");
  }

  public void setRoute(List<LatLng> points) {
    String url = Utils.getDirectionUrl(points);
    navRoute = points;
    DownloadTask2 task = new DownloadTask2();
    task.execute(url);
  }

  public List<LatLng> startRequest(String url) {

    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
      .url(url)
      .build();

    List<LatLng> polyLine = new ArrayList<>();
    try {
      Response response = client.newCall(request).execute();
      polyLine = parseResponse(response);
      return polyLine;
    } catch (IOException e) {
      Log.e("Fail", "failed to parse response");
      e.printStackTrace();
    }
    return polyLine;
  }

  public void autoMoveCamera(List<LatLng> list) {
    LatLngBounds.Builder boundBuilder = new LatLngBounds.Builder();
//
    for (int i = 0; i < list.size(); i++) {
      boundBuilder.include(list.get(i));
    }
    Log.d("Success", "latLngs size is " + Integer.toString(list.size()));

    LatLngBounds bounds = boundBuilder.build();
    int width = getResources().getDisplayMetrics().widthPixels;
    int height = getResources().getDisplayMetrics().heightPixels;
    int padding = 100;
    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
  }


  List<LatLng> parseResponse(Response response) {

    List<LatLng> points = new ArrayList<>();

    Log.d("Download", "Received result, processing response");
    List<String> encodedRoutes = new ArrayList<>();
    try {

      JSONObject json = new JSONObject(response.body().string());
      JSONArray routeArray = json.getJSONArray("routes");

      if (routeArray == null || routeArray.length() == 0) {
        Log.d("Error", "parsing routes failed");
        throw new JSONException("Route Array is null or Empty");
      }

      for (int i = 0; i < routeArray.length(); i++) {
        JSONObject encodedPolyLine = routeArray.getJSONObject(i)
          .getJSONObject("overview_polyline");
        encodedRoutes.add(encodedPolyLine.getString("points"));
      }
      Log.d("Success", "Parsing successful");
      Log.d("Success", "PolyLine array size" + ":" + " "+ encodedRoutes.size());

      for (int i = 0; i < encodedRoutes.size(); i++) {
        points.addAll(PolyUtil.decode(encodedRoutes.get(i)));
      }
      Log.d("Success", "points have size" + Integer.toString(points.size()));

    } catch (JSONException | IOException e) {
      e.printStackTrace();
      Log.d("Error", e.getMessage());
    }
    return points;
  }

  private class DownloadTask2 extends AsyncTask<String, String, List<LatLng>> {

    @Override
    protected List<LatLng> doInBackground(String... strings) {
      List<LatLng> points = new ArrayList<>();
      try {
        Log.d("Success", "Initiated request");
        points = startRequest(strings[0]);
        Log.d("Success", "Initiated request and received response");
        Log.d("Success", "points has length " + points.size());
        return points;
      } catch (Exception e) {
        Log.e("Fail", "Request to Download Failed");
      }
      return points;
    }

    @Override
    protected void onPostExecute(List<LatLng> latLngs) {
      // super.onPostExecute(latLngs);
      drawRoute(latLngs);
      Log.d("Success", "route drawn on map");
    }
  }

}