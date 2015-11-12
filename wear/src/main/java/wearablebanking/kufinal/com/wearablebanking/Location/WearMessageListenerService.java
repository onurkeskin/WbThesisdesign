package wearablebanking.kufinal.com.wearablebanking.Location;

import android.content.Intent;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by onur on 5.11.2015.
 */
public class WearMessageListenerService extends WearableListenerService {


    private static final String PATH = "/request-location-atm";
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if( messageEvent.getPath().equalsIgnoreCase( PATH ) ) {
            String loc = new String(messageEvent.getData());
            String locX = loc.split(" : ")[0];
            String locY = loc.split(" : ")[1];
            Intent location = new Intent(this, NavigationActivity.class);
            location.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            location.putExtra("Location_Requested_x", locX);
            location.putExtra("Location_Requested_y", locY);
            startActivity(location);
        }
    }

}
