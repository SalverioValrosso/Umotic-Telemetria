package org.altervista.umotic.umotic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import java.sql.Time;
import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getContext(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT , (TimePickerDialog.OnTimeSetListener)getContext(), hour, minute, android.text.format.DateFormat.is24HourFormat(getContext()));
    }
}
