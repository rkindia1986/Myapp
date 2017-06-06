package com.sjm.myapp.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.sjm.myapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Helly-PC on 05/31/2017.
 */

public class AddCust_Fragment extends Fragment   {
    private static final String TAG = "AddCust_Fragment";
    ProgressDialog pd;
    private Unbinder unbinder;
    @BindView(R.id.cust_no)
    EditText cust_no;
    @BindView(R.id.cust_name)
    EditText cust_name;
    @BindView(R.id.cust_add)
    EditText cust_add;
    @BindView(R.id.cust_city)
    EditText cust_city;
    @BindView(R.id.amt)
    EditText amt;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.rent_amt)
    EditText rent_amt;
    @BindView(R.id.stb_acc_no)
    EditText stb_acc_no;
    @BindView(R.id.stb_nuid)
    EditText stb_nuid;

    @BindView(R.id.cafno)
    EditText cafno;
    @BindView(R.id.stb_ac2)
    EditText stb_ac2;
    @BindView(R.id.stb_nuid2)
    EditText stb_nuid2;
    @BindView(R.id.cafno2)
    EditText cafno2;
    @BindView(R.id.rdogroup)
    RadioGroup rdogroup;
    @BindView(R.id.rdo_manual)
    RadioButton rdo_manual;
    @BindView(R.id.rdo_continus)
    RadioButton rdo_continus;
    @BindView(R.id.rdo_plan)
    RadioButton rdo_plan;
    @BindView(R.id.btn_addcust)
    Button btn_addcust;

    @BindView(R.id.edt_month)
    EditText edt_month;

    @BindView(R.id.lyt_rentplanchange)
    Button lyt_rentplanchange;
    ArrayList<String> s =new ArrayList<String>();
    @BindView(R.id.datePicker)
    DatePicker datePicker;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_customer, container, false);
        unbinder = ButterKnife.bind(this, view);
        btn_addcust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkValidation()){

                }
            }
        });

        rdogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = rdogroup.getCheckedRadioButtonId();
                if(selectedId== R.id.rdo_manual)
                {
                    lyt_rentplanchange.setVisibility(View.GONE);
                }
                else if(selectedId== R.id.rdo_continus)
                {
                    edt_month.setEnabled(false);
                    lyt_rentplanchange.setVisibility(View.VISIBLE);
                }
                else if(selectedId== R.id.rdo_plan)
                {
                    edt_month.setEnabled(true);
                    lyt_rentplanchange.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }

    public boolean checkValidation()
    {
        if (TextUtils.isEmpty(cust_no.getText().toString().trim()) ) {
            cust_no.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(cust_name.getText().toString().trim()) ) {
            cust_name.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(cust_add.getText().toString().trim()) ) {
            cust_add.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(cust_city.getText().toString().trim()) ) {
            cust_city.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(amt.getText().toString().trim()) ) {
            amt.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(rent_amt.getText().toString().trim()) ) {
            rent_amt.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(stb_acc_no.getText().toString().trim()) ) {
            stb_acc_no.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(stb_nuid.getText().toString().trim()) ) {
            stb_nuid.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(cafno.getText().toString().trim()) ) {
            cafno.setError("Please fill data");
            return false;
        }

        return true;
    }

}
