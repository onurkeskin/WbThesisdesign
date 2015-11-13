package wearablebanking.kufinal.com.wearablebanking.BankQueue;

import android.animation.TimeAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import wearablebanking.kufinal.com.wearablebanking.Location.GeneralLocationRequestService;
import wearablebanking.kufinal.com.wearablebanking.Location.LocationInfoService;
import wearablebanking.kufinal.com.wearablebanking.R;
import com.google.gson.Gson;


public class BankMenuActivity extends Activity implements BankMenuFragment.BankFragmentListener{

    private final String queue_timer_prefs = "QueueTimerPrefs";
    private final String queue_timer_pref_name = "QueueTimerPrefsName";

    private String timeRemainingText;
    private int queue;

    private Button Bank_info_btn;
    private Button get_queue_btn;
    private GeneralLocationRequestService req;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_menu);

        Bank_info_btn = (Button) findViewById(R.id.Bank_info_btn);
        Bank_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfoClicked();
            }
        });

        get_queue_btn = (Button) findViewById(R.id.get_queue_btn);
        get_queue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQueueClicked();
            }
        });


        Intent intent = getIntent();
        double[] coors = intent.getDoubleArrayExtra("coordinates");
        req = LocationInfoService.getInstance().parseLocation(coors);

        SharedPreferences settings = getSharedPreferences(queue_timer_prefs, 0);
        String queueTimer = settings.getString(queue_timer_pref_name, "");

        if(!queueTimer.equals("")){
            Gson gson = new Gson();
            QueueOccupationInfo queue = gson.fromJson(queueTimer, QueueOccupationInfo.class);
            arrivalSetTimer(queue);
        }

    }



    @Override
    public void getQueueClicked() {
        Calendar c = Calendar.getInstance();

        QueueOccupationInfo arrival = req.requestQueue(c.getTime());

        arrivalSetTimer(arrival);
    }

    @Override
    public void getInfoClicked() {

    }

    private void arrivalSetTimer(QueueOccupationInfo arrival){
        long time = arrival.calculateRemainingTime();
        queue = arrival.getQueueNum();

        long diffSeconds = time / 1000 % 60;
        long diffMinutes = time / (60 * 1000) % 60;
        long diffHours = time / (60 * 60 * 1000) % 24;
        long diffDays = time / (24 * 60 * 60 * 1000);

        Gson gson = new Gson();
        String json = gson.toJson(arrival);

        SharedPreferences settings = getSharedPreferences(queue_timer_prefs, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(queue_timer_pref_name, json);
        editor.commit();

        timeRemainingText = diffDays + ":"
                + diffHours + ":"
                + diffMinutes + ":"
                + diffSeconds;

        final Handler timerHandler = new Handler(){
            public void handleMessage(Message msg) {
                get_queue_btn.setText("Your Queue Number: " + queue +
                        "\nExpected Wait Time = " + timeRemainingText);
            }
        };

        get_queue_btn.setText("Your Queue Number: " + queue +
                "\nExpected Wait Time = " + timeRemainingText);

        new CountDownTimer(time, 1000){
            public void onTick(long millisUntilFinished) {
                long diffSeconds = millisUntilFinished / 1000 % 60;
                long diffMinutes = millisUntilFinished / (60 * 1000) % 60;
                long diffHours = millisUntilFinished / (60 * 60 * 1000) % 24;
                long diffDays = millisUntilFinished / (24 * 60 * 60 * 1000);
                String text = "";

                if(diffDays > 0){
                    text += diffDays + ":";
                }
                if(diffHours > 0){
                    text += diffHours + ":";
                }
                if(diffMinutes > 0){
                    text += diffMinutes + ":";
                }
                if(diffSeconds > 0){
                    text += diffSeconds;
                }

                timeRemainingText = text;
                timerHandler.dispatchMessage(new Message());
            }

            @Override
            public void onFinish() {

            }
        }.start();
        get_queue_btn.setEnabled(false);
    }
}
