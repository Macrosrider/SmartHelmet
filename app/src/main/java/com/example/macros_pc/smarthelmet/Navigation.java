package com.example.macros_pc.smarthelmet;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class Navigation implements OnMapReadyCallback {
    public Context context;
    private AppCompatActivity compatActivity;

    public Navigation(AppCompatActivity compatActivity) {
        this.compatActivity = compatActivity;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ContextCompat.checkSelfPermission(compatActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager)
                    compatActivity.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            Location location = locationManager.getLastKnownLocation(locationManager
                    .PASSIVE_PROVIDER); //this row makes my code working and I don't know why
            if(location != null) {
                Log.e("TAG", "GPS is on");
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng current = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions()
                        .position(current)
                        .title("Current location"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15));
            }else{
                Log.e("TAG", "GPS doesn't work");
            }

                // Add a marker in Sydney, Australia, and move the camera.
        }


    }



}