package wearablebanking.kufinal.com.wearablebanking;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import se.walkercrou.places.Place;

/**
 * Created by onur on 5.11.2015.
 */
public class ListenerServiceFromWear extends WearableListenerService {

    private static final String ATM_WEAR_PATH = "/request-location-atm";
    private static final String TAG = "listenerTag";


    double[] prev = new double[2];
    double[] cur = new double[2];
    boolean updated = false;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        /*
         * Receive the message from wear
         */
        if (messageEvent.getPath().equals(ATM_WEAR_PATH)) {
            GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Wearable.API)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();

            MapFinder finder = new MapFinder(getResources().getString(R.string.google_maps_key_mobile));

            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            cur[1] = mLastLocation.getLongitude();
            cur[0] = mLastLocation.getLatitude();
            List<Place> places = finder.getAtmClosest(cur[0], cur[1]);
//            Place closest = finder.getAtmClosest(cur[0], cur[1]).get(0);
            updated = false;

            StringBuilder info = new StringBuilder();
            info.append( cur[0] + " : " + cur[1] + "\n");
            Iterator<Place> it = places.iterator();
            while(it.hasNext()){
                Place closest = it.next();
                info.append(closest.getLatitude() + " : " + closest.getLongitude() + "\n");
            }
            byte[] data = info.toString().getBytes();
            String requestNode = messageEvent.getSourceNodeId();

            mGoogleApiClient.blockingConnect(100000, TimeUnit.MILLISECONDS);
            Wearable.MessageApi.sendMessage(mGoogleApiClient, requestNode, ATM_WEAR_PATH, data);
            mGoogleApiClient.disconnect();

        }

    }


}
