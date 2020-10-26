package com.example.projectlogin;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class AddressMapsAPI extends FragmentActivity implements OnMapReadyCallback{
    GoogleMap map;
    SupportMapFragment mapFragment;
    SearchView searchView;
    MarkerOptions pinloca;
    LatLng start = null, end= null;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_maps_a_p_i);

        searchView = findViewById(R.id.sv_location);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.797884,106.6404873), 12));
        String a = "hcmus";

        //todo: get location from firebase to string

        List<Address> addressList = null;
        Geocoder geocoder = new Geocoder(AddressMapsAPI.this);
        try {
            addressList = geocoder.getFromLocationName(a, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Address address = addressList.get(0);
        start = new LatLng(address.getLatitude(),address.getLongitude());


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(AddressMapsAPI.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addressList.size() == 0) {
                        Toast.makeText(AddressMapsAPI.this, "Invalid address", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    Address address = addressList.get(0);
                    map.clear();
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    end = latLng;
                    pinloca = new MarkerOptions().position(latLng).title(location);
                    map.addMarker(pinloca);
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                map.clear();
                pinloca = new MarkerOptions().position(latLng);
                Geocoder geocoder1 = new Geocoder(getApplicationContext());
                List<Address> addressList = null;
                try {
                    addressList = geocoder1.getFromLocation(latLng.latitude, latLng.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                map.addMarker(pinloca);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));


                end = latLng;
                //end = new LatLng(address.getLatitude(), address.getLongitude());
            }
        });


    }

    public void add(View view) {
        if (start != null && end != null){
            float[] results = new float[1];
            Location.distanceBetween(start.latitude, start.longitude,end.latitude, end.longitude, results);
            float res = results[0];
            Geocoder geocoder1 = new Geocoder(getApplicationContext());
            List<Address> addressList = null;
            try {
                addressList = geocoder1.getFromLocation(end.latitude, end.longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            Intent intent = new Intent(getApplicationContext(),PaymentActivity.class);
            intent.putExtra("Address", address.getAddressLine(0));
            intent.putExtra("Distance",res);
            startActivity(intent);
        }

    }
}