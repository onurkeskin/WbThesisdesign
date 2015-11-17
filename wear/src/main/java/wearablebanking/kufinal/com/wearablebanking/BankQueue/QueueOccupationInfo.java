package wearablebanking.kufinal.com.wearablebanking.BankQueue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.google.gson.annotations.SerializedName;;

/**
 * Created by PC on 10.11.2015.
 */
public class QueueOccupationInfo {
    @SerializedName("queueNumber")
    private int queueNum;

    @SerializedName("meetingDate")
    private String expectedDueTime;

    public QueueOccupationInfo(int queueNum, String expectedDueTime) {
        this.queueNum = queueNum;
        this.expectedDueTime = expectedDueTime;
    }

    public long calculateRemainingTime(){

        String dt = expectedDueTime.toString();  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");

        Calendar c = Calendar.getInstance();
        Date curDate = c.getTime();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date appointmentTime = c.getTime();

        long diff = appointmentTime.getTime() - curDate.getTime();

        return diff;
    }

    public int getQueueNum() {
        return queueNum;
    }
}
