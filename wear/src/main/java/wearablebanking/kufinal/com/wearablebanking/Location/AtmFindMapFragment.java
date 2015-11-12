//package wearablebanking.kufinal.com.wearablebanking.Location;
//
//import android.app.Fragment;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.maps.CameraUpdate;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapView;
//import com.google.android.gms.maps.MapsInitializer;
//import com.google.android.gms.maps.model.LatLng;
//
//import wearablebanking.kufinal.com.wearablebanking.R;
//
///**
// * Created by PC on 28.10.2015.
// */
//public class AtmFindMapFragment extends Fragment {
//
//    MapView mapView;
//    GoogleMap map;
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment2, container, false);
//        // Gets the MapView from the XML layout and creates it
//
//        try {
//            MapsInitializer.initialize(getActivity());
//        } catch (GooglePlayServicesNotAvailableException e) {
//            Log.e("Address Map", "Could not initialize google play", e);
//        }
//
//        switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()) )
//        {
//            case ConnectionResult.SUCCESS:
//                Toast.makeText(getActivity(), "SUCCESS", Toast.LENGTH_SHORT).show();
//                mapView = (MapView) v.findViewById(R.id.map);
//                mapView.onCreate(savedInstanceState);
//                // Gets to GoogleMap from the MapView and does initialization stuff
//                if(mapView!=null)
//                {
//                    map = mapView.getMap();
//                    map.getUiSettings().setMyLocationButtonEnabled(false);
//                    map.setMyLocationEnabled(true);
//                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
//                    map.animateCamera(cameraUpdate);
//                }
//                break;
//            case ConnectionResult.SERVICE_MISSING:
//                Toast.makeText(getActivity(), "SERVICE MISSING", Toast.LENGTH_SHORT).show();
//                break;
//            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
//                Toast.makeText(getActivity(), "UPDATE REQUIRED", Toast.LENGTH_SHORT).show();
//                break;
//            default: Toast.makeText(getActivity(), GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()), Toast.LENGTH_SHORT).show();
//        }
//
//
//
//
//        // Updates the location and zoom of the MapView
//
//        return v;
//    }
//
//    @Override
//    public void onResume() {
//        mapView.onResume();
//        super.onResume();
//    }
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mapView.onDestroy();
//    }
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        mapView.onLowMemory();
//    }
//}