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
    private AppCompatActivity compatActivity;

    public Navigation(AppCompatActivity compatActivity) {
        this.compatActivity = compatActivity;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //if you delete these comments, it will crash your programm
        if (ContextCompat.checkSelfPermission(compatActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);

//            LocationManager manager = (LocationManager)
//                    compatActivity.getSystemService(Context.LOCATION_SERVICE);
//            Criteria mCriteria = new Criteria();
//            String bestProvider = String.valueOf(manager.getBestProvider(mCriteria, true));
//            Log.i("Loc", bestProvider);
//
//
//            Location mLocation = manager.getLastKnownLocation(bestProvider);
//
//            if (mLocation != null) {
//                Log.e("TAG", "GPS is on");
//                final double currentLatitude = mLocation.getLatitude();
//                final double currentLongitude = mLocation.getLongitude();
//                LatLng loc1 = new LatLng(currentLatitude, currentLongitude);
//                googleMap.addMarker(new MarkerOptions().position(loc1).title("Your Current Location"));
//                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude), 15));
//                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
//            }
        }
//        Log.i("map", "doesn't work");
//        LatLng sydney = new LatLng(-34, 151);
//        googleMap.addMarker(new MarkerOptions()
//                .position(sydney)
//                .title("Here I am"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));


        // Add a marker in Sydney, Australia, and move the camera.
    }


}