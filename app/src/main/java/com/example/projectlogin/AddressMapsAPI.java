package com.example.projectlogin;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class AddressMapsAPI extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    GoogleMap map;
    SupportMapFragment mapFragment;
    SearchView searchView;
    MarkerOptions pinloca;
    LatLng start = null, end = null;
    String stringsearch = "";
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_maps_a_p_i);

        searchView = findViewById(R.id.sv_location);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);
        if (getIntent().hasExtra("address")){
            stringsearch = getIntent().getStringExtra("address");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;


      /*  locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);*/


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

        final Address address = addressList.get(0);
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
                    stringsearch = address.getAddressLine(0);
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
                if (addressList.size() != 0){
                    stringsearch = addressList.get(0).getAddressLine(0);
                }
                else stringsearch = Double.toString(latLng.latitude) +  Double.toString(latLng.longitude);
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
            /*try {
                addressList = geocoder1.getFromLocationName(stringsearch,1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);*/

            Intent intent = new Intent(getApplicationContext(),PaymentActivity.class);
            intent.putExtra("Address", stringsearch /*address.getAddressLine(0)*/);
            intent.putExtra("Distance",res);
            startActivity(intent);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        map.addMarker(new MarkerOptions().position(latLng).title("You are here"));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}