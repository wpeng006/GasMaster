package com.laioffer.GasMaster;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/** Demonstrates customizing the info window and/or its contents. */
class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {


    private Context context;

    CustomInfoWindowAdapter(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v =  inflater.inflate(R.layout.custom_info_contents, null);
        TextView brand = (TextView) v.findViewById(R.id.brand);
        TextView rating = (TextView) v.findViewById(R.id.rating);
        Log.i("brand", marker.getTitle());
        brand.setText(marker.getTitle());
        rating.setText(marker.getSnippet());
        return v;

    }

}