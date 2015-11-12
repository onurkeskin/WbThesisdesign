package wearablebanking.kufinal.com.wearablebanking.BankQueue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by PC on 10.11.2015.
 */
public class QueueOccupationInfo {
    private int queueNum;
    private String expectedDueTime;

    public QueueOccupationInfo(int queueNum, String expectedDueTime) {
        this.queueNum = queueNum;
        this.expectedDueTime = expectedDueTime;
    }

    public String calculateRemainingTime(){

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

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        return diffDays + " " +  diffHours + " " + diffMinutes + " " + diffSeconds;
    }

    public int getQueueNum() {
        return queueNum;
    }
}
