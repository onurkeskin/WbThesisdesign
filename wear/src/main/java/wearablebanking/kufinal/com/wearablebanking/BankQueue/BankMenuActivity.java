package wearablebanking.kufinal.com.wearablebanking.BankQueue;

import android.animation.TimeAnimator;
import android.app.Activity;


import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;


import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import wearablebanking.kufinal.com.wearablebanking.CustomDialogues.DatePickerFragment;
import wearablebanking.kufinal.com.wearablebanking.CustomDialogues.TimePickerFragment;
import wearablebanking.kufinal.com.wearablebanking.Location.GeneralLocationRequestService;
import wearablebanking.kufinal.com.wearablebanking.Location.LocationInfoService;
import wearablebanking.kufinal.com.wearablebanking.R;
import com.google.gson.Gson;


public class BankMenuActivity extends FragmentActivity implements BankMenuFragment.BankFragmentListener{


    private String choosenDate;
    private BankMenuFragment fragment;

    private GeneralLocationRequestService req;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_menu);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new BankMenuFragment();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();


        Intent intent = getIntent();
        double[] coors = intent.getDoubleArrayExtra("coordinates");
        req = LocationInfoService.getInstance().parseLocation(coors);


    }



    @Override
    public void getQueueClicked() {
        final DatePickerFragment pickDate = new DatePickerFragment();
        final TimePickerFragment pickTime = new TimePickerFragment();


        final Calendar c = Calendar.getInstance();
        pickTime.setOnTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                c.set(Calendar.MINUTE,minute);
            }
        });

        pickDate.setOnDateSetListener(new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                pickTime.show(getFragmentManager(), "timePicker");
                c.set(year,monthOfYear,dayOfMonth);
            }
        });

        pickDate.show(getFragmentManager(), "datePicker");

        QueueOccupationInfo arrival = req.requestQueue(c.getTime());

        fragment.arrivalSetTimer(arrival);
    }

    @Override
    public void getInfoClicked() {

    }

}
