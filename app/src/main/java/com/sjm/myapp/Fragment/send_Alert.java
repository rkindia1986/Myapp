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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.sjm.myapp.ApiService;
import com.sjm.myapp.Application;
import com.sjm.myapp.NetworkConnection;
import com.sjm.myapp.R;
import com.sjm.myapp.RetroClient;
import com.sjm.myapp.SqlLiteDbHelper;
import com.sjm.myapp.Utils;

import org.json.JSONArray;
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

public class send_Alert extends Fragment {
    private static final String TAG = "send_Alert";
    ProgressDialog pd;
    private Unbinder unbinder;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.rdogrpup)
    RadioGroup rdogrpup;
    @BindView(R.id.rdo_custom)
    RadioButton rdo_custom;
    @BindView(R.id.rdo_paid)
    RadioButton rdo_paid;
    @BindView(R.id.rdo_due)
    RadioButton rdo_due;
    @BindView(R.id.btn_sendalert)
    Button btn_sendalert;
    ArrayList<String> cityList = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    SqlLiteDbHelper sqlLiteDbHelper;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.send_alert, container, false);
        unbinder = ButterKnife.bind(this, view);
        sqlLiteDbHelper = new SqlLiteDbHelper(getActivity());
        sqlLiteDbHelper.openDataBase();
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, cityList);
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);
        rdogrpup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.rdo_custom) {
                    editText.setVisibility(View.VISIBLE);

                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rdo_paid) {
                    editText.setVisibility(View.INVISIBLE);
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rdo_due) {
                    editText.setVisibility(View.INVISIBLE);
                }
            }
        });
        getCityList();


        btn_sendalert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = rdogrpup.getCheckedRadioButtonId();
                String type = "";
                String messsage = "";
                if (selectedId == R.id.rdo_paid) {
                    type = "PAID";
                    messsage = "";
                    SendAlert(type, spinner.getSelectedItem().toString().toString(), Application.preferences.getUSerid(), messsage);
                } else if (selectedId == R.id.rdo_due) {
                    type = "DUE";
                    messsage = "";
                    SendAlert(type, spinner.getSelectedItem().toString().toString(), Application.preferences.getUSerid(), messsage);
                } else if (selectedId == R.id.rdo_custom) {
                    type = "CUSTOM";
                    messsage = editText.getText().toString();
                    if (checkValidation()) {
                        SendAlert(type, spinner.getSelectedItem().toString().toString(), Application.preferences.getUSerid(), messsage);
                    }
                }


            }
        });
        return view;
    }

    public boolean checkValidation() {
        if (TextUtils.isEmpty(editText.getText().toString().trim())) {
            editText.setError("Please enter message here");
            return false;
        }
        return true;
    }

    public void SendAlert(String pay_status, String city, String userid, String stat) {

        if (NetworkConnection.isNetworkAvailable(getContext())) {
            try {
                showProgressDialog();
                ApiService api = RetroClient.getApiService();
                Call<String> call;
                if (stat.equalsIgnoreCase(""))
                    call = api.send_multiple_sms("send_multiple_sms", pay_status, city, userid);
                else
                    call = api.send_multiple_Customsms("send_multiple_sms", pay_status, city, userid, stat);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.e(TAG, "call getDetailsByQr: " + call.toString());

                        Log.e(TAG, "onResponse getDetailsByQr: " + response.body());
                        hideProgressDialog();
                        ParseResponse(response.body());

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

    private void ParseResponse(String body) {

        String message = "Error Occurred";
        boolean Status = false;
        try {
            JSONObject j = new JSONObject(body);
            if (j != null) {
                if (body.contains("message")) {


                    if (j.getString("message").equalsIgnoreCase("success")) {
                        message = j.optString("message");
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Status = false;
            message = "Error Occurred";

        }

        Utils.ShowMessageDialog(getActivity(), message);


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


}
