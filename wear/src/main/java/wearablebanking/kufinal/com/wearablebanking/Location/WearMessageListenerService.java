package wearablebanking.kufinal.com.wearablebanking.Location;

import android.content.Intent;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import wearablebanking.kufinal.com.wearablebanking.Finance.UserFinanceActivity;

/**
 * Created by onur on 5.11.2015.
 */
public class WearMessageListenerService extends WearableListenerService {


    private static final String ATM_WEAR_PATH = "/request-location-atm";
    private static final String FINANCE_WEAR_PATH = "/request-finance";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if( messageEvent.getPath().equalsIgnoreCase( ATM_WEAR_PATH ) ) {
            String data = new String(messageEvent.getData());
            Intent location = new Intent(this, NavigationActivity.class);
            location.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            location.putExtra("Locations", data);
            startActivity(location);
        } else
        if( messageEvent.getPath().equalsIgnoreCase( FINANCE_WEAR_PATH ) ) {
            String data = new String(messageEvent.getData());
            Intent finance = new Intent(this, UserFinanceActivity.class);
            finance.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finance.putExtra("Finances", data);
            startActivity(finance);
        }
    }

}
