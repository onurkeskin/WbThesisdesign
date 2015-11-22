package wearablebanking.kufinal.com.wearablebanking.CustomDialogues;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Skanek on 15.11.2015.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private TimePickerDialog.OnTimeSetListener externalListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        TimePickerDialog picker = new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

        picker.getWindow().setLayout(320, 320);

        return picker;
    }

    public void setOnTimeSetListener(TimePickerDialog.OnTimeSetListener listener) {
        this.externalListener = listener;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        if (externalListener != null)
            externalListener.onTimeSet(view, hourOfDay, minute);
    }
}

