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
            String data = new String(messageEvent.getData());
            Intent location = new Intent(this, NavigationActivity.class);
            location.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            location.putExtra("Locations", data);
            startActivity(location);
        }
    }

}
