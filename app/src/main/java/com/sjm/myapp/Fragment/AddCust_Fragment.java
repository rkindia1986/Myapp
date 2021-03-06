package com.sjm.myapp.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.SystemClock;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sjm.myapp.Application;
import com.sjm.myapp.R;
import com.sjm.myapp.SqlLiteDbHelper;
import com.sjm.myapp.Utils;
import com.sjm.myapp.pojo.Customer;
import com.sjm.myapp.pojo.PaymentRecord;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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

    @BindView(R.id.rdo_continus)
    RadioButton rdo_continus;
    @BindView(R.id.rdo_plan)
    RadioButton rdo_plan;
    @BindView(R.id.btn_addcust)
    Button btn_addcust;

    @BindView(R.id.edt_month)
    EditText edt_month;
    String conn_type = "";
    String conn_status = "on";
    @BindView(R.id.lyt_rentplanchange)
    LinearLayout lyt_rentplanchange;
    ArrayList<String> s = new ArrayList<String>();
    @BindView(R.id.datePicker)
    DatePicker datePicker;
    SqlLiteDbHelper sqlLiteDbHelper;
    String sdate, created_at, updatedat;
    JSONObject jsonObject;
    private int myear, mmonth, mday, mhour, mmin, msec;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_customer, container, false);
        unbinder = ButterKnife.bind(this, view);
        sqlLiteDbHelper = new SqlLiteDbHelper(getActivity());
        sqlLiteDbHelper.openDataBase();
        edt_month.setEnabled(false);
        btn_addcust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
                    sdate = Utils.getDate(datePicker.getDayOfMonth(), (datePicker.getMonth() + 1), datePicker.getYear());
                    String enddate = "";
                    int selectedId = rdogroup.getCheckedRadioButtonId();

                    if (selectedId == R.id.rdo_continus) {
                        conn_type = "continuous";
                    } else if (selectedId == R.id.rdo_plan) {
                        conn_type = "plan";
                        if (!TextUtils.isEmpty(edt_month.getText().toString())) {

                            Calendar calendar = Calendar.getInstance();

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            try {
                                Date myDate = simpleDateFormat.parse(sdate);
                                if (myDate != null) {
                                    calendar.setTime(myDate);
                                    calendar.add(Calendar.MONTH, Integer.parseInt(edt_month.getText().toString()));
                                    myDate = calendar.getTime();
                                    enddate = simpleDateFormat.format(myDate);

                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    Calendar c = Calendar.getInstance();
                    mday = c.get(Calendar.DAY_OF_MONTH);
                    mmonth = c.get(Calendar.MONTH);
                    myear = c.get(Calendar.YEAR);
                    mhour = c.get(Calendar.HOUR_OF_DAY) + 1;
                    mmin = c.get(Calendar.MINUTE);
                    msec = c.get(Calendar.SECOND);

                    created_at = Utils.getDatetime(myear, mmonth, mday, mhour, mmin, msec);
                    updatedat = Utils.getDatetime(myear, mmonth, mday, mhour, mmin, msec);
                    Log.e("sdate", sdate);


                    jsonObject = new JSONObject();
                    try {
                        String customid = "temp" + SystemClock.currentThreadTimeMillis();
                        jsonObject.put("sync", "0");
                        jsonObject.put("id", customid);
                        jsonObject.put("syncid", customid);
                        jsonObject.put("customer_no", cust_no.getText().toString());
                        jsonObject.put("name", cust_name.getText().toString());
                        jsonObject.put("address", cust_add.getText().toString());
                        jsonObject.put("city", cust_city.getText().toString());
                        jsonObject.put("amount", amt.getText().toString());
                        jsonObject.put("amount2", amt.getText().toString());
                        jsonObject.put("phone", phone.getText().toString());
                        jsonObject.put("rent_amount", rent_amt.getText().toString());
                        jsonObject.put("stb_account_no_1", stb_acc_no.getText().toString());
                        jsonObject.put("nu_id_no_1", stb_nuid.getText().toString());
                        jsonObject.put("caf_no_1", cafno.getText().toString());
                        jsonObject.put("stb_account_no_2", stb_ac2.getText().toString());
                        jsonObject.put("nu_id_no_2", stb_nuid2.getText().toString());
                        jsonObject.put("caf_no_2", cafno2.getText().toString());
                        jsonObject.put("connection_type", conn_type);
                        jsonObject.put("customer_connection_status", conn_status);
                        jsonObject.put("rent_start_date", sdate);
                        jsonObject.put("rent_end_date", enddate);
                        jsonObject.put("created_at", created_at);
                        jsonObject.put("created_by", Application.preferences.getUSerid());
                        jsonObject.put("updated_at", updatedat);
                        jsonObject.put("updated_by", Application.preferences.getUSerid());
                        jsonObject.put("no_of_month", edt_month.getText().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                 /*   if (NetworkConnection.isNetworkAvailable(getContext())) {
                        try {
                            showProgressDialog();
                            ApiService api = RetroClient.getApiService();


                            Call<String> call = api.add_customer("add_customer", cust_name.getText().toString(), cust_no.getText().toString(), cust_add.getText().toString(), cust_city.getText().toString(), amt.getText().toString(), phone.getText().toString(), rent_amt.getText().toString(), stb_acc_no.getText().toString(), stb_nuid.getText().toString(), cafno.getText().toString(), stb_ac2.getText().toString(), stb_nuid2.getText().toString(), cafno2.getText().toString(), conn_type, conn_status, sdate, edt_month.getText().toString(), Application.preferences.getUSerid());
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
                                    Gson gson = new GsonBuilder().create();
                                    Customer customer = gson.fromJson(jsonObject.toString(), Customer.class);

                                    sqlLiteDbHelper.UpdateCustomer(customer);
                                    Utils.ShowMessageDialog(getContext(), "Saved Successfully");
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {*/
                    Gson gson = new GsonBuilder().create();
                    Customer customer = gson.fromJson(jsonObject.toString(), Customer.class);
                    if (sqlLiteDbHelper.checkCustomer(customer)) {
                        Utils.ShowMessageDialog(getContext(), "Customer Exist. Customer number is exist");
                    } else {

                        sqlLiteDbHelper.UpdateCustomer(customer);
                        sqlLiteDbHelper.InsertCity(cust_city.getText().toString());


                        PaymentRecord paymentRecord = new PaymentRecord();
                        paymentRecord.setSync("0");
                        paymentRecord.setPayment_amount(amt.getText().toString());
                        paymentRecord.setCreated_by(Application.preferences.getUSerid());
                        paymentRecord.setCustomer_id(customer.getId());
                        paymentRecord.setCreated_at(sdate);
                        paymentRecord.setId(customer.getId());
                        sqlLiteDbHelper.InsertPayment(paymentRecord);


                    /*    int amount = Integer.parseInt(customer.getAmount2());
                        int rentamount = Integer.parseInt(customer.getRent_amount());
                        sqlLiteDbHelper.InsertCity(cust_city.getText().toString());
                        if (!TextUtils.isEmpty(customer.getNo_of_month())) {

                            for (int i = 0; i < Integer.parseInt(customer.getNo_of_month()); i++) {
                                Calendar calendar = Calendar.getInstance();

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                try {
                                    Date myDate = simpleDateFormat.parse(customer.getRent_start_date());
                                    if (myDate != null) {
                                        calendar.setTime(myDate);
                                        calendar.add(Calendar.MONTH, 1);
                                        Date date = calendar.getTime();
                                        customer.setRent_end_date(simpleDateFormat.format(date));

                                        RentRecord rentRecord = new RentRecord();
                                        rentRecord.setSync("0");
                                        rentRecord.setCustomer_no(customer.getCustomer_no());
                                        rentRecord.setId(customer.getId());

                                        rentRecord.setRent_start_date(customer.getRent_start_date());
                                        rentRecord.setRent_end_date(customer.getRent_end_date());
                                        rentRecord.setPayment_amount(customer.getRent_amount());
                                        rentRecord.setCreated_by(Application.preferences.getUSerid());
                                        rentRecord.setCreated_at(created_at);
                                        rentRecord.setUpdated_at(updatedat);
                                        rentRecord.setUpdated_by(Application.preferences.getUSerid());

                                        if (rentamount <= amount) {
                                            rentRecord.setPayment_status("PAID");
                                            amount = amount - rentamount;

                                        } else {
                                            rentRecord.setPayment_status("DUE");
                                        }
                                        Log.e("i = " + i, gson.toJson(rentRecord).toString());
                                        Log.e("rent amt| amt",customer.getAmount() +" " + amount +" " + rentamount);
                                        rentRecord.setUpdated_by(Application.preferences.getUSerid());
                                        sqlLiteDbHelper.InsertRentRecord(rentRecord);
                                        customer.setRent_start_date(customer.getRent_end_date());
                                        sqlLiteDbHelper.UpdateAmount(customer.getCustomer_no(), customer.getAmount(), amount + "");

                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }

                        }
*/
                        Utils.ShowMessageDialog(getContext(), "Saved Successfully");
                        cleardata();
                    }
                    // }
                }
            }
        });

        rdogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = rdogroup.getCheckedRadioButtonId();
                if (selectedId == R.id.rdo_continus) {
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
                            Gson gson = new GsonBuilder().create();

                            JSONObject jsonObject = j.optJSONObject("result");
                            Customer customer = gson.fromJson(jsonObject.toString(), Customer.class);
                            customer.setSync("1");
                            customer.setSyncid(customer.getId());
                            sqlLiteDbHelper.UpdateCustomer(customer);
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


            //sqlLiteDbHelper.UpdateCustomer();
            Toast.makeText(getContext(), "Customer added successfully ", Toast.LENGTH_LONG).show();
            cleardata();
        } else {
            Utils.ShowMessageDialog(getContext(), message);
        }
    }

    public boolean checkValidation() {
        if (TextUtils.isEmpty(cust_no.getText().toString().trim())) {
            cust_no.setError("Please fill data");
            cust_no.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(cust_name.getText().toString().trim())) {
            cust_name.setError("Please fill data");
            cust_name.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(cust_add.getText().toString().trim())) {
            cust_add.setError("Please fill data");
            cust_add.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(cust_city.getText().toString().trim())) {
            cust_city.setError("Please fill data");
            cust_city.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(amt.getText().toString().trim())) {
            amt.setError("Please fill data");
            amt.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(rent_amt.getText().toString().trim())) {
            rent_amt.setError("Please fill data");
            rent_amt.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(phone.getText().toString().trim())) {
            phone.setError("Please fill data");
            phone.requestFocus();
            return false;
        }
        if (phone.getText().toString().trim().length() < 8) {
            phone.setError("Invalid phone number");
            phone.requestFocus();
            return false;
        }

        int selectedId = rdogroup.getCheckedRadioButtonId();
        if (selectedId == R.id.rdo_plan) {
            if (TextUtils.isEmpty(edt_month.getText().toString().trim())) {
                edt_month.setError("Please fill data");
                edt_month.requestFocus();
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
        rdo_continus.setChecked(true);
        edt_month.setText("");
    }

    public void UploadCustomer() {

    }


}
