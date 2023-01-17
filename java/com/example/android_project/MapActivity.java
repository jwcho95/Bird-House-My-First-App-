package com.example.android_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng CompanyLocation = new LatLng(37.58566127967645, 127.2144412486413);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("버드하우스");
        markerOptions.snippet("반려조 전용 용품점");
        markerOptions.position(CompanyLocation);
        mMap.addMarker(markerOptions);

//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CompanyLocation, 16));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(CompanyLocation, 16));
    }
}