package wearablebanking.kufinal.com.wearablebanking.Location;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.DismissOverlayView;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.android.gms.wearable.WearableStatusCodes;

import java.util.List;

import se.walkercrou.places.Place;
import wearablebanking.kufinal.com.wearablebanking.BankQueue.BankMenuActivity;
import wearablebanking.kufinal.com.wearablebanking.R;

public class NavigationActivity extends WearableActivity implements OnMapReadyCallback,
        GoogleMap.OnMapLongClickListener {


//    private TextView closest_atm_location_label;
    /**
     * Overlay that shows a short help text when first launched. It also provides an option to
     * exit the app.
     */
    private DismissOverlayView mDismissOverlay;

    /**
     * The map. It is initialized when the map has been fully loaded and is ready to be used.
     *
     * @see #onMapReady(com.google.android.gms.maps.GoogleMap)
     */
    private GoogleMap mMap;

    private MapFragment mMapFragment;


    GoogleApiClient googleApiClient;
    private String[] locs;

    private double currentLatLan[];

    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        // Set the layout. It only contains a SupportMapFragment and a DismissOverlay.
        setContentView(R.layout.activity_navigation);

        // Enable ambient support, so the map remains visible in simplified, low-color display
        // when the user is no longer actively using the app but the app is still visible on the
        // watch face.
        setAmbientEnabled();

        Intent intent = getIntent();
        String data = intent.getStringExtra("Locations");
        locs = data.split("\n");

        // Retrieve the containers for the root of the layout and the map. Margins will need to be
        // set on them to account for the system window insets.
        final FrameLayout topFrameLayout = (FrameLayout) findViewById(R.id.root_container);
        final FrameLayout mapFrameLayout = (FrameLayout) findViewById(R.id.map_container);

//        closest_atm_location_label = (TextView) findViewById(R.id.closest_atm_location_label);
//        closest_atm_location_label.setText("Displaying" + user_location_x + " " + user_location_y);
//        closest_atm_location_label.setOnClickListener(new IngMapListener(new double[]{Double.parseDouble(user_location_x) , Double.parseDouble(user_location_y)}));

        // Set the system view insets on the containers when they become available.
        topFrameLayout.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                // Call through to super implementation and apply insets
                insets = topFrameLayout.onApplyWindowInsets(insets);

                FrameLayout.LayoutParams params =
                        (FrameLayout.LayoutParams) mapFrameLayout.getLayoutParams();

                // Add Wearable insets to FrameLayout container holding map as margins
                params.setMargins(
                        insets.getSystemWindowInsetLeft(),
                        insets.getSystemWindowInsetTop(),
                        insets.getSystemWindowInsetRight(),
                        insets.getSystemWindowInsetBottom());
                mapFrameLayout.setLayoutParams(params);

                return insets;
            }
        });

        // Obtain the DismissOverlayView and display the intro help text.
        mDismissOverlay = (DismissOverlayView) findViewById(R.id.dismiss_overlay);
        mDismissOverlay.setIntroText(R.string.intro_text);
        mDismissOverlay.showIntroIfNecessary();

        // Obtain the MapFragment and set the async listener to be notified when the map is ready.
        mMapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        googleApiClient.connect();
    }

    /**
     * Starts ambient mode on the map.
     * The API swaps to a non-interactive and low-color rendering of the map when the user is no
     * longer actively using the app.
     */
    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        mMapFragment.onEnterAmbient(ambientDetails);
    }

    /**
     * Exits ambient mode on the map.
     * The API swaps to the normal rendering of the map when the user starts actively using the app.
     */
    @Override
    public void onExitAmbient() {
        super.onExitAmbient();
        mMapFragment.onExitAmbient();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Map is ready to be used.
        mMap = googleMap;

        // Set the long click listener as a way to exit the map.
        mMap.setOnMapLongClickListener(this);
        currentLatLan = new double[2];
        double focusedLatLan[] = new double[2];
        double cMinDist = Double.MAX_VALUE;

        if (locs.length > 0) {
            String loc = locs[0];
            String locXStr = loc.split(" : ")[0];
            String locYStr = loc.split(" : ")[1];

            double locX = Double.parseDouble(locXStr);
            double locY = Double.parseDouble(locYStr);
            currentLatLan[0] = locX;
            currentLatLan[1] = locY;

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(locX, locY)));
        }
        // ADD MARKERS;
        for (int i = 1; i < locs.length; i++) {
            String loc = locs[i];
            String locXStr = loc.split(" : ")[0];
            String locYStr = loc.split(" : ")[1];

            double locX = Double.parseDouble(locXStr);
            double locY = Double.parseDouble(locYStr);

            double distanceToUser = getDistance(currentLatLan[0], currentLatLan[1], locX, locY);
            if (cMinDist > distanceToUser) {
                cMinDist = distanceToUser;
                focusedLatLan[0] = locX;
                focusedLatLan[1] = locY;
            }

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(locX, locY))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ingbanklogosmall)));
        }
        CameraUpdate center =
                CameraUpdateFactory.newLatLng(new LatLng(focusedLatLan[0],
                        focusedLatLan[1]));

        mMap.moveCamera(center);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        mMap.animateCamera(zoom);
        mMap.setOnMarkerClickListener(new IngMarkerListener());

        mMap.
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        // Display the dismiss overlay with a button to exit this activity.
        mDismissOverlay.show();
    }


    private class IngMapListener implements View.OnClickListener {

        double[] coors;

        public IngMapListener(double[] coors) {
            this.coors = coors;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), BankMenuActivity.class);
            intent.putExtra("coordinates", coors);
            NavigationActivity.this.startActivity(intent);
        }
    }

    private class IngMarkerListener implements GoogleMap.OnMarkerClickListener {
        @Override
        public boolean onMarkerClick(Marker marker) {
            if (marker.getPosition().latitude != currentLatLan[0] && marker.getPosition().longitude != currentLatLan[1]) {
                LatLng pos = marker.getPosition();
                double[] darray = new double[]{pos.latitude, pos.longitude};
                Intent intent = new Intent(getApplicationContext(), BankMenuActivity.class);
                intent.putExtra("coordinates", darray);
                NavigationActivity.this.startActivity(intent);
                return true;
            }
            return false;
        }
    }

    public double getDistance(LatLng from, LatLng to) {
        return Math.pow(from.latitude - to.latitude, 2) + Math.pow(from.longitude - to.longitude, 2);
    }

    public double getDistance(double from1, double from2, double to1, double to2) {
        return Math.pow(from1 - to1, 2) + Math.pow(from2 - to2, 2);
    }
}