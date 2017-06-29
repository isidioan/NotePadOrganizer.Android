package com.isidioan.notepad.isidorosioannou.notepad;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by akis on 30/5/2017.
 */

public class GeocoderAddress extends IntentService {

    private ResultReceiver resultReceiver;
    public static final int SUCCESS = 100;
    public static final int FAIL = 101;
    public static final String RECEIVER = "receiver";
    public static final String LOCATION_DATA = "location_Data";
    public static final String RESULT_DATA = "resultData";

    public GeocoderAddress () {
        super("reverseGeocoder");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        resultReceiver=intent.getParcelableExtra(RECEIVER);
        Location location = intent.getParcelableExtra(LOCATION_DATA);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

        }catch (IOException e){
            e.printStackTrace();
            Log.e("Geocoder","error",e);
        }catch (IllegalArgumentException ex){
            Log.e("Geocoder","Inavlid values",ex);
        }
        if(addresses==null || addresses.size()==0){
            Log.e("Error","No addresses found");
            deliverResult(FAIL,"No addresses available");
        }
        else{
            Address address = addresses.get(0);
            ArrayList<String> fetchAddress= new ArrayList<>();
            for(int i =0; i<= address.getMaxAddressLineIndex();i++){
                fetchAddress.add(address.getAddressLine(i));
            }
            deliverResult(SUCCESS, TextUtils.join(System.getProperty("line.separator"),fetchAddress));
        }

    }

    private void deliverResult(int resultCode,String address){
        Bundle bundle = new Bundle();
        bundle.putString(RESULT_DATA,address);
        resultReceiver.send(resultCode,bundle);
    }
}
