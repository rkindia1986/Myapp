package com.sjm.myapp.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Helly-PC on 06/01/2017.
 */

public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    TextView txt;

    SelectDateFragment(TextView txt) {
        this.txt = txt;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        populateSetDate(yy, mm + 1, dd);
    }

    public void populateSetDate(int year, int month, int day) {
        String m = "";
        if (month < 10) {
            m = "0" + month;
        } else {
            m = month + "";
        }
        String d = "";
        if (day < 10) {
            d = "0" + day;
        } else {
            d = day + "";
        }
        this.txt.setText(year + "-" + m + "-" + d);
    }

}