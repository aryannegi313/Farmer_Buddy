package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocateActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "LocateActivity";
    public static final String FINE_LOCATION=Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COURSE_LOCATION=Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final int LOCATION_PERMISSION_REQUEST_CODE =1234;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    public static final float DEFAULT_ZOOM=15f;

    private Boolean mLocationPermissionGranted=false;
    private GoogleMap mMap;

    //widgets
    private EditText mSearchText;
    private ImageView mgps;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap=googleMap;

        if(mLocationPermissionGranted){
            getDeviceLocation();

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mSearchText=(EditText)findViewById(R.id.input_search);
        mgps=(ImageView)findViewById(R.id.ic_gps);
        getLocationPermission();
        init();
    }

    private void init(){
        Log.d(TAG, "init: intializing ");
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH
                        || actionId==EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction()==KeyEvent.KEYCODE_ENTER)
                {
                    geoLocate();
                }
                return false;
            }
        });

        mgps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked gps icon");
                getDeviceLocation();
            }
        });
        HideSoftKeyboard();
    }

    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");

        String searchString=mSearchText.getText().toString();

        Geocoder geocoder=new Geocoder(LocateActivity.this);
        List<Address> list=new ArrayList<>();
        
        try {
            list=geocoder.getFromLocationName(searchString,1);
        }catch(IOException e)
        {
            Log.d(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if(list.size()>0){
            Address address=list.get(0);

            Log.d(TAG, "geoLocate: found a location:" + address.toString());
            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM,address.getAddressLine(0));
        }
    }



    public LatLng getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting device location");
        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        final LatLng[] latLng = {null};

        try {
                if(mLocationPermissionGranted) {
                    Task location = mFusedLocationProviderClient.getLastLocation();
                    location.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()) {
                                Log.d(TAG, "onComplete: found location!");
                                Location currentLocation=(Location)task.getResult();
                                latLng[0] =new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                                moveCamera(latLng[0],DEFAULT_ZOOM,"My Location");
                            }else
                            {
                                Log.d(TAG, "onComplete: current location is null");
                                Toast.makeText(LocateActivity.this,"UNABLE TO FIND CURRENT LOCATION",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }catch(SecurityException e)
            {
                Log.e(TAG, "getDeviceLocation: Security Exception: "+e.getMessage() );
            }

        return latLng[0];
        }

        private void moveCamera(LatLng latlng,float zoom,String title)
        {
            Log.d(TAG, "moveCamera: moving camera to lat: " + latlng.latitude + " ,lng: " + latlng.longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom));
            if(!title.equals("My Location")){
                MarkerOptions options=new MarkerOptions()
                        .position(latlng)
                        .title(title);
                mMap.addMarker(options);
            }
        HideSoftKeyboard();
        }

    private void initMap(){
        Log.d(TAG, "initMap: initializing the map");
        SupportMapFragment mapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(LocateActivity.this);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: Getting location permissions");
        String[] permissions={Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
                initMap();
            }
            else{
                ActivityCompat.requestPermissions(this,
                        permissions,LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else{
            ActivityCompat.requestPermissions(this,
                    permissions,LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted=false;
        Log.d(TAG, "onRequestPermissionsResult: called");

        switch (requestCode)
        {
            case LOCATION_PERMISSION_REQUEST_CODE:
            {
                if(grantResults.length>0 )
                {
                    for (int i=0; i<grantResults.length;i++) {
                        if(grantResults[i]!=PackageManager.PERMISSION_GRANTED)
                        {
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: Permissio Failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionGranted=true;
                    initMap();
                }
            }
        }
    }

    private void HideSoftKeyboard()
    {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


}
