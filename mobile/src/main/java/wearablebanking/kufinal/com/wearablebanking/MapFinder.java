package wearablebanking.kufinal.com.wearablebanking;

import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;


import com.google.android.gms.location.LocationListener;

import java.util.List;

import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Param;
import se.walkercrou.places.Place;
import wearablebanking.kufinal.com.wearablebanking.R;

/**
 * Created by PC on 4.11.2015.
 */
public class MapFinder {


    private String key;
    private GooglePlaces client;

    public MapFinder(String key){
        this.key = key;
        client = new GooglePlaces(key);
    }

    public List<Place> getAtmClosest(double latitude, double longitude){

//        List<Place> places = client.getPlacesByQuery("Ing Bank Atm", 10);
        List<Place> places = client.getPlacesByRadar(latitude, longitude, 10000, 10, Param.name("name").value("Ing Bank Atm"),Param.name("rankby").value("distance"));

        return places;
    }

}
