package com.example.cobragas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GasMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.
        OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        com.google.android.gms.location.LocationListener {

    final int PERMISSION_REQUEST_LOCATION = 101;
    GoogleMap gMap;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    ArrayList<Gas> stations = new ArrayList<>();
    Gas currentStation = null;
    Location currentBestLocation;

    SensorManager sensorManager;
    Sensor accelerometer;
    Sensor magnetometer;
    TextView textDirection;


    LocationListener networkListener;
    LocationManager locationManager;
    LocationListener gpsListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle extras = getIntent().getExtras();

        try {
            GasDataSource ds = new GasDataSource(GasMapActivity.this);
            ds.open();
            if(extras != null){
                currentStation = ds.getSpecificStation(extras.getInt("stationsid"));
            }
            else {
                stations = ds.getStations("stationname", "ASC");
            }
            ds.close();
        }
        catch (Exception e){
            Toast.makeText(this, "Contact (s) could not be retrieved.", Toast.LENGTH_LONG).show();
        }
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        createLocationRequest();

        if(mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        /*
         * This section is for the compass sensor
         */
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


        if (accelerometer != null && magnetometer != null) {
            sensorManager.registerListener(mySensorEventListener, accelerometer,
                    SensorManager.SENSOR_DELAY_FASTEST);
            sensorManager.registerListener(mySensorEventListener, magnetometer,
                    SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            Toast.makeText(this, "Sensors not found", Toast.LENGTH_LONG).show();
        }
        textDirection = (TextView) findViewById(R.id.textHeading);


        initListButton();
        initMapButton();
        initSettingsButton();
        ImageButton ibmap = (ImageButton) findViewById(R.id.imageButtonMap);
        ibmap.setEnabled(false);
        initMapTypeButton();
        //initGetLocationButton();


    }


    private SensorEventListener mySensorEventListener = new SensorEventListener() {
        float[] accelerometerValues;
        float[] magneticValues;
        @Override
        public void onSensorChanged(SensorEvent event) {

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                accelerometerValues = event.values;

            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                magneticValues = event.values;

            if (accelerometerValues != null && magneticValues != null) {
                float R[] = new float[9];
                float I[] = new float[9];
                boolean success = SensorManager.getRotationMatrix(R, I,
                        accelerometerValues, magneticValues);

                if (success) {

                    float orientation[] = new float[3];
                    SensorManager.getOrientation(R, orientation);

                    float azimut = (float) Math.toDegrees(orientation[0]);
                    if (azimut < 0.0f) {
                        azimut += 360.0f;
                    }
                    String direction;
                    if (azimut >= 315 || azimut < 45) {
                        direction = "N";
                    } else if (azimut >= 225 && azimut < 315) {
                        direction = "W";
                    } else if (azimut >= 135 && azimut < 225) {
                        direction = "S";
                    } else {
                        direction = "E";
                    }

                    textDirection.setText(direction);
                }
            }
        };
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };



    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getBaseContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getBaseContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            //locationManager.removeUpdates(gpsListener);
            //locationManager.removeUpdates(networkListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListButton() {
        ImageButton ibList = (ImageButton) findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GasMapActivity.this, GasListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }

    private void initMapButton() {
        ImageButton ibMap = (ImageButton) findViewById(R.id.imageButtonMap);
        ibMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GasMapActivity.this, GasMapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }

    private void initSettingsButton() {
        ImageButton ibList = (ImageButton) findViewById(R.id.imageButtonSettings);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GasMapActivity.this, GasSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }

    protected void onStart(){
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @SuppressLint("RestrictedApi")
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getBaseContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getBaseContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, (LocationListener) this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getBaseContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getBaseContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, (LocationListener) this);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getBaseContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getBaseContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                (LocationListener) this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        Point size = new Point();
        WindowManager w = getWindowManager();
        w.getDefaultDisplay().getSize(size);
        int measuredWidth = size.x;
        int measuredHeight = size.y;

        if(stations.size() > 0){
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for(int i = 0; i< stations.size();i++){
                currentStation = stations.get(i);

                Geocoder geo = new Geocoder(this);
                List<Address> addresses = null;

                String address = currentStation.getStreetAddress() + ", " +
                        currentStation.getCity() + ", " +
                        currentStation.getState() + " " +
                        currentStation.getZipCode();

                try{
                    addresses = geo.getFromLocationName(address, 1);
                }catch (IOException e){
                    e.printStackTrace();
                }

                LatLng point = new LatLng(addresses.get(0).getLatitude(),
                        addresses.get(0).getLongitude());
                builder.include(point);

                gMap.addMarker(new MarkerOptions().position(point)
                        .title(currentStation.getStationName()).snippet(address));
            }
            gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),
                    measuredWidth, measuredHeight, 450));

        }
        else {
            if(currentStation != null){
                Geocoder geo = new Geocoder(this);
                List<Address> addresses = null;


                String address = currentStation.getStreetAddress() + ", " +
                        currentStation.getCity() + ", " +
                        currentStation.getState() + ", " +
                        currentStation.getZipCode();

                try{
                    addresses = geo.getFromLocationName(address,1);
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                LatLng point = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());

                gMap.addMarker(new MarkerOptions().position(point)
                        .title(currentStation.getStationName()).snippet(address));
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 16));
            }
            else {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        GasMapActivity.this).create();
                alertDialog.setTitle("No Data");
                alertDialog.setMessage("No data is available for the mapping function.");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                        "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                alertDialog.show();
            }
        }

        try {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(GasMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(GasMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        Snackbar.make(findViewById(R.id.activity_map), "MyContactList requires this permission to locate your stations", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(GasMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
                            }
                        })
                                .show();
                    } else {
                        ActivityCompat.requestPermissions(GasMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
                    }
                } else {
                    startLocationUpdates();
                }
            } else {
                startLocationUpdates();
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Error requesting permission", Toast.LENGTH_LONG).show();
        }
    }

    /*
    private void initGetLocationButton() {
        Button locationButton = (Button) findViewById(R.id.buttonGetLocation);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(GasMapActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(GasMapActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                                Snackbar.make(findViewById(R.id.activity_map), "MyContactList requires this permission to locate your stations", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ActivityCompat.requestPermissions(GasMapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
                                    }
                                })
                                        .show();
                            } else {
                                ActivityCompat.requestPermissions(GasMapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
                            }
                        } else {
                            startLocationUpdates();
                        }
                    } else {
                        startLocationUpdates();
                    }
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Error requesting permission", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
*/
    private void startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getBaseContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getBaseContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        gMap.setMyLocationEnabled(true);
    }

    private boolean isBetterLocation(Location location){
        boolean isBetter = false;
        if(currentBestLocation == null){
            isBetter = true;
        }
        else if(location.getAccuracy() <= currentBestLocation.getAccuracy()){
            isBetter = true;
        }
        else if(location.getTime() - currentBestLocation.getTime() > 5*60*1000){
            isBetter = true;
        }
        return isBetter;
    }

    /*
        @Override
        public void onRequestPermissionsResult(int requestCode,
                                              String permissions[], int[] grantResults){
            switch(requestCode){
                case PERMISSION_REQUEST_LOCATION: {
                    if(grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        startLocationUpdates();
                     } else {
                        Toast.makeText(GasMapActivity.this,
                                "MyContactList will not locate your stations.",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    */
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void initMapTypeButton() {
        final Button satelitebtn = (Button)findViewById(R.id.buttonMapType);
        satelitebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentSetting = satelitebtn.getText().toString();
                if(currentSetting.equalsIgnoreCase("Satellite View")){
                    gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    satelitebtn.setText("Normal View");
                }
                else {
                    gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    satelitebtn.setText("Satellite View");
                }
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(getBaseContext(), "Lat: " +location.getLatitude() +
                        " Long: " + location.getLongitude() +
                        "Accuracy: " +location.getAccuracy(),
                Toast.LENGTH_LONG).show();
    }
}