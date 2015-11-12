package wearablebanking.kufinal.com.wearablebanking.BankQueue;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
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

public class BankMenuActivity extends Activity implements BankMenuFragment.BankFragmentListener{


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
    }



    @Override
    public void getQueueClicked() {
        Calendar c = Calendar.getInstance();

        QueueOccupationInfo arrival = req.requestQueue(c.getTime());

        String time = arrival.calculateRemainingTime();
        int queue = arrival.getQueueNum();

        String[] times = time.split(" ");
        String totalTime = times[0]+ ":" + times[1]+ ":" + times[2]+ ":" + times[3];
        get_queue_btn.setText("Your Queue Number: " + queue + "\nExpected Wait Time = " + totalTime);
        get_queue_btn.setEnabled(false);
    }

    @Override
    public void getInfoClicked() {

    }
}
