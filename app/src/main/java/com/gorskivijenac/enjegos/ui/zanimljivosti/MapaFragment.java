package com.gorskivijenac.enjegos.ui.zanimljivosti;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gorskivijenac.enjegos.R;


public class MapaFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mapa, container, false);

        mapView = rootView.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Customize the map as needed...
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Add a marker for the coordinates 42.397287, 18.843244
        LatLng targetLocation = new LatLng(42.397287, 18.843244);
        googleMap.addMarker(new MarkerOptions().position(targetLocation).title("Target Location"));

        // Get the current location and add a marker for it
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));

                    // Move the camera to show both markers on the map
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(currentLocation);
                    builder.include(targetLocation);
                    LatLngBounds bounds = builder.build();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 200);
                    googleMap.animateCamera(cameraUpdate);
                }
            });
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onMapReady(googleMap);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}
