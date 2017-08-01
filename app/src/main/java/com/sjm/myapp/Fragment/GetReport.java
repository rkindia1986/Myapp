package com.sjm.myapp.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sjm.myapp.GetReportList;
import com.sjm.myapp.R;
import com.sjm.myapp.SqlLiteDbHelper;
import com.sjm.myapp.Utils;
import com.sjm.myapp.pojo.RentRecord;
import com.sjm.myapp.pojo.RentRecordList;
import com.sjm.myapp.pojo.RentSummary;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Helly-PC on 05/31/2017.
 */

public class GetReport extends Fragment {
    private static final String TAG = "GetReport";
    ProgressDialog pd;
    private Unbinder unbinder;
    @BindView(R.id.txt_startdate)
    EditText txt_startdate;
    @BindView(R.id.txt_enddate)
    EditText txt_enddate;

    @BindView(R.id.spinner)
    Spinner spinner;

    @BindView(R.id.rdo_paid)
    RadioButton rdo_paid;
    @BindView(R.id.rdo_due)
    RadioButton rdo_due;

    @BindView(R.id.rdotype)
    RadioGroup rdotype;
    @BindView(R.id.rdo_status)
    RadioGroup rdo_status;
    @BindView(R.id.rdo_on)
    RadioButton rdo_on;
    @BindView(R.id.rdo_all)
    RadioButton rdo_all;
    @BindView(R.id.rdo_off)
    RadioButton rdo_off;

    @BindView(R.id.btn_getreport)
    Button btn_getreport;
    SqlLiteDbHelper sqlLiteDbHelper;
    ArrayList<String> cityList = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    public static RentRecordList rentRecordList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reports, container, false);
        unbinder = ButterKnife.bind(this, view);
        sqlLiteDbHelper = new SqlLiteDbHelper(getActivity());
        sqlLiteDbHelper.openDataBase();
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, cityList);
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);

        getCityList();

        txt_startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment(txt_startdate);
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

        btn_getreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getReport();
            }
        });
        return view;
    }


    public void getCityList() {

        cityList.clear();
        cityList = sqlLiteDbHelper.Get_AllCity();
        cityList.add(0,"Select City");
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, cityList);
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);
    }

    private void parseCityResponse(String body) {
        cityList.clear();
        try {

            JSONObject j = new JSONObject(body);

            if (j != null) {
                if (body.contains("status")) {
                    if (j.getString("status").equalsIgnoreCase("0")) {
                        JSONArray jsonArray = j.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            cityList.add(jsonArray.getString(i));
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Utils.ShowMessageDialog(getContext(), j.getString("message"));

                    }
                } else {
                    Utils.ShowMessageDialog(getContext(), "Error Occurred");

                }
            } else {
                Utils.ShowMessageDialog(getContext(), "Error Occurred");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Utils.ShowMessageDialog(getContext(), "Error Occurred");
        }
    }

    public void showProgressDialog() {
        pd = new ProgressDialog(getContext());
        pd.setMessage("Please wait");
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
    }

    public void hideProgressDialog() {
        try {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
        } catch (IllegalArgumentException e) {
            // Handle or log or ignore
            e.printStackTrace();
        } catch (Exception e) {
            // Handle or log or ignore
        } finally {
            pd = null;
        }
    }

    public void getReport() {
        int selectedId = rdotype.getCheckedRadioButtonId();
        String type = "";
        if (selectedId == R.id.rdo_paid) {
            type = "PAID";
        } else if (selectedId == R.id.rdo_due) {
            type = "DUE";
        }
        selectedId = rdo_status.getCheckedRadioButtonId();
        String con_status = "";
        if (selectedId == R.id.rdo_all) {
            con_status = "ALL";
        } else if (selectedId == R.id.rdo_off) {
            con_status = "off";
        } else if (selectedId == R.id.rdo_on) {
            con_status = "on";
        }
        String city="%";
        if(spinner.getSelectedItemPosition() >0)
        {
            city= spinner.getSelectedItem().toString().toString();
        }
        rentRecordList = new RentRecordList();
        ArrayList<RentRecord> rentRecords = sqlLiteDbHelper.getRentRecordbycity(city, txt_startdate.getText().toString(), txt_enddate.getText().toString(), con_status, type);
        if (rentRecords != null && rentRecords.size() > 0) {
            int total = 0;
            for (int i = 0; i < rentRecords.size(); i++) {
                total = total + Integer.parseInt(rentRecords.get(i).getPayment_amount());
            }
            RentSummary rentSummary = new RentSummary();
            rentSummary.setRent_from_date(txt_startdate.getText().toString());
            rentSummary.setRent_end_date(txt_enddate.getText().toString());
            rentSummary.setTotal_based_on_total(total + "");
            rentRecordList.setRentSummary(rentSummary);
            rentRecordList.setRentRecords(rentRecords);
            Intent intent = new Intent(getContext(), GetReportList.class);
            startActivity(intent);
        } else {
            Utils.ShowMessageDialog(getContext(), "No Record Available");
        }


    }

    private void parseGetReportResponse(String body) {
        String message = "";
        boolean Status = false;
        try {
            JSONObject j = new JSONObject(body);
            if (j != null) {
                if (body.contains("status")) {

                    if (j.getInt("status") == 1) {
                        message = j.optString("message");
                        Status = false;
                    } else if (j.getInt("status") == 0) {
                        Status = true;
                        message = j.optString("message");
                        Gson gson = new GsonBuilder().create();

                        rentRecordList = gson.fromJson(j.getJSONObject("result").toString(), RentRecordList.class);

                    } else {
                        Status = false;
                        message = "Error Occurred";
                    }
                }
            } else {
                Status = false;
                message = "Error Occurred";
            }

        } catch (Exception e) {
            e.printStackTrace();
            Status = false;
            message = "Error Occurred";

        }
        if (Status) {
            if (rentRecordList != null && rentRecordList.getRentRecords() != null && rentRecordList.getRentRecords().size() > 0) {
                Intent intent = new Intent(getContext(), GetReportList.class);
                startActivity(intent);
            } else {
                Utils.ShowMessageDialog(getContext(), "No records found");
            }
            //  Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        } else {
            Utils.ShowMessageDialog(getContext(), message);
        }
    }

}
