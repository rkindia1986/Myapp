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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sjm.myapp.pojo.Installation_History;

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

public class Setting extends AppCompatActivity {
    private static final String TAG = "Setting";
    ProgressDialog pd;
    private Unbinder unbinder;

    @BindView(R.id.btn_masterpass)
    Button btn_masterpass;
    @BindView(R.id.btn_setlink)
    Button btn_setlink;

    @BindView(R.id.edt_set_wlink)
    EditText edt_set_wlink;
    @BindView(R.id.edt_set_masterpass)
    EditText edt_set_masterpass;
    @BindView(R.id.edt_set_masterpass2)
    EditText edt_set_masterpass2;
    @BindView(R.id.edt_oldmasterpass)
    EditText edt_oldmasterpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setting);
        unbinder = ButterKnife.bind(this);
        setTitle(getString(R.string.action_settings).toUpperCase());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btn_masterpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
                    if (NetworkConnection.isNetworkAvailable(Setting.this)) {
                        try {
                            showProgressDialog();
                            ApiService api = RetroClient.getApiService();
                            Call<String> call = api.change_master_password("change_master_password", Application.preferences.getverify_licence_key(), Application.preferences.getDeviceId(), edt_oldmasterpass.getText().toString(), edt_set_masterpass.getText().toString(), edt_set_masterpass2.getText().toString());
                            Log.e(TAG, "call getDetailsByQr: " + call.request().url().toString());

                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {

                                    Log.e(TAG, "onResponse getDetailsByQr: " + response.body());
                                    hideProgressDialog();
                                    parseResponse(response.body());
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    hideProgressDialog();
                                    Log.e(TAG, "onFailure getDetailsByQr: " + t.getMessage());
                                    Utils.ShowMessageDialog(Setting.this, "Error Occurred");
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Utils.ShowMessageDialog(Setting.this, "No Connection Available");
                    }
                }
            }
        });
        btn_setlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation2()) {
                    if (NetworkConnection.isNetworkAvailable(Setting.this)) {
                        try {
                            showProgressDialog();
                            ApiService api = RetroClient.getApiService();
                            Call<String> call = api.update_website_link("update_website_link", Application.preferences.getverify_licence_key(), Application.preferences.getDeviceId(), edt_set_wlink.getText().toString());
                            Log.e(TAG, "call getDetailsByQr: " + call.request().url().toString());

                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {

                                    Log.e(TAG, "onResponse getDetailsByQr: " + response.body());
                                    hideProgressDialog();
                                    parseResponse2(response.body());
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    hideProgressDialog();
                                    Log.e(TAG, "onFailure getDetailsByQr: " + t.getMessage());
                                    Utils.ShowMessageDialog(Setting.this, "Error Occurred");
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Utils.ShowMessageDialog(Setting.this, "No Connection Available");
                    }
                }
            }
        });
    }

    private void parseResponse(String body) {
        String message = "Error Occurred";
        boolean Status = false;
        try {
            JSONObject j = new JSONObject(body);
            if (j != null) {
                if (body.contains("message")) {


                    message = j.optString("message");

                }
                if (j.getInt("status") == 0) {
                    Application.preferences.setMASTERPASS(edt_set_masterpass.getText().toString());
                    JSONObject jj = new JSONObject(Application.preferences.getDetails());
                    jj.put("master_password", edt_set_masterpass.getText().toString());
                    Application.preferences.setDetails(jj.toString());

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

        Utils.ShowMessageDialog(Setting.this, message);

    }
    private void parseResponse2(String body) {
        String message = "Error Occurred";
        boolean Status = false;
        try {
            JSONObject j = new JSONObject(body);
            if (j != null) {
                if (body.contains("message")) {


                    message = j.optString("message");

                }
                if (j.getInt("status") == 0) {
                    JSONObject jj = new JSONObject(Application.preferences.getDetails());
                    jj.put("website_link", edt_set_wlink.getText().toString());
                    Application.preferences.setDetails(jj.toString());

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

        Utils.ShowMessageDialog(Setting.this, message);

    }
    public void showProgressDialog() {
        pd = new ProgressDialog(Setting.this);
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
        if (TextUtils.isEmpty(edt_oldmasterpass.getText().toString().trim())) {
            edt_oldmasterpass.setError("Please fill data");
            return false;
        }

        if (TextUtils.isEmpty(edt_set_masterpass.getText().toString().trim())) {
            edt_set_masterpass.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(edt_set_masterpass2.getText().toString().trim())) {
            edt_set_masterpass2.setError("Please fill data");
            return false;
        }
        if (edt_oldmasterpass.getText().toString().trim().length() < 4) {
            edt_oldmasterpass.setError("Master password length atleast 4 digit");

            return false;
        }


        if (edt_set_masterpass.getText().toString().trim().length() < 4) {
            edt_set_masterpass.setError("Master password length atleast 4 digit");

            return false;
        }

        if (edt_set_masterpass2.getText().toString().trim().length() < 4) {
            edt_set_masterpass2.setError("Master password length atleast 4 digit");
            return false;
        }
        if (!edt_set_masterpass.getText().toString().trim().equals(edt_set_masterpass2.getText().toString().trim())) {
            edt_set_masterpass2.setError("Password does not match");

            return false;
        }


        return true;
    }

    public boolean checkValidation2() {
        if (TextUtils.isEmpty(edt_set_wlink.getText().toString().trim())) {
            edt_set_wlink.setError("Please fill data");
            return false;
        }

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
