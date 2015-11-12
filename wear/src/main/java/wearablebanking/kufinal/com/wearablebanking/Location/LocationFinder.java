package wearablebanking.kufinal.com.wearablebanking.Location;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.Set;

/**
 * Created by PC on 4.11.2015.
 */
public class LocationFinder {

    private GoogleApiClient mGoogleApiClient;
    private static final String LOCATION_CAPABILITY_NAME = "voice_transcription";

    public void LocationFinder(){

    }



    public void requestFromHandheld(){
        CapabilityApi.GetCapabilityResult result =
                Wearable.CapabilityApi.getCapability(
                        mGoogleApiClient, "",
                        CapabilityApi.FILTER_REACHABLE).await();

    }

    private String pickBestNodeId(Set<Node> nodes) {
        String bestNodeId = null;
        // Find a nearby node or pick one arbitrarily
        for (Node node : nodes) {
            if (node.isNearby()) {
                return node.getId();
            }
            bestNodeId = node.getId();
        }
        return bestNodeId;
    }
}
