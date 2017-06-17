package com.sjm.myapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Helly-PC on 06/02/2017.
 */

public class AddLicence extends AppCompatActivity {
    private static final String TAG = "AddLicence";
    ProgressDialog pd;
    private Unbinder unbinder;

    @BindView(R.id.btnsubmit)
    Button btnsubmit;
    @BindView(R.id.btnclear)
    Button btnclear;

    @BindView(R.id.edt_licencekey)
    EditText edt_licencekey;
    @BindView(R.id.edt_op_name)
    EditText edt_op_name;
    @BindView(R.id.edt_op_contactno)
    EditText edt_op_contactno;
    @BindView(R.id.edt_cab_name)
    EditText edt_cab_name;
    @BindView(R.id.edt_op_add)
    EditText edt_op_add;
    @BindView(R.id.edt_web_link)
    EditText edt_web_link;
    @BindView(R.id.edt_web_loginpass)
    EditText edt_web_loginpass;
    @BindView(R.id.edt_master_pass)
    EditText edt_master_pass;
    @BindView(R.id.edt_web_loginid)
    EditText edt_web_loginid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.addlicence);
        unbinder = ButterKnife.bind(this);
        setTitle(getString(R.string.adlice).toUpperCase());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearData();
            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
                    if (NetworkConnection.isNetworkAvailable(AddLicence.this)) {
                        try {
                            showProgressDialog();
                            ApiService api = RetroClient.getApiService();
                            Call<String> call = api.add_installation_history("add_customer", edt_cab_name.getText().toString(), edt_op_add.getText().toString(), edt_op_contactno.getText().toString(), edt_web_link.getText().toString(), edt_web_loginid.getText().toString(), edt_web_loginpass.getText().toString(), edt_master_pass.getText().toString(), edt_licencekey.getText().toString());
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
                                    Utils.ShowMessageDialog(AddLicence.this, "Error Occurred");
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Utils.ShowMessageDialog(AddLicence.this, "No Connection Available");
                    }
                }
            }
        });
    }

    public void ClearData() {
        edt_cab_name.setText("");
        edt_licencekey.setText("");
        edt_master_pass.setText("");
        edt_op_add.setText("");
        edt_op_contactno.setText("");
        edt_op_name.setText("");
        edt_web_loginpass.setText("");
        edt_web_loginid.setText("");
        edt_web_link.setText("");
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
                            ClearData();
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
            Toast.makeText(AddLicence.this, "Customer added successfully ", Toast.LENGTH_LONG).show();
        } else {
            Utils.ShowMessageDialog(AddLicence.this, message);
        }
    }

    public void showProgressDialog() {
        pd = new ProgressDialog(AddLicence.this);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public boolean checkValidation() {
        if (TextUtils.isEmpty(edt_cab_name.getText().toString().trim())) {
            edt_cab_name.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(edt_licencekey.getText().toString().trim())) {
            edt_licencekey.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(edt_master_pass.getText().toString().trim())) {
            edt_master_pass.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(edt_op_add.getText().toString().trim())) {
            edt_op_add.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(edt_op_contactno.getText().toString().trim())) {
            edt_op_contactno.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(edt_op_name.getText().toString().trim())) {
            edt_op_name.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(edt_web_loginpass.getText().toString().trim())) {
            edt_web_loginpass.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(edt_web_loginid.getText().toString().trim())) {
            edt_web_loginid.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(edt_web_link.getText().toString().trim())) {
            edt_web_link.setError("Please fill data");
            return false;
        }

        return true;
    }

}