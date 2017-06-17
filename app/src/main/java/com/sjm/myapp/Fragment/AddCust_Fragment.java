package com.sjm.myapp.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.sjm.myapp.ApiService;
import com.sjm.myapp.NetworkConnection;
import com.sjm.myapp.R;
import com.sjm.myapp.RetroClient;
import com.sjm.myapp.Utils;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Helly-PC on 05/31/2017.
 */

public class AddCust_Fragment extends Fragment {
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
    LinearLayout lyt_rentplanchange;
    ArrayList<String> s = new ArrayList<String>();
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
                if (checkValidation()) {
                    if (NetworkConnection.isNetworkAvailable(getContext())) {
                        try {
                            showProgressDialog();
                            ApiService api = RetroClient.getApiService();
                            int selectedId = rdogroup.getCheckedRadioButtonId();
                            String conn_type = "";
                            String conn_status = "on";
                            if (selectedId == R.id.rdo_manual) {
                                conn_type = "manual";
                            } else if (selectedId == R.id.rdo_continus) {
                                conn_type = "continuous";
                            } else if (selectedId == R.id.rdo_plan) {
                                conn_type = "plan";
                            }
                            String sdate = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                            Log.e("sdate", sdate);

                            Call<String> call = api.add_customer("add_customer", cust_name.getText().toString(), cust_no.getText().toString(), cust_add.getText().toString(), cust_city.getText().toString(), amt.getText().toString(), phone.getText().toString(), rent_amt.getText().toString(), stb_acc_no.getText().toString(), stb_nuid.getText().toString(), cafno.getText().toString(), stb_ac2.getText().toString(), stb_nuid2.getText().toString(), cafno2.getText().toString(), conn_type, conn_status, sdate, edt_month.getText().toString(), "1");
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

        rdogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = rdogroup.getCheckedRadioButtonId();
                if (selectedId == R.id.rdo_manual) {
                    lyt_rentplanchange.setVisibility(View.GONE);
                } else if (selectedId == R.id.rdo_continus) {
                    edt_month.setEnabled(false);
                    lyt_rentplanchange.setVisibility(View.VISIBLE);
                } else if (selectedId == R.id.rdo_plan) {
                    edt_month.setEnabled(true);
                    lyt_rentplanchange.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }

    private void parseResponse(String body) {
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

                        if (j.getString("message").equalsIgnoreCase("success")) {
                            Status = true;
                            message = j.optString("message");

                        } else {
                            Status = false;
                            message = j.optString("message");
                        }
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
            Toast.makeText(getContext(), "Customer added successfully ", Toast.LENGTH_LONG).show();
            cleardata();
        } else {
            Utils.ShowMessageDialog(getContext(), message);
        }
    }

    public boolean checkValidation() {
        if (TextUtils.isEmpty(cust_no.getText().toString().trim())) {
            cust_no.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(cust_name.getText().toString().trim())) {
            cust_name.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(cust_add.getText().toString().trim())) {
            cust_add.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(cust_city.getText().toString().trim())) {
            cust_city.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(amt.getText().toString().trim())) {
            amt.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(rent_amt.getText().toString().trim())) {
            rent_amt.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(stb_acc_no.getText().toString().trim())) {
            stb_acc_no.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(stb_nuid.getText().toString().trim())) {
            stb_nuid.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(cafno.getText().toString().trim())) {
            cafno.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(phone.getText().toString().trim())) {
            phone.setError("Please fill data");
            return false;
        }
        if (phone.getText().toString().trim().length() < 8) {
            phone.setError("Invalid phone number");
            return false;
        }

        int selectedId = rdogroup.getCheckedRadioButtonId();
        if (selectedId == R.id.rdo_plan) {
            if (TextUtils.isEmpty(edt_month.getText().toString().trim())) {
                edt_month.setError("Please fill data");
                return false;
            }
        }
        return true;
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

    public void cleardata() {
        cust_no.setText("");
        cust_name.setText("");
        cust_add.setText("");
        cust_city.setText("");
        amt.setText("");
        phone.setText("");
        rent_amt.setText("");
        stb_acc_no.setText("");
        stb_nuid.setText("");
        cafno.setText("");
        stb_ac2.setText("");
        stb_nuid2.setText("");
        cafno2.setText("");
        rdo_manual.setChecked(true);


    }


}