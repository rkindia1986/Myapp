package com.sjm.myapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

import org.json.JSONArray;
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

public class EditLicence extends AppCompatActivity {
    private static final String TAG = "EditLicence";
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

    @BindView(R.id.edt_master_pass1)
    EditText edt_master_pass;

    @BindView(R.id.edt_master_pass2)
    EditText edt_master_pass2;
    Installation_History installation_history;
    boolean nodatafound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.editlicence);
        unbinder = ButterKnife.bind(this);
        setTitle(getString(R.string.edtlice).toUpperCase());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Gson gson = new GsonBuilder().create();
        GetHistory();


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
                    if (NetworkConnection.isNetworkAvailable(EditLicence.this)) {
                        try {
                            showProgressDialog();
                            ApiService api = RetroClient.getApiService();
                            Call<String> call = api.update_installation_history("update_installation_history", edt_op_name.getText().toString(), edt_op_contactno.getText().toString(), edt_cab_name.getText().toString(), edt_op_add.getText().toString(), "","","", edt_master_pass.getText().toString(), edt_licencekey.getText().toString(), Application.preferences.getDeviceId());
                            Log.e(TAG, "call getDetailsByQr: " + call.request().url().toString());

                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {

                                    Log.e(TAG, "onResponse getDetailsByQr: " + response.body());
                                    parseResponse(response.body());
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    hideProgressDialog();
                                    Log.e(TAG, "onFailure getDetailsByQr: " + t.getMessage());
                                    Utils.ShowMessageDialog(EditLicence.this, "Error Occurred");
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Utils.ShowMessageDialog(EditLicence.this, "No Connection Available");
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

        edt_master_pass2.setText("");
    }

    private void parseResponse(String body) {
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
                        // Gson gson = new GsonBuilder().create();
                        //Installation_History installation_history = gson.fromJson(body, Installation_History.class);
                        if (message.contains("Installation Updated Successfully.")) {
                            //Application.preferences.setLICENCEKEY(installation_history.getLicence_key());
                            Application.preferences.setUSerid(installation_history.getUser_id());
                            Application.preferences.setMASTERPASS(edt_master_pass.getText().toString());
                            Application.preferences.setverify_licence_key("1");
                            GetHistory2();
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


        } else {
            hideProgressDialog();
            Utils.ShowMessageDialog(EditLicence.this, message);
        }
    }

    public void showProgressDialog() {
        pd = new ProgressDialog(EditLicence.this);
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

        if (TextUtils.isEmpty(edt_licencekey.getText().toString().trim())) {
            edt_licencekey.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(edt_op_name.getText().toString().trim())) {
            edt_op_name.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(edt_op_contactno.getText().toString().trim())) {
            edt_op_contactno.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(edt_cab_name.getText().toString().trim())) {
            edt_cab_name.setError("Please fill data");
            return false;
        }

        if (TextUtils.isEmpty(edt_op_add.getText().toString().trim())) {
            edt_op_add.setError("Please fill data");
            return false;
        }



        if (TextUtils.isEmpty(edt_master_pass.getText().toString().trim())) {
            edt_master_pass.setError("Please fill data");
            return false;
        }
        if (TextUtils.isEmpty(edt_master_pass2.getText().toString().trim())) {
            edt_master_pass2.setError("Please fill data");
            return false;
        }


        if (edt_master_pass.getText().toString().trim().length() < 4) {
            edt_master_pass.setError("Master password length atleast 4 digit");

            return false;
        }

        if (edt_master_pass2.getText().toString().trim().length() < 4) {
            edt_master_pass2.setError("Master password length atleast 4 digit");
            return false;
        }
        if (!edt_master_pass.getText().toString().trim().equals(edt_master_pass2.getText().toString().trim())) {
            edt_master_pass2.setError("Password does not match");

            return false;
        }


        return true;
    }

    public void GetHistory() {
        if (NetworkConnection.isNetworkAvailable(EditLicence.this)) {
            try {
                ApiService api = RetroClient.getApiService();
                Call<String> call = api.get_installation_history("get_installation_history", Application.preferences.getLICENCEKEY());
                Log.e(TAG, "call getDetailsByQr: " + call.request().url().toString());

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        Log.e(TAG, "onResponse getDetailsByQr: " + response.body());
                        hideProgressDialog();
                        parseResponses(response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        hideProgressDialog();
                        if (Application.preferences.getDetails().length() > 10) {

                            Gson gson = new GsonBuilder().create();
                            installation_history = gson.fromJson(Application.preferences.getDetails(), Installation_History.class);
                            if (installation_history != null) {
                                edt_cab_name.setText(installation_history.getCable_network_name());
                                edt_licencekey.setText(installation_history.getLicence_key());
                                edt_master_pass.setText(installation_history.getMaster_password());
                                edt_master_pass2.setText(installation_history.getMaster_password());
                                edt_op_add.setText(installation_history.getOperator_address());
                                edt_op_contactno.setText(installation_history.getCable_operator_contact_no());
                                edt_op_name.setText(installation_history.getCable_operator_name());

                            } else {

                                Log.e(TAG, "onFailure getDetailsByQr: " + t.getMessage());
                                Utils.ShowMessageDialog(EditLicence.this, "Error Occurred");

                            }
                        } else {
                            Log.e(TAG, "onFailure getDetailsByQr: " + t.getMessage());
                            Utils.ShowMessageDialog(EditLicence.this, "Error Occurred");
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (Application.preferences.getDetails().length() > 10) {

                Gson gson = new GsonBuilder().create();
                installation_history = gson.fromJson(Application.preferences.getDetails(), Installation_History.class);
                if (installation_history != null) {
                    edt_cab_name.setText(installation_history.getCable_network_name());
                    edt_licencekey.setText(installation_history.getLicence_key());
                    edt_master_pass.setText(installation_history.getMaster_password());
                    edt_master_pass2.setText(installation_history.getMaster_password());
                    edt_op_add.setText(installation_history.getOperator_address());
                    edt_op_contactno.setText(installation_history.getCable_operator_contact_no());
                    edt_op_name.setText(installation_history.getCable_operator_name());

                }

            } else {
                Utils.ShowMessageDialog(EditLicence.this, "No Connection Available");
            }
        }
    }

    public void GetHistory2() {
        if (NetworkConnection.isNetworkAvailable(EditLicence.this)) {
            try {
                ApiService api = RetroClient.getApiService();
                Call<String> call = api.get_installation_history("get_installation_history", Application.preferences.getLICENCEKEY());
                Log.e(TAG, "call getDetailsByQr: " + call.request().url().toString());

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        Log.e(TAG, "onResponse getDetailsByQr: " + response.body());
                        hideProgressDialog();
                        parseResponses2(response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        hideProgressDialog();

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void parseResponses(String body) {
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
                        JSONArray jsonArray = j.optJSONArray("result");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            Gson gson = new GsonBuilder().create();
                            installation_history = gson.fromJson(jsonArray.getJSONObject(0).toString(), Installation_History.class);
                            if (installation_history != null && installation_history.getLicence_key() != null && installation_history.getLicence_key().length() > 5 && installation_history.getVerify_licence_key().equalsIgnoreCase("1")) {
                                Application.preferences.setLICENCEKEY(installation_history.getLicence_key());
                                Application.preferences.setUSerid(installation_history.getUser_id());
                                Application.preferences.setMASTERPASS(installation_history.getMaster_password());
                                Application.preferences.setverify_licence_key(installation_history.getLicence_key());
                                Application.preferences.setDetails(jsonArray.getJSONObject(0).toString());
                            }
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

            if (installation_history != null) {
                edt_cab_name.setText(installation_history.getCable_network_name());
                edt_licencekey.setText(installation_history.getLicence_key());
                edt_master_pass.setText(installation_history.getMaster_password());
                edt_master_pass2.setText(installation_history.getMaster_password());
                edt_op_add.setText(installation_history.getOperator_address());
                edt_op_contactno.setText(installation_history.getCable_operator_contact_no());
                edt_op_name.setText(installation_history.getCable_operator_name());

            }


        } else {
            Utils.ShowMessageDialog(EditLicence.this, message);
        }
    }

    private void parseResponses2(String body) {
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
                        JSONArray jsonArray = j.optJSONArray("result");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            Gson gson = new GsonBuilder().create();
                            installation_history = gson.fromJson(jsonArray.getJSONObject(0).toString(), Installation_History.class);
                            if (installation_history != null && installation_history.getLicence_key() != null && installation_history.getLicence_key().length() > 5 && installation_history.getVerify_licence_key().equalsIgnoreCase("1")) {
                                Application.preferences.setLICENCEKEY(installation_history.getLicence_key());
                                Application.preferences.setUSerid(installation_history.getUser_id());
                                Application.preferences.setMASTERPASS(installation_history.getMaster_password());
                                Application.preferences.setverify_licence_key(installation_history.getLicence_key());
                                Application.preferences.setDetails(jsonArray.getJSONObject(0).toString());
                            }
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
            SHowDialog( "Record Updated Successfully");

        } else {
            Utils.ShowMessageDialog(EditLicence.this, message);
        }
    }
    public void SHowDialog(String msg)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditLicence.this);
        alertDialog.setTitle(R.string.app_name);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(EditLicence.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

        alertDialog.show();
    }

}
