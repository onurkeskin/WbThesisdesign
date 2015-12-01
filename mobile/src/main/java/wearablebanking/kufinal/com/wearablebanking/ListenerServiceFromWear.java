package wearablebanking.kufinal.com.wearablebanking;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONArray;
import org.json.JSONObject;

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
    private static final String FINANCE_WEAR_PATH = "/request-finance";
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
            info.append(cur[0] + " : " + cur[1] + "\n");
            Iterator<Place> it = places.iterator();
            while (it.hasNext()) {
                Place closest = it.next();
                info.append(closest.getLatitude() + " : " + closest.getLongitude() + "\n");
            }
            byte[] data = info.toString().getBytes();
            String requestNode = messageEvent.getSourceNodeId();

            mGoogleApiClient.blockingConnect(100000, TimeUnit.MILLISECONDS);
            Wearable.MessageApi.sendMessage(mGoogleApiClient, requestNode, ATM_WEAR_PATH, data);
            mGoogleApiClient.disconnect();

        } else if (messageEvent.getPath().equals(FINANCE_WEAR_PATH)) {
            GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Wearable.API)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
            String requestNode = messageEvent.getSourceNodeId();

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            String[] urls = {"https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20csv%20where%20url%3D'http%3A%2F%2Fdownload.finance.yahoo.com%2Fd%2Fquotes.csv%3Fs%3Ding%26f%3Dsl1d1t1c1ohgv%26e%3D.csv'&format=json&diagnostics=true&callback="
                    , "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20csv%20where%20url%3D'http%3A%2F%2Fdownload.finance.yahoo.com%2Fd%2Fquotes.csv%3Fs%3Dmepet.is%26f%3Dsl1d1t1c1ohgv%26e%3D.csv'&format=json&diagnostics=true&callback="
                    , "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20csv%20where%20url%3D'http%3A%2F%2Fdownload.finance.yahoo.com%2Fd%2Fquotes.csv%3Fs%3DGold%26f%3Dsl1d1t1c1ohgv%26e%3D.csv'&format=json&diagnostics=true&callback="
            };
            Integer count = urls.length;

            final StringBuilder toSend = new StringBuilder();
//            for(String url: urls) {
//                // Request a string response from the provided URL.
//                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                        new decrementelListener(count) {
//                            @Override
//                            public void onResponse(String response) {
//                                // Display the first 500 characters of the response string.
//                                try {
//                                    JSONObject jObject = new JSONObject(response);
//                                    JSONObject query = jObject.getJSONObject("query");
//                                    JSONObject results = query.getJSONObject("results");
//                                    JSONObject rows = results.getJSONObject("row");
//                                    String res = rows.getString("col0") + ":" + rows.getString("col1");
//                                    toSend.append(res);
//                                    toSend.append("\n");
//                                    num--;
//                                } catch (Exception e) {
//
//                                } finally {
//
//                                }
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                    }
//                });
            // Add the request to the RequestQueue.
            // Request a string response from the provided URL.

            for (String url : urls) {
//                // Request a string response from the provided URL.
                RequestFuture<String> future = RequestFuture.newFuture();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, future, future);

                // Add the request to the RequestQueue.
                queue.add(stringRequest);
                try{
                String response = future.get();
                JSONObject jObject = new JSONObject(response);
                JSONObject query = jObject.getJSONObject("query");
                JSONObject results = query.getJSONObject("results");
                JSONObject rows = results.getJSONObject("row");
                String res = rows.getString("col0") + ":" + rows.getString("col1");
                toSend.append(res);
                toSend.append("\n");
                }catch(Exception e){

                }
            }

            Wearable.MessageApi.sendMessage(mGoogleApiClient, requestNode, FINANCE_WEAR_PATH, toSend.toString().getBytes());
            mGoogleApiClient.disconnect();
        }
    }

    class decrementelListener implements Response.Listener<String> {
        Integer num;

        public decrementelListener(Integer num) {
            this.num = num;
        }

        @Override
        public void onResponse(String response) {

        }
    }

}
