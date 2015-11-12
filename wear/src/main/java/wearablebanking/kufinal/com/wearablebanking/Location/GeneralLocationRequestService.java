package wearablebanking.kufinal.com.wearablebanking.Location;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import wearablebanking.kufinal.com.wearablebanking.BankQueue.QueueOccupationInfo;

/**
 * Created by PC on 10.11.2015.
 */
public class GeneralLocationRequestService {
    private int locationId;

    public GeneralLocationRequestService(int locationId) {
        this.locationId = locationId;
    }

    public QueueOccupationInfo requestQueue(Date requestDate)
    {
        //MOCK DATA
        String dt = requestDate.toString();  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.HOUR, 1);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        String output = sdf1.format(c.getTime());

        return new QueueOccupationInfo(3,output);
    }



}
