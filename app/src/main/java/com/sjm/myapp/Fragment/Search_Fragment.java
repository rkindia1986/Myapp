package com.sjm.myapp.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.sjm.myapp.Application;
import com.sjm.myapp.Constant;
import com.sjm.myapp.ExpenseReportList;
import com.sjm.myapp.NetworkConnection;
import com.sjm.myapp.R;
import com.sjm.myapp.RetroClient;
import com.sjm.myapp.SearchList;
import com.sjm.myapp.SqlLiteDbHelper;
import com.sjm.myapp.Utils;
import com.sjm.myapp.pojo.Customer;
import com.sjm.myapp.pojo.Expense;
import com.sjm.myapp.pojo.Installation;
import com.sjm.myapp.pojo.Installation_History;
import com.sjm.myapp.pojo.PaymentRecord;
import com.sjm.myapp.pojo.RentRecord;
import com.sjm.myapp.pojo.SearchCustomer;

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

public class Search_Fragment extends Fragment {
    private static final String TAG = "Search_Fragment";
    Installation installation;
    ProgressDialog pd;
    private Unbinder unbinder;
    @BindView(R.id.edt_search_add)
    EditText edt_search_add;
    @BindView(R.id.edt_search_custname)
    EditText edt_search_custname;
    @BindView(R.id.edt_search_stbac)
    EditText edt_search_stbac;
    @BindView(R.id.edt_search_sutno)
    EditText edt_search_sutno;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.radiogroup)
    RadioGroup radiogroup;
    @BindView(R.id.rdo_all)
    RadioButton rdo_all;
    @BindView(R.id.rdo_off)
    RadioButton rdo_off;
    @BindView(R.id.rdo_on)
    RadioButton rdo_on;
    @BindView(R.id.btnsearch)
    Button btnsearch;
    ArrayList<String> s = new ArrayList<String>();
    String connectionStatus = "All";

    ArrayList<String> cityList = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    private SqlLiteDbHelper sqlLiteDbHelper;
    private Customer selCustomer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act_search, container, false);
        unbinder = ButterKnife.bind(this, view);

        sqlLiteDbHelper = new SqlLiteDbHelper(getActivity());
        sqlLiteDbHelper.openDataBase();
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, cityList);
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);
        showProgressDialog();

        UploadCustomer();
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Application.preferences.getLICENCEKEY().equalsIgnoreCase("")) {
                    Utils.ShowMessageDialog(getContext(), "Required Licence? Please contact us");
                } else {
                    int selectedId = radiogroup.getCheckedRadioButtonId();
                    if (selectedId == R.id.rdo_all) {
                        connectionStatus = "";
                    } else if (selectedId == R.id.rdo_off) {
                        connectionStatus = "off";
                    } else if (selectedId == R.id.rdo_on) {
                        connectionStatus = "on";
                    }
                    /*if (NetworkConnection.isNetworkAvailable(getContext())) {
                        try {
                            showProgressDialog();
                            ApiService api = RetroClient.getApiService();
                            Call<String> call = api.SearchCustomer("get_customer_search", edt_search_custname.getText().toString(), edt_search_sutno.getText().toString(), edt_search_add.getText().toString(), spinner.getSelectedItem().toString().toString(), connectionStatus, edt_search_stbac.getText().toString());
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
                    } else {*/
                    Constant.categoryListModel = new SearchCustomer();
                    String city = "";
                    if (spinner.getSelectedItemPosition() > 0) {
                        city = spinner.getSelectedItem().toString().toString();
                    }
                    String que = "select * from Customer_Master where customer_no like '%" + edt_search_sutno.getText().toString().trim() +
                            "%' and name like '%" + edt_search_custname.getText().toString().trim() +
                            "%' and (stb_account_no_1 like '%" + edt_search_stbac.getText().toString().trim()
                            + "%' or stb_account_no_2 like '%" + edt_search_stbac.getText().toString().trim()
                            + "%') and address like '%" + edt_search_add.getText().toString().trim()
                            + "%' and city like '%" + city
                            + "%' and customer_connection_status like '%" + connectionStatus + "%'";
                    ArrayList<Customer> customers = sqlLiteDbHelper.Get_AllCustomers2(que);
                    if (customers != null && customers.size() > 0) {
                        Constant.categoryListModel.setLstCustomer(customers);
                        Intent intent = new Intent(getActivity(), SearchList.class);
                        intent.putExtra("query", que);
                        startActivity(intent);
                    } else {
                        Utils.ShowMessageDialog(getContext(), "No Customer Available");
                    }
                }


            }
        });
        return view;
    }

    private void parseResponse(String body) {
        try {

            JSONObject j = new JSONObject(body);

            if (j != null) {
                if (body.contains("totalRecord") && j.getString("totalRecord").equalsIgnoreCase("0")) {
                    Utils.ShowMessageDialog(getContext(), j.getString("message"));
                } else {
                    Gson gson = new GsonBuilder().create();
                    Constant.categoryListModel = gson.fromJson(body, SearchCustomer.class);
                    if (Constant.categoryListModel.getStatus() == 1) {
                        Utils.ShowMessageDialog(getContext(), Constant.categoryListModel.getMessage());
                    } else {
                        Intent intent = new Intent(getActivity(), SearchList.class);
                        startActivity(intent);
                    }
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

    public void getCityList() {

        if (NetworkConnection.isNetworkAvailable(getContext())) {
            try {

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
            hideProgressDialog();
            cityList.clear();
            cityList = sqlLiteDbHelper.Get_AllCity();
            cityList.add(0, "Select City");
            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, cityList);
            adapter.notifyDataSetChanged();
            spinner.setAdapter(adapter);
            //  Utils.ShowMessageDialog(getContext(), "No Connection Available");
        }

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
                        if (cityList != null && cityList.size() > 0) {
                            sqlLiteDbHelper.UpdateCity(cityList);

                        }
                        cityList.add(0, "Select City");
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

    public void getInstallation() {
        if (Application.preferences.getUSerid().equalsIgnoreCase("") &&
                Application.preferences.getLICENCEKEY().equalsIgnoreCase("")) {
            if (NetworkConnection.isNetworkAvailable(getContext())) {
                try {

                    ApiService api = RetroClient.getApiService();
                    Call<String> call = api.welcome("welcome", Application.preferences.getDeviceId(), Application.preferences.getimei_number());
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Log.e(TAG, "call getDetailsByQr: " + call.toString());

                            Log.e(TAG, "onResponse getDetailsByQr: " + response.body());

                            parseWelcomeResponse(response.body());
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

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

    private void parseWelcomeResponse(String body) {
        String message = "";
        boolean Status = false;
        try {
            JSONObject j = new JSONObject(body);
            if (j != null) {
                if (body.contains("status")) {

                    if (j.getInt("status") == 0) {
                        message = j.optString("message");
                        Status = false;
                    } else if (j.getInt("status") == 1) {
                        Status = true;
                        message = j.optString("message");
                        if (message.equalsIgnoreCase("Awesome! Your account verified.")) {
                            Gson gson = new GsonBuilder().create();
                            try {

                                Installation_History installation_history = gson.fromJson(j.optJSONObject("result").toString(), Installation_History.class);
                                if (installation_history != null && installation_history.getLicence_key() != null && installation_history.getLicence_key().length() > 5 && installation_history.getVerify_licence_key().equalsIgnoreCase("1")) {
                                    Application.preferences.setLICENCEKEY(installation_history.getLicence_key());
                                    Application.preferences.setUSerid(installation_history.getUser_id());
                                    Application.preferences.setMASTERPASS(installation_history.getMaster_password());
                                    Application.preferences.setverify_licence_key(installation_history.getLicence_key());
                                    Application.preferences.setDetails(j.getJSONObject("result").toString());
                                }
                            } catch (Exception e) {

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
            SHowDialog(message);
        } else {
            Utils.ShowMessageDialog(getContext(), message);
        }
    }

    public void SHowDialog(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle(R.string.app_name);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (Build.VERSION.SDK_INT >= 11) {
                            getActivity().recreate();
                        } else {
                            Intent intent = getActivity().getIntent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            getActivity().finish();
                            getActivity().overridePendingTransition(0, 0);

                            startActivity(intent);
                            getActivity().overridePendingTransition(0, 0);
                        }
                    }
                });

        alertDialog.show();
    }


    public void UploadCustomer() {

        ArrayList<Customer> tempcustomers = sqlLiteDbHelper.Get_AllCustomers2("select * from Customer_Master where sync like '0'");
        if (tempcustomers != null && tempcustomers.size() > 0) {
            selCustomer = tempcustomers.get(0);

            if (NetworkConnection.isNetworkAvailable(getContext())) {
                try {

                    ApiService api = RetroClient.getApiService();
                    Call<String> call = null;
                    if (selCustomer.getId().equalsIgnoreCase(selCustomer.getSyncid())) {
                        call = api.add_customer("add_customer", selCustomer.getName(), selCustomer.getCustomer_no(), selCustomer.getAddress(), selCustomer.getCity(), selCustomer.getAmount(), selCustomer.getPhone(), selCustomer.getRent_amount(), selCustomer.getStb_account_no_1(), selCustomer.getNu_id_no_1(), selCustomer.getCaf_no_1(), selCustomer.getStb_account_no_2(), selCustomer.getNu_id_no_2(), selCustomer.getCaf_no_2(), selCustomer.getConnection_type(), selCustomer.getCustomer_connection_status(), selCustomer.getRent_start_date(), selCustomer.getNo_of_month(), Application.preferences.getUSerid());
                    } else {
                        call = api.Edit_customer("edit_customer", selCustomer.getName(), selCustomer.getCustomer_no(), selCustomer.getAddress(), selCustomer.getCity(), selCustomer.getAmount(), selCustomer.getPhone(), selCustomer.getRent_amount(), selCustomer.getStb_account_no_1(), selCustomer.getNu_id_no_1(), selCustomer.getCaf_no_1(), selCustomer.getStb_account_no_2(), selCustomer.getNu_id_no_2(), selCustomer.getCaf_no_2(), selCustomer.getConnection_type(), selCustomer.getCustomer_connection_status(), selCustomer.getRent_start_date(), selCustomer.getNo_of_month(), Application.preferences.getUSerid());
                    }
                    Log.e(TAG, "call add_customer: " + call.request().url().toString());
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {


                            Log.e(TAG, "onResponse getDetailsByQr: " + response.body());

                            parseAddCustResponse(response.body());
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                            UploadCustomer();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                getCityList();
            }
        } else {
            uploadExpense();
        }

    }

    private void parseAddCustResponse(String body) {


        String message = "";
        boolean Status = false;
        JSONObject j = null;
        try {
            j = new JSONObject(body);
            if (j != null) {
                if (body.contains("status")) {

                    if (j.getString("status").equalsIgnoreCase("1")) {

                        Status = false;
                    } else if (j.getString("status").equalsIgnoreCase("0")) {

                        if (j.getString("message").equalsIgnoreCase("success")) {
                            Status = true;
                            message = j.optString("message");

                        } else {
                            Status = false;

                        }
                    } else {
                        Status = false;

                    }
                }
            } else {
                Status = false;

            }

        } catch (Exception e) {
            e.printStackTrace();
            Status = false;


        }
        if (Status && j!=null) {
            Gson gson = new GsonBuilder().create();
            JSONObject jsonObject = j.optJSONObject("result");
            Customer customer = gson.fromJson(jsonObject.toString(), Customer.class);
            customer.setSync("1");
            customer.setSyncid(selCustomer.getSyncid());
            customer.setAmount2(selCustomer.getAmount2());
            sqlLiteDbHelper.UpdateCustomer(customer, selCustomer.getSyncid());
            sqlLiteDbHelper.UpdateAllPaymentIDs(selCustomer.getSyncid(), customer.getId());
            sqlLiteDbHelper.UpdateAllExpenseIDs(selCustomer.getSyncid(), customer.getId());
            sqlLiteDbHelper.UpdateAllRentIDs(selCustomer.getSyncid(), customer.getId());
            UploadCustomer();
        } else {
            UploadCustomer();
        }

    }

    Expense expense = null;

    public void uploadExpense() {

        if (NetworkConnection.isNetworkAvailable(getContext())) {

            try {
                ArrayList<Expense> arrexp = sqlLiteDbHelper.getExpenserecords("select * from manage_expense where sync='0'");

                if (arrexp != null && arrexp.size() > 0) {
                    expense = null;
                    for (int i = 0; i < arrexp.size(); i++) {
                        if (!arrexp.get(i).getId().contains("temp")) {
                            expense = arrexp.get(i);
                            break;
                        }
                    }
                    if (expense != null) {
                        ApiService api = RetroClient.getApiService();
                        Call<String> call = api.add_expense_income("add_expense_income", expense.getExpense_type(), expense.getDescription(), expense.getAmount(), expense.getExpense_date(), expense.getId());
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Log.e(TAG, "call getDetailsByQr: " + call.toString());
                                Log.e(TAG, "onResponse getDetailsByQr: " + response.body());
                                parseManageExpenseResponse(response.body());
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                uploadExpense();
                                Log.e(TAG, "onFailure getDetailsByQr: " + t.getMessage());

                            }
                        });
                    }
                }else
                {
                    UploadPayment();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            getCityList();
        }
    }

    private void parseManageExpenseResponse(String body) {

        boolean Status = false;
        try {
            JSONObject j = new JSONObject(body);
            if (j != null) {
                if (body.contains("status")) {

                    if (j.getInt("status") == 0) {
                        Status = true;

                    } else {
                        Status = false;

                    }
                }
            } else {
                Status = false;

            }

        } catch (Exception e) {
            e.printStackTrace();
            Status = false;


        }
        if (Status) {
            expense.setSync("1");
            sqlLiteDbHelper.UpdateExpense(expense);
            uploadExpense();
        } else {
            uploadExpense();
        }
    }

    RentRecord rentRecord;

    public void UploadRent() {

        if (NetworkConnection.isNetworkAvailable(getContext())) {

            try {
                ArrayList<RentRecord> rentrecords = sqlLiteDbHelper.getRentRecord2("select * from rent_record where sync='0'");

                if (rentrecords != null && rentrecords.size() > 0) {
                    rentRecord = null;
                    for (int i = 0; i < rentrecords.size(); i++) {
                        if (!rentrecords.get(i).getId().contains("temp")) {
                            rentRecord = rentrecords.get(i);
                            break;
                        }
                    }
                    if (rentRecord != null) {
                        ApiService api = RetroClient.getApiService();
                        Call<String> call = api.insertRentRecord("rent_record_by_customer", rentRecord.getRent_start_date(), rentRecord.getRent_end_date(), rentRecord.getId(), Application.preferences.getUSerid(), rentRecord.getPayment_amount(), rentRecord.getPayment_status());
                        Log.e(TAG, "UploadRent: " + call.request().url().toString());
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Log.e(TAG, "call getDetailsByQr: " + call.toString());
                                Log.e(TAG, "onResponse getDetailsByQr: " + response.body());
                                parseRentResponse(response.body());
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                UploadRent();
                                Log.e(TAG, "onFailure getDetailsByQr: " + t.getMessage());
                                // Utils.ShowMessageDialog(getContext(), "Error Occurred");
                            }
                        });
                    }else
                    {
                        getCityList();
                    }
                }else
                {
                    getCityList();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {
            getCityList();
        }
    }

    private void parseRentResponse(String body) {

        boolean Status = false;
        try {
            JSONObject j = new JSONObject(body);
            if (j != null) {
                if (body.contains("status")) {

                    if (j.getInt("status") == 0) {
                        Status = true;

                    } else {
                        Status = false;
                    }
                }
            } else {
                Status = false;

            }

        } catch (Exception e) {
            e.printStackTrace();
            Status = false;


        }
        if (Status) {
            rentRecord.setSync("1");
            sqlLiteDbHelper.UpdateRentRecordStatus(rentRecord);
            UploadRent();
        } else {
            UploadRent();
        }
    }

    PaymentRecord paymentRecord = null;

    public void UploadPayment() {
        if (NetworkConnection.isNetworkAvailable(getActivity())) {

            try {
                ArrayList<PaymentRecord> paymentRecords = sqlLiteDbHelper.getPaymentrecords("select * from customer_payment where sync='0'");

                if (paymentRecords != null && paymentRecords.size() > 0) {
                    paymentRecord = null;
                    for (int i = 0; i < paymentRecords.size(); i++) {
                        if (!paymentRecords.get(i).getId().contains("temp")) {
                            paymentRecord = paymentRecords.get(i);
                            break;
                        }
                    }
                    if (paymentRecord != null) {
                        ApiService api = RetroClient.getApiService();
                        Call<String> call = api.add_customer_payment_amount("add_customer_payment_amount", paymentRecord.getId(), Application.preferences.getUSerid(), paymentRecord.getPayment_amount());
                        Log.e(TAG, "UploadPayment: " + call.request().url().toString());
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Log.e(TAG, "call getDetailsByQr: " + call.toString());
                                Log.e(TAG, "onResponse getDetailsByQr: " + response.body());
                                parsePaymentResponse(response.body());
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                UploadPayment();
                                Log.e(TAG, "onFailure getDetailsByQr: " + t.getMessage());
                                // Utils.ShowMessageDialog(getContext(), "Error Occurred");
                            }
                        });
                    }else
                    {
                        UploadRent();
                    }
                }else
                {
                    UploadRent();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            getCityList();
        }
    }

    private void parsePaymentResponse(String body) {

        boolean Status = false;
        try {
            JSONObject j = new JSONObject(body);
            if (j != null) {
                if (body.contains("status")) {

                    if (j.getInt("status") == 0) {
                        Status = true;

                    } else {
                        Status = false;
                    }
                }
            } else {
                Status = false;

            }

        } catch (Exception e) {
            e.printStackTrace();
            Status = false;


        }
        if (Status) {
            paymentRecord.setSync("1");
            sqlLiteDbHelper.UpdatePayment(paymentRecord);
            UploadPayment();
        } else {
            UploadPayment();
        }
    }
}
