package wearablebanking.kufinal.com.wearablebanking.BankQueue;

import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;

import wearablebanking.kufinal.com.wearablebanking.R;

/**
 * Created by PC on 10.11.2015.
 */
public class BankMenuFragment extends Fragment {

    private final String queue_timer_prefs = "QueueTimerPrefs";
    private final String queue_timer_pref_name = "QueueTimerPrefsName";

    private BankFragmentListener listener;
    private SharedPreferences preferences;

    private Button get_queue_btn;
    private Button Bank_info_btn;
    String timeRemainingText;
    int queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bank_menu_fragment, container, false);

        Bank_info_btn = (Button) view.findViewById(R.id.Bank_info_btn);
        Bank_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.getInfoClicked();
            }
        });

        get_queue_btn = (Button) view.findViewById(R.id.get_queue_btn);
        get_queue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.getQueueClicked();
            }
        });

        preferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);

        String queueTimer = preferences.getString(queue_timer_pref_name, "");

        if (!queueTimer.equals("")) {
            Gson gson = new Gson();
            QueueOccupationInfo queue = gson.fromJson(queueTimer, QueueOccupationInfo.class);
            arrivalSetTimer(queue);
        }

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof BankFragmentListener) {
            listener = (BankFragmentListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implemenet BankFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void arrivalSetTimer(QueueOccupationInfo arrival) {
        queue = arrival.getQueueNum();

        long time = arrival.calculateRemainingTime();

        if (time >= 0) {

            long diffSeconds = time / 1000 % 60;
            long diffMinutes = time / (60 * 1000) % 60;
            long diffHours = time / (60 * 60 * 1000) % 24;
            long diffDays = time / (24 * 60 * 60 * 1000);

            timeRemainingText = diffDays + ":"
                    + diffHours + ":"
                    + diffMinutes + ":"
                    + diffSeconds;

            final Handler timerHandler = new Handler() {
                public void handleMessage(Message msg) {
                    get_queue_btn.setText("Your Queue Number: " + queue +
                            "\nExpected Wait Time = " + timeRemainingText);

                    if (timeRemainingText.equals("")) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove(queue_timer_pref_name);
                        editor.commit();

                        get_queue_btn.setText(getString(R.string.get_bank_queue_txt));
                    }
                }
            };

            get_queue_btn.setText("Your Queue Number: " + queue +
                    "\nExpected Wait Time = " + timeRemainingText);

            new CountDownTimer(time, 1000) {
                public void onTick(long millisUntilFinished) {
                    long diffSeconds = millisUntilFinished / 1000 % 60;
                    long diffMinutes = millisUntilFinished / (60 * 1000) % 60;
                    long diffHours = millisUntilFinished / (60 * 60 * 1000) % 24;
                    long diffDays = millisUntilFinished / (24 * 60 * 60 * 1000);
                    String text = "";

                    if (diffDays > 0) {
                        text += diffDays + ":";
                    }
                    if (diffHours > 0) {
                        text += diffHours + ":";
                    }
                    if (diffMinutes > 0) {
                        text += diffMinutes + ":";
                    }
                    if (diffSeconds > 0) {
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
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(queue_timer_pref_name);
            editor.commit();

            get_queue_btn.setText(getString(R.string.get_bank_queue_txt));
            get_queue_btn.setEnabled(true);
        }
    }

    public interface BankFragmentListener {
        public void getQueueClicked();

        public void getInfoClicked();
    }
}
