package com.sjm.myapp.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.sjm.myapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Helly-PC on 05/31/2017.
 */

public class manageExp extends Fragment {
    private static final String TAG = "manageExp";
    ProgressDialog pd;
    private Unbinder unbinder;

    DatePickerDialog datePickerDialog;
    @BindView(R.id.rdo_expense)
    RadioButton rdo_expense;
    @BindView(R.id.rdo_income)
    RadioButton rdo_income;

    @BindView(R.id.edt_desc)
    EditText edt_desc;
    @BindView(R.id.edt_amount)
    EditText edt_amount;
    @BindView(R.id.txt_date)
    EditText txt_date;

    @BindView(R.id.btn_add)
    Button btn_add;

    @BindView(R.id.txt_startdate)
    EditText txt_startdate;
    @BindView(R.id.txt_enddate)
    EditText txt_enddate;

    @BindView(R.id.rdo_typeexpense)
    RadioButton rdo_typeexpense;
    @BindView(R.id.rdo_typeincome)
    RadioButton rdo_typeincome;

    @BindView(R.id.btn_getreport)
    Button btn_getreport;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manage_expense, container, false);
        unbinder = ButterKnife.bind(this, view);

        txt_startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment(txt_startdate);
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });
        txt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment(txt_date);
                newFragment.show(getFragmentManager(), "DatePicker");
                          }
        });

        txt_enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment(txt_enddate);
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });


        return view;
    }


}
