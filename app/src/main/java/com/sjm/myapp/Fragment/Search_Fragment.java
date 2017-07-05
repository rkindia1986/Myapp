package com.sjm.myapp.Fragment;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.sjm.myapp.GetReportList;
import com.sjm.myapp.NetworkConnection;
import com.sjm.myapp.R;
import com.sjm.myapp.RetroClient;
import com.sjm.myapp.SearchList;
import com.sjm.myapp.SqlLiteDbHelper;
import com.sjm.myapp.Utils;
import com.sjm.myapp.pojo.Installation;
import com.sjm.myapp.pojo.RentRecordList;
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
    public static SearchCustomer categoryListModel = new SearchCustomer();
    ArrayList<String> cityList = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    private SqlLiteDbHelper sqlLiteDbHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act_search, container, false);
        unbinder = ButterKnife.bind(this, view);
        getInstallation();
        sqlLiteDbHelper=new SqlLiteDbHelper(getActivity());
        sqlLiteDbHelper.openDataBase();
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, cityList);
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);

        getCityList();
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedId = radiogroup.getCheckedRadioButtonId();
                if (selectedId == R.id.rdo_all) {
                    connectionStatus = "all";
                } else if (selectedId == R.id.rdo_off) {
                    connectionStatus = "off";
                } else if (selectedId == R.id.rdo_on) {
                    connectionStatus = "on";
                }
                if (NetworkConnection.isNetworkAvailable(getContext())) {
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
                } else {
                    Utils.ShowMessageDialog(getContext(), "No Connection Available");
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
                    categoryListModel = gson.fromJson(body, SearchCustomer.class);
                    if (categoryListModel.getStatus() == 1) {
                        Utils.ShowMessageDialog(getContext(), categoryListModel.getMessage());
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
            cityList.clear();
            cityList = sqlLiteDbHelper.Get_AllCity();
            adapter.notifyDataSetChanged();
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
                        JSONArray jsonArray = j.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            cityList.add(jsonArray.getString(i));
                        }
                        if(cityList!=null && cityList.size() >0)
                        {
                            sqlLiteDbHelper.UpdateCity(cityList);
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

    public void getInstallation() {
        if (NetworkConnection.isNetworkAvailable(getContext())) {
            try {

                ApiService api = RetroClient.getApiService();
                Call<String> call = api.welcome("welcome", com.sjm.myapp.Application.preferences.getDeviceId(), com.sjm.myapp.Application.preferences.getimei_number());
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

    private void parseWelcomeResponse(String body) {
        String message = "";
        boolean Status = false;
        try {
            JSONObject j = new JSONObject(body);
            if (j != null) {
                if (body.contains("status")) {

                    if (j.getInt("status")==0) {
                        message = j.optString("message");
                        Status = false;
                    } else if (j.getInt("status")==1){
                        Status = true;
                        message = j.optString("message");
                        Gson gson = new GsonBuilder().create();
                        installation = gson.fromJson(j.getJSONObject("result").toString(), Installation.class);
                        Log.e(TAG, "parseWelcomeResponse: "+ gson.toJson( installation).toString() );
                        com.sjm.myapp.Application.preferences.setLICENCEKEY(installation.getLicence_key());
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

            //  Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        } else {
            Utils.ShowMessageDialog(getContext(), message);
        }
    }

}
