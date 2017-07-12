package com.sjm.myapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sjm.myapp.Fragment.Search_Fragment;
import com.sjm.myapp.pojo.Customer;
import com.sjm.myapp.pojo.PaymentRecordsList;
import com.sjm.myapp.pojo.PlanAlert;
import com.sjm.myapp.pojo.RentRecordsList;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sjm.myapp.Fragment.Search_Fragment.categoryListModel;

/**
 * Created by Helly-PC on 06/01/2017.
 */

public class ViewDtails extends AppCompatActivity {
    private static final String TAG = "Backup";
    ProgressDialog pd;
    private Unbinder unbinder;
    int selectedId = 0;
    PlanAlert planAlert;
    @BindView(R.id.txt_cno)
    TextView txt_cno;
    @BindView(R.id.txt_stbac)
    TextView txt_stbac;
    @BindView(R.id.txt_nuid)
    TextView txt_nuid;
    @BindView(R.id.txt_cafno)
    TextView txt_cafno;
    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.txt_add)
    TextView txt_add;
    @BindView(R.id.txt_city)
    TextView txt_city;
    @BindView(R.id.txt_phone)
    TextView txt_phone;
    @BindView(R.id.txt_startdate)
    TextView txt_startdate;
    @BindView(R.id.txt_enddate)
    TextView txt_enddate;
    @BindView(R.id.txt_amount)
    TextView txt_amount;
    @BindView(R.id.txt_rent)
    TextView txt_rent;
    @BindView(R.id.txt_connstatus)
    TextView txt_connstatus;
    @BindView(R.id.edtsms)
    EditText edtsms;

    @BindView(R.id.btn_change)
    Button btn_change;
    @BindView(R.id.btn_addpayment)
    Button btn_addpayment;
    @BindView(R.id.btn_rentrecord)
    Button btn_rentrecord;
    @BindView(R.id.btn_payrecord)
    Button btn_payrecord;
    @BindView(R.id.btn_deleteclient)
    Button btn_deleteclient;
    @BindView(R.id.btn_call)
    Button btn_call;
    @BindView(R.id.btn_SMS)
    Button btn_SMS;
    @BindView(R.id.btn_changeplan)
    Button btn_changeplan;
    @BindView(R.id.btn_updaterent)
    Button btn_updaterent;

    @BindView(R.id.edt_addpayment)
    EditText edt_addpayment;
    @BindView(R.id.edt_month)
    EditText edt_month;
    @BindView(R.id.edt_newrent)
    EditText edt_newrent;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.lyt_rentplanchange)
    LinearLayout lyt_rentplanchange;
    Customer customer;
    String UpdatedStatus = "";
    public static PaymentRecordsList paymentRecordsList;
    public static RentRecordsList rentRecordsList;

    @BindView(R.id.datePicker)
    DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.viewdetails);
        unbinder = ButterKnife.bind(this);
        if (getIntent().getExtras() != null) {
            selectedId = getIntent().getExtras().getInt("selected");
        }
        if (selectedId != 0) {

        }
        setTitle("VIEW DETAILS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        btn_changeplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lyt_rentplanchange.getVisibility() == View.VISIBLE) {
                    lyt_rentplanchange.setVisibility(View.GONE);
                } else {
                    lyt_rentplanchange.setVisibility(View.VISIBLE);
                }
            }
        });
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customer.getPhone() != null && customer.getPhone().length() >= 5) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + customer.getPhone()));
                    if (ActivityCompat.checkSelfPermission(ViewDtails.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(intent);
                }
            }
        });
        btn_updaterent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation2()) {
                    if (NetworkConnection.isNetworkAvailable(ViewDtails.this)) {
                        try {

                            String m = "";
                            if ((datePicker.getMonth() + 1) < 10) {
                                m = "0" + (datePicker.getMonth() + 1);
                            } else {
                                m = (datePicker.getMonth() + 1) + "";
                            }
                            String d = "";
                            if (datePicker.getDayOfMonth() < 10) {
                                d = "0" + datePicker.getDayOfMonth();
                            } else {
                                d = datePicker.getDayOfMonth() + "";
                            }
                            String date = datePicker.getYear() + "-" + m + "-" + d;

                            // String date=datePicker.getYear() +"-" + datePicker.getMonth() + "-" +datePicker.getDayOfMonth();
                            showProgressDialog();
                            Log.e("date", date);
                            ApiService api = RetroClient.getApiService();

                            Call<String> call = api.Edit_customer("edit_customer", customer.getName(), customer.getCustomer_no(), customer.getAddress(), customer.getCity(), customer.getAmount(), customer.getPhone(), edt_newrent.getText().toString(), customer.getStb_account_no_1(), customer.getNu_id_no_1(), customer.getCaf_no_1(), customer.getStb_account_no_2(), customer.getNu_id_no_2(), customer.getCaf_no_2(), customer.getConnection_type(), customer.getCustomer_connection_status(), customer.getRent_start_date(), edt_month.getText().toString(), "4");
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Log.e(TAG, "call getDetailsByQr: " + call.toString());

                                    Log.e(TAG, "onResponse getDetailsByQr: " + response.body());
                                    hideProgressDialog();
                                    parseUPDATEResponse(response.body());
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    hideProgressDialog();
                                    Log.e(TAG, "onFailure getDetailsByQr: " + t.getMessage());
                                    Utils.ShowMessageDialog(ViewDtails.this, "Error Occurred");
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Utils.ShowMessageDialog(ViewDtails.this, "No Connection Available");
                    }

                }
            }
        });
        btn_addpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
                    if (NetworkConnection.isNetworkAvailable(ViewDtails.this)) {
                        try {
                            showProgressDialog();
                            ApiService api = RetroClient.getApiService();

                            Call<String> call = api.add_customer_payment_amount("add_customer_payment_amount", customer.getId(), "4", edt_addpayment.getText().toString());
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Log.e(TAG, "call getDetailsByQr: " + call.toString());

                                    Log.e(TAG, "onResponse getDetailsByQr: " + response.body());
                                    hideProgressDialog();
                                    parseADDPAYMENTResponse(response.body());
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    hideProgressDialog();
                                    Log.e(TAG, "onFailure getDetailsByQr: " + t.getMessage());
                                    Utils.ShowMessageDialog(ViewDtails.this, "Error Occurred");
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Utils.ShowMessageDialog(ViewDtails.this, "No Connection Available");
                    }
                }
            }
        });
        btn_SMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customer.getPhone() != null && customer.getPhone().length() >= 5) {
                    String number = customer.getPhone();  // The number on which you want to send SMS
                    // The number on which you want to send SMS
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null));
                    sendIntent.putExtra("sms_body", "Enter SMS Text ");
                    startActivity(sendIntent);

                }
            }
        });
        btn_payrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetPaymentRecord();
            }
        });
        btn_rentrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetRentRecord();
            }
        });
        customer = categoryListModel.getLstCustomer().get(selectedId);
        setdata();
        btn_deleteclient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowMessageDialog(2);
            }

        });
        btn_change.setOnClickListener(new View.OnClickListener()

                                      {
                                          @Override
                                          public void onClick(View view) {
                                              ShowMessageDialog(1);
                                          }
                                      }

        );

        Spinner spinnerCountShoes = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerCountShoesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.paydetails));
        spinnerCountShoes.setAdapter(spinnerCountShoesArrayAdapter);
        spinnerCountShoes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SetMessage(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public void ShowMessageDialog(final int kk) {
        final EditText input = new EditText(ViewDtails.this);
        input.setHint("Enter Passcode");
        input.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setSelection(input.getText().length());
        input.setSingleLine();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());


        final AlertDialog dialog = new AlertDialog.Builder(ViewDtails.this)
                .setView(input)
                .setTitle(R.string.sec_alert)
                .setMessage(getString(R.string.entercode))
                .setPositiveButton("OK", null) //Set to null. We override the onclick
                .setNegativeButton("CANCEL", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                Button buttonOK = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                Button buttonCancel = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                buttonCancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                buttonOK.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(input.getText())) {
                            input.setError("Enter Passcode");

                        } else if (input.getText().toString().trim().equalsIgnoreCase(Application.preferences.getMASTERPASS())) {
                            dialog.dismiss();
                            if (kk == 1) {
                                UpdateConnection();
                            } else {
                                DeletCustomer();
                            }
                        } else {
                            input.setError("Invalid Passcode");

                        }
                    }
                });
            }
        });
        dialog.show();

    }

    public void UpdateConnection() {
        if (NetworkConnection.isNetworkAvailable(ViewDtails.this)) {
            try {
                showProgressDialog();
                ApiService api = RetroClient.getApiService();
                if (customer.getCustomer_connection_status().equalsIgnoreCase("on")) {
                    UpdatedStatus = "off";
                } else {
                    UpdatedStatus = "on";
                }
                Call<String> call = api.update_customer_connection_status("update_customer_connection_status", customer.getId(), "4", UpdatedStatus);
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
                        Utils.ShowMessageDialog(ViewDtails.this, "Error Occurred");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Utils.ShowMessageDialog(ViewDtails.this, "No Connection Available");
        }

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
                            customer.setCustomer_connection_status(UpdatedStatus);
                            txt_connstatus.setText(UpdatedStatus);
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
            Toast.makeText(ViewDtails.this, message, Toast.LENGTH_SHORT).show();
        } else {
            Utils.ShowMessageDialog(ViewDtails.this, message);
        }
    }

    private void parseADDPAYMENTResponse(String body) {
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
                            Gson gson = new GsonBuilder().create();
                            customer = gson.fromJson(j.optJSONObject("result").toString(), Customer.class);
                            if (customer != null)
                                txt_amount.setText(customer.getAmount());
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
            Toast.makeText(ViewDtails.this, message, Toast.LENGTH_SHORT).show();
        } else {
            Utils.ShowMessageDialog(ViewDtails.this, message);
        }
    }

    private void parseUPDATEResponse(String body) {
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
                        customer = gson.fromJson(j.optJSONObject("result").toString(), Customer.class);

                        setdata();
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
            Toast.makeText(ViewDtails.this, message, Toast.LENGTH_SHORT).show();
        } else {
            Utils.ShowMessageDialog(ViewDtails.this, message);
        }
    }

    private void parseDELETEResponse(String body) {
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
                        Log.e(".getLstCustomer()", Search_Fragment.categoryListModel.getLstCustomer().size() + "");
                        Search_Fragment.categoryListModel.getLstCustomer().remove(selectedId);
                        Log.e("AFTER.getLstCustomer()", Search_Fragment.categoryListModel.getLstCustomer().size() + "");

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
            Toast.makeText(ViewDtails.this, message, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Utils.ShowMessageDialog(ViewDtails.this, message);
        }
    }

    public void GetPaymentRecord() {
        if (NetworkConnection.isNetworkAvailable(ViewDtails.this)) {
            try {
                showProgressDialog();
                ApiService api = RetroClient.getApiService();

                Call<String> call = api.payment_record("payment_record", "6"); //customer.getId());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.e(TAG, "call GetPaymentRecord: " + customer.getId());

                        Log.e(TAG, "onResponse GetPaymentRecord: " + response.body());
                        hideProgressDialog();
                        parsePaymentResponse(response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        hideProgressDialog();
                        Log.e(TAG, "onFailure GetPaymentRecord: " + t.getMessage());
                        Utils.ShowMessageDialog(ViewDtails.this, "Error Occurred");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Utils.ShowMessageDialog(ViewDtails.this, "No Connection Available");
        }

    }

    private void parsePaymentResponse(String body) {

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
                            Gson gson = new GsonBuilder().create();
                            paymentRecordsList = gson.fromJson(body, PaymentRecordsList.class);
                            if (paymentRecordsList != null && paymentRecordsList.getLstPaymentrecords().size() > 0) {
                                startActivity(new Intent(ViewDtails.this, PaymentList.class));
                            } else {
                                message = "No Records";
                                Status = false;
                            }
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

        } else {
            Utils.ShowMessageDialog(ViewDtails.this, message);
        }


    }

    public void GetRentRecord() {
        if (NetworkConnection.isNetworkAvailable(ViewDtails.this)) {
            try {
                showProgressDialog();
                ApiService api = RetroClient.getApiService();

                Call<String> call = api.rent_record_by_customer_id("rent_record_by_customer_id", customer.getId(), "4"); //customer.getId());

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.e(TAG, "call GetRentRecord: " + customer.getId());

                        Log.e(TAG, "onResponse GetRentRecord: " + response.body());
                        hideProgressDialog();
                        parseRentResponse(response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        hideProgressDialog();
                        Log.e(TAG, "onFailure GetRentRecord: " + t.getMessage());
                        Utils.ShowMessageDialog(ViewDtails.this, "Error Occurred");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Utils.ShowMessageDialog(ViewDtails.this, "No Connection Available");
        }

    }

    private void parseRentResponse(String body) {

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
                            Gson gson = new GsonBuilder().create();
                            rentRecordsList = gson.fromJson(body, RentRecordsList.class);
                            if (rentRecordsList != null && rentRecordsList.getLstrentrecord().size() > 0) {
                                startActivity(new Intent(ViewDtails.this, RentList.class));
                            } else {
                                message = "No Records";
                                Status = false;
                            }
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

        } else {
            Utils.ShowMessageDialog(ViewDtails.this, message);
        }


    }

    public void showProgressDialog() {
        pd = new ProgressDialog(ViewDtails.this);
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
        if (TextUtils.isEmpty(edt_addpayment.getText().toString().trim())) {
            edt_addpayment.setError("Please enter data");
            return false;
        }
        return true;
    }

    public boolean checkValidation2() {
        if (TextUtils.isEmpty(edt_month.getText().toString().trim())) {
            edt_month.setError("Please enter month");
            return false;
        }
        if (TextUtils.isEmpty(edt_newrent.getText().toString().trim())) {
            edt_newrent.setError("Please enter rent");
            return false;
        }
        return true;
    }

    public void setdata() {
        if (customer != null) {
            txt_add.setText(customer.getAddress());
            txt_amount.setText(customer.getAmount());
            txt_cafno.setText(customer.getCaf_no_1());
            txt_city.setText(customer.getCity());
            txt_cno.setText(customer.getCustomer_no());
            txt_connstatus.setText(customer.getCustomer_connection_status());
            txt_enddate.setText("");
            txt_name.setText(customer.getName());
            txt_nuid.setText(customer.getNu_id_no_1());
            txt_phone.setText(customer.getPhone());
            txt_amount.setText(customer.getAmount());
            txt_rent.setText(customer.getRent_amount());
            txt_startdate.setText(customer.getRent_start_date());
            txt_stbac.setText(customer.getStb_account_no_1());

            if (!TextUtils.isEmpty(customer.getNo_of_month())) {

                Calendar calendar = Calendar.getInstance();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                try {
                    Date myDate = simpleDateFormat.parse(customer.getRent_start_date());
                    if (myDate != null) {
                        calendar.setTime(myDate);
                        calendar.add(Calendar.MONTH, Integer.parseInt(customer.getNo_of_month()));
                        Date date = calendar.getTime();
                        txt_enddate.setText(simpleDateFormat.format(date));

                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            getPlanAlert();
        } else {

        }
        lyt_rentplanchange.setVisibility(View.GONE);
    }

    public void DeletCustomer() {
        if (NetworkConnection.isNetworkAvailable(ViewDtails.this)) {
            try {
                showProgressDialog();
                ApiService api = RetroClient.getApiService();

                Call<String> call = api.DeleteCustomer("delete_customer", customer.getId());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.e(TAG, "call getDetailsByQr: " + call.toString());

                        Log.e(TAG, "onResponse getDetailsByQr: " + response.body());
                        hideProgressDialog();
                        parseDELETEResponse(response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        hideProgressDialog();
                        Log.e(TAG, "onFailure getDetailsByQr: " + t.getMessage());
                        Utils.ShowMessageDialog(ViewDtails.this, "Error Occurred");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Utils.ShowMessageDialog(ViewDtails.this, "No Connection Available");
        }
    }

    public void getPlanAlert() {
        if (NetworkConnection.isNetworkAvailable(ViewDtails.this)) {
            try {


                // String date=datePicker.getYear() +"-" + datePicker.getMonth() + "-" +datePicker.getDayOfMonth();
                showProgressDialog();
                ApiService api = RetroClient.getApiService();

                Call<String> call = api.send_sms_by_customer("send_sms_by_customer", "device_id23HiWKh0qQ", customer.getId(), "plan_alert");
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.e(TAG, "call getDetailsByQr: " + call.toString());

                        Log.e(TAG, "onResponse getDetailsByQr: " + response.body());
                        hideProgressDialog();
                        parseResponsePlan(response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        hideProgressDialog();
                        Log.e(TAG, "onFailure getDetailsByQr: " + t.getMessage());
                        Utils.ShowMessageDialog(ViewDtails.this, "Error Occurred");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Utils.ShowMessageDialog(ViewDtails.this, "No Connection Available");
        }
    }

    private void parseResponsePlan(String body) {
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

                        Gson gson = new GsonBuilder().create();
                        planAlert = gson.fromJson(j.optJSONObject("result").toString(), PlanAlert.class);
                        SetMessage(spinner.getSelectedItemPosition());
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

    }

    public void SetMessage(int kk) {
        if (planAlert != null && planAlert.getCustomer() != null && planAlert.getInstallation_history() != null) {

            if (kk == 0) {
                edtsms.setText("Selected ==0" + planAlert.getInstallation_history().getCable_network_name() + ":" + planAlert.getCustomer().getCustomer_no() + ":" + planAlert.getCustomer().getName());
            } else if (kk == 1) {
                edtsms.setText("Selected ==1" + planAlert.getInstallation_history().getCable_network_name() + ":" + planAlert.getCustomer().getCustomer_no() + ":" + planAlert.getCustomer().getName());
            } else if (kk == 2) {
                edtsms.setText("Selected ==2" + planAlert.getInstallation_history().getCable_network_name() + ":" + planAlert.getCustomer().getCustomer_no() + ":" + planAlert.getCustomer().getName());
            }
        }
    }

}
