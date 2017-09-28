package com.example.macros_pc.smarthelmet;


import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class Navigation implements OnMapReadyCallback {
    public Context context;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //if you delete these comments, it will crash your programm
        /*if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);*/
        Log.i("map", "doesn't work");
        LatLng sydney = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Here I am"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));


        // Add a marker in Sydney, Australia, and move the camera.
    }


}