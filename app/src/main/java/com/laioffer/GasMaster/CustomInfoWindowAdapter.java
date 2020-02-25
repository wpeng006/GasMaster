package com.laioffer.GasMaster;

import android.content.Context;
import android.graphics.Typeface;
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
        TextView opening = (TextView) v.findViewById(R.id.opening);
        brand.setText(marker.getTitle());
        brand.setTypeface(null, Typeface.BOLD);
        rating.setText(marker.getSnippet());
        GasStation gasStation = (GasStation) marker.getTag();
        if (gasStation.isOpen) {
            opening.setText("Opening");
        } else {
            opening.setText("Closed");
        }

        return v;

    }

}