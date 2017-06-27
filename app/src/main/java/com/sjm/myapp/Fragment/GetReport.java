package com.sjm.myapp.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.sjm.myapp.ApiService;
import com.sjm.myapp.ExpenseReportList;
import com.sjm.myapp.GetReportList;
import com.sjm.myapp.NetworkConnection;
import com.sjm.myapp.R;
import com.sjm.myapp.RentList;
import com.sjm.myapp.RetroClient;
import com.sjm.myapp.Utils;
import com.sjm.myapp.ViewDtails;
import com.sjm.myapp.pojo.RentRecordList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sjm.myapp.Fragment.manageExp.expenseReport;

/**
 * Created by Helly-PC on 05/31/2017.
 */

public class GetReport extends Fragment   {
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

    ArrayList<String> cityList = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    public static RentRecordList rentRecordList;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reports, container, false);
        unbinder = ButterKnife.bind(this, view);

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

        if (NetworkConnection.isNetworkAvailable(getContext())) {
            try {
                showProgressDialog();
                ApiService api = RetroClient.getApiService();
                Call<String> call = api.get_city_list("get_city_list");
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.e(TAG, "call getDetailsByQr: " + call.toString());

                        Log.e(TAG, "onResponse getDetailsByQr: " + response.body());
                        hideProgressDialog();
                        parseCityResponse(response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        hideProgressDialog();
                        Log.e(TAG, "onFailure getDetailsByQr: " + t.getMessage());
                        Utils.ShowMessageDialog(getContext(), "Error Occurred");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Utils.ShowMessageDialog(getContext(), "No Connection Available");
        }

    }

    private void parseCityResponse(String body) {
        cityList.clear();
        try {

            JSONObject j = new JSONObject(body);

            if (j != null) {
                if (body.contains("status")) {
                    if (j.getString("status").equalsIgnoreCase("0")) {
                        JSONArray jsonArray =  j.getJSONArray("result");
                        for(int i=0;i<jsonArray.length();i++)
                        {
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

        if (NetworkConnection.isNetworkAvailable(getContext())) {
            try {
                showProgressDialog();
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
                }else if (selectedId == R.id.rdo_on) {
                    con_status = "on";
                }




                ApiService api = RetroClient.getApiService();
                Call<String> call = api.get_rent_report("get_rent_report",txt_startdate.getText().toString(),txt_enddate.getText().toString(),con_status,type,spinner.getSelectedItem().toString());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.e(TAG, "call getDetailsByQr: " + call.toString());

                        Log.e(TAG, "onResponse getDetailsByQr: " + response.body());
                        hideProgressDialog();
                        parseGetReportResponse(response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        hideProgressDialog();
                        Log.e(TAG, "onFailure getDetailsByQr: " + t.getMessage());
                        Utils.ShowMessageDialog(getContext(), "Error Occurred");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Utils.ShowMessageDialog(getContext(), "No Connection Available");
        }

    }

    private void parseGetReportResponse(String body) {
        String message = "";
        boolean Status = false;
        try {
            JSONObject j = new JSONObject(body);
            if (j != null) {
                if (body.contains("status")) {

                    if (j.getString("status").equalsIgnoreCase("1")) {
                        message = j.optString("message");
                        Status = false;
                    } else if (j.getString("status").equalsIgnoreCase("0")) {
                        Status = true;
                        message = j.optString("message");
                        Gson gson = new GsonBuilder().create();

                        rentRecordList = gson.fromJson(body, RentRecordList.class);
                        startActivity(new Intent(getContext(), GetReportList.class));
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
