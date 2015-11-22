package wearablebanking.kufinal.com.wearablebanking.CustomDialogues;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by Skanek on 15.11.2015.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private DatePickerDialog.OnDateSetListener externalListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog picker = new DatePickerDialog(getActivity(), this, year, month, day);
        picker.getWindow().setLayout(320, 320);
        // Create a new instance of DatePickerDialog and return it
        return picker;
    }

    public void setOnDateSetListener(DatePickerDialog.OnDateSetListener listener) {
        this.externalListener = listener;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        if (externalListener != null)
            externalListener.onDateSet(view, year, month, day);
    }
}

