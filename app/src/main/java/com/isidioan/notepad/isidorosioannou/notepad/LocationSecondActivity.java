package com.isidioan.notepad.isidorosioannou.notepad;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

public class LocationSecondActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener{

    private EditText editTitleLocation, addressText, latText,longText;
    private Button saveLocationButton, deleteLocationButton, navigateButton,findLocationButton;
    private static final int UPDATE_TIME = 15000;
    private static final int FASTER_UPDATE_TIME = 10000;
    private static final float METERS = 5;
    private GoogleApiClient myGoogleApi;
    private Location location;
    private DatabaseAdapt dbAdapter;
    private String address,titleLocation;
    private boolean myLocationUpdates;
    private LocationRequest myLocationRequest;
    private static final int CHECK_CODE=1;
    private double longitude,latitude;
    private static final String EXTERNAL_PERMISSIONS[] = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET};
    private static final int EXTERNAL_CODE=1;
    private AddressResultReceiver resultReceiver;
    private boolean view;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_second);
        myLocationUpdates=false;
        initializeViews();
        buildGoogleApi();
        resultReceiver = new AddressResultReceiver(null);
        dbAdapter = new DatabaseAdapt(this);
        dbAdapter.open();
        Intent intent = getIntent();
        view = intent.getExtras().getBoolean(LocationMainActivity.VIEW_LOC);
       if(view) {
           titleLocation = intent.getExtras().getString(LocationMainActivity.LOCATION_TITLE, "");
           address = intent.getExtras().getString(LocationMainActivity.LOCATION_ADDRESS, "");
           longitude = intent.getExtras().getDouble(LocationMainActivity.LOCATION_lONG, 0);
           latitude = intent.getExtras().getDouble(LocationMainActivity.LOCATION_LAT, 0);
           id = intent.getExtras().getInt(LocationMainActivity.LOCATION_ID, 0);
           editTitleLocation.setText(titleLocation);
           addressText.setText(address);
           latText.setText(String.valueOf(latitude));
           longText.setText(String.valueOf(longitude));
       }
    }

    private void initializeViews(){
        editTitleLocation = (EditText)findViewById(R.id.editTitleLocation);
        addressText = (EditText)findViewById(R.id.addressText);
        latText = (EditText)findViewById(R.id.latitudeText);
        longText=(EditText)findViewById(R.id.longitudeText);
        saveLocationButton = (Button)findViewById(R.id.saveLocation);
        deleteLocationButton = (Button)findViewById(R.id.deleteLocation);
        findLocationButton = (Button)findViewById(R.id.findLocation);
        navigateButton= (Button)findViewById(R.id.navigateLocation);
        saveLocationButton.setOnClickListener(this);
        deleteLocationButton.setOnClickListener(this);
        findLocationButton.setOnClickListener(this);
        navigateButton.setOnClickListener(this);

    }

    public void onStart(){
        super.onStart();
        myGoogleApi.connect();
    }

    public void onResume(){
        super.onResume();
        if (myGoogleApi.isConnected()){
            startLocationUpdates();
        }
        else {
            myGoogleApi.connect();

        }
    }

    public void onPause(){
        super.onPause();
       if(myGoogleApi.isConnected())
        stopLocationUpdates();
    }

    public void onStop(){
        super.onStop();
        myGoogleApi.disconnect();
    }
    public void onDestroy(){
        super.onDestroy();
        dbAdapter.close();
    }

    private synchronized void buildGoogleApi(){
        myGoogleApi = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private void createRequest (){

        myLocationRequest = new LocationRequest();
        myLocationRequest.setInterval(UPDATE_TIME)
                        .setFastestInterval(FASTER_UPDATE_TIME)
                        .setSmallestDisplacement(METERS)
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(myLocationRequest);
            builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(myGoogleApi,builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()){
                    case LocationSettingsStatusCodes.SUCCESS:
                        startLocationUpdates();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try{
                            status.startResolutionForResult(LocationSecondActivity.this,CHECK_CODE);
                        }catch (IntentSender.SendIntentException e){
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Toast.makeText(LocationSecondActivity.this,"Settings Change Unavailable",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void retrieveLocation(){
        try {
            location = LocationServices.FusedLocationApi.getLastLocation(myGoogleApi);
        }catch (SecurityException ex){
            ex.printStackTrace();
        }

    }
    private void startLocationUpdates(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(myGoogleApi, myLocationRequest, this);
            myLocationUpdates=true;
        }else {
            ActivityCompat.requestPermissions(this,EXTERNAL_PERMISSIONS,EXTERNAL_CODE);
        }
    }
    private void stopLocationUpdates(){
        LocationServices.FusedLocationApi.removeLocationUpdates(myGoogleApi,LocationSecondActivity.this);
        myLocationUpdates=false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       switch (requestCode){
           case CHECK_CODE:
               switch (resultCode){
                   case Activity.RESULT_OK:
                   startLocationUpdates();
                       break;
                   case Activity.RESULT_CANCELED:
                       break;
               }
               break;
       }
    }

    @Override
    public void onConnected(Bundle bundle) {
        createRequest();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"Google Api coonection suspended",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,"Google Api can not connect",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location=location;
        longitude=location.getLongitude();
        latitude=location.getLatitude();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        switch (viewId){
            case R.id.saveLocation:
               titleLocation=editTitleLocation.getText().toString();
              if(!view) {
                  try {
                      dbAdapter.createLocation(titleLocation, address, latitude, longitude);
                      Toast.makeText(this, "Location Saved", Toast.LENGTH_SHORT).show();
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              }else{
                  try{
                      dbAdapter.updateLocation(id,titleLocation,address,latitude,longitude);
                      Toast.makeText(this, "Location Edited", Toast.LENGTH_SHORT).show();
                  }catch (Exception e){
                      e.printStackTrace();
                  }
              }
               finish();
                break;
            case R.id.deleteLocation:
                dbAdapter.deleteLocation(id);
                finish();
                break;
            case R.id.navigateLocation:
                String uri = "https://www.google.com/maps/dir/?api=1&destination=";
                Uri dir = Uri.parse(uri + Uri.encode(address));
                Intent intent = new Intent(Intent.ACTION_VIEW,dir );
                startActivity(intent);
                break;
            case R.id.findLocation:
                    retrieveLocation();
                if(location!=null){
                    latitude = location.getLatitude();
                    longitude=location.getLongitude();
                    startIntentSetvice();
                    }

                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case EXTERNAL_CODE:
                if(ActivityCompat.checkSelfPermission(this,permissions[0])==PackageManager.PERMISSION_GRANTED)
                {
                    startLocationUpdates();
                }
                else {
                    Toast.makeText(this,"Permissions denied",Toast.LENGTH_LONG).show();

                }
                break;
        }
    }
    private class AddressResultReceiver extends ResultReceiver{
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }
          public void  onReceiveResult(int resultCode,Bundle resultData){
              if(resultCode==GeocoderAddress.SUCCESS){
                   address = resultData.getString(GeocoderAddress.RESULT_DATA);
                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          latText.setText(String.valueOf(latitude));
                          longText.setText(String.valueOf(longitude));
                          addressText.setText(address);
                      }
                  });


              }

            }
    }
    private void startIntentSetvice(){

        Intent intent = new Intent(this,GeocoderAddress.class);
        intent.putExtra(GeocoderAddress.RECEIVER,resultReceiver);
        intent.putExtra(GeocoderAddress.LOCATION_DATA,location);
        startService(intent);
    }
}
