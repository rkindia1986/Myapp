package com.sjm.myapp.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sjm.myapp.ApiService;
import com.sjm.myapp.Application;
import com.sjm.myapp.ExpenseReportList;
import com.sjm.myapp.NetworkConnection;
import com.sjm.myapp.R;
import com.sjm.myapp.RetroClient;
import com.sjm.myapp.Utils;
import com.sjm.myapp.pojo.ExpenseReport;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    @BindView(R.id.rdogroup_1)
    RadioGroup rdogroup_1;

    @BindView(R.id.rdogroup_2)
    RadioGroup rdogroup_2;

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

    public static ExpenseReport expenseReport;

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

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
                    if (NetworkConnection.isNetworkAvailable(getContext())) {
                        try {
                            showProgressDialog();
                            int selectedId = rdogroup_1.getCheckedRadioButtonId();
                            String type = "";

                            if (selectedId == R.id.rdo_expense) {
                                type = "expense";
                            } else if (selectedId == R.id.rdo_income) {
                                type = "income";
                            }
                            Log.e("type", type);
                            ApiService api = RetroClient.getApiService();
                            Call<String> call = api.add_expense_income("add_expense_income", type, edt_desc.getText().toString(), edt_amount.getText().toString(), txt_date.getText().toString(),  Application.preferences.getUSerid());
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Log.e(TAG, "call getDetailsByQr: " + call.toString());

                                    Log.e(TAG, "onResponse getDetailsByQr: " + response.body());
                                    hideProgressDialog();
                                    parseResponse(response.body());
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
            }
        });

        btn_getreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation2()) {
                    if (NetworkConnection.isNetworkAvailable(getContext())) {
                        try {
                            showProgressDialog();
                            int selectedId = rdogroup_2.getCheckedRadioButtonId();
                            String type = "";

                            if (selectedId == R.id.rdo_typeexpense) {
                                type = "expense";
                            } else if (selectedId == R.id.rdo_typeincome) {
                                type = "income";
                            }
                            Log.e("type", type);
                            ApiService api = RetroClient.getApiService();
                            Call<String> call = api.get_expense_report("get_expense_report", type, txt_startdate.getText().toString(), txt_enddate.getText().toString());
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Log.e(TAG, "call get_expense_report: " + call.toString());

                                    Log.e(TAG, "onResponse get_expense_report: " + response.body());
                                    hideProgressDialog();
                                    parseGetReportResponse(response.body());
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    hideProgressDialog();
                                    Log.e(TAG, "onFailure get_expense_report: " + t.getMessage());
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
            }
        });
        return view;
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

                        expenseReport = gson.fromJson(body, ExpenseReport.class);

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
            if (expenseReport != null && expenseReport.getLstExpense() != null && expenseReport.getLstExpense().size() > 0) {
                Intent intent = new Intent(getContext(), ExpenseReportList.class);
                startActivity(intent);
            } else {
                Utils.ShowMessageDialog(getContext(), "No records found");
            }
            //  Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        } else {
            Utils.ShowMessageDialog(getContext(), message);
        }
    }

    private void parseResponse(String body) {
        String message = "";
        boolean Status = false;
        try {
            JSONObject j = new JSONObject(body);
            if (j != null) {
                if (body.contains("status")) {

                    if (j.getInt("status")== 1) {
                        message = j.optString("message");
                        Status = false;
                    } else if (j.getInt("status")== 0) {
                        Status = true;
                        message = j.optString("message");

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
            clearAddExpFields();
            Utils.ShowMessageDialog(getContext(), message);
            //  Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

        } else {
            Utils.ShowMessageDialog(getContext(), message);
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

    public boolean checkValidation() {

        if (TextUtils.isEmpty(edt_desc.getText().toString().trim())) {
            edt_desc.setError("Please enter desc");
            return false;
        }
        if (TextUtils.isEmpty(edt_amount.getText().toString().trim())) {
            edt_amount.setError("Please enter amount");
            return false;
        }
        if (TextUtils.isEmpty(txt_date.getText().toString().trim())) {
            txt_date.setError("Please select date");
            return false;
        }
        return true;
    }


    public void clearAddExpFields() {
        edt_amount.setText("");
        edt_desc.setText("");
        txt_date.setText("");
    }

    public boolean checkValidation2() {

        if (TextUtils.isEmpty(txt_startdate.getText().toString().trim())) {
            txt_startdate.setError("Please select date");
            return false;
        }
        if (TextUtils.isEmpty(txt_enddate.getText().toString().trim())) {
            txt_enddate.setError("Please select date");
            return false;
        }
        return true;
    }


}
