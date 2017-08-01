package com.sjm.myapp.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.sjm.myapp.R;
import com.sjm.myapp.SqlLiteDbHelper;
import com.sjm.myapp.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Helly-PC on 05/31/2017.
 */

public class NewMonth_rent extends Fragment {
    private static final String TAG = "Update_Customer";
    ProgressDialog pd;
    private Unbinder unbinder;
    @BindView(R.id.edt_new_month_rent)
    EditText edt_new_month_rent;

    @BindView(R.id.btn_addrent)
    Button btn_addrent;

    @BindView(R.id.spinner)
    Spinner spinner;
    ArrayList<String> cityList = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    SqlLiteDbHelper sqlLiteDbHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_month_rent, container, false);
        unbinder = ButterKnife.bind(this, view);
        sqlLiteDbHelper = new SqlLiteDbHelper(getActivity());
        sqlLiteDbHelper.openDataBase();
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, cityList);
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);

        getCityList();

        btn_addrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {

                    sqlLiteDbHelper.UpdateRent(spinner.getSelectedItem().toString(), edt_new_month_rent.getText().toString());
                    Utils.ShowMessageDialog(getContext(), "Rent Updated successfully");
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
            Utils.ShowMessageDialog(getContext(), message);
            //  Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            edt_new_month_rent.setText("");
        } else {
            Utils.ShowMessageDialog(getContext(), message);
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
        if (TextUtils.isEmpty(edt_new_month_rent.getText().toString().trim())) {
            edt_new_month_rent.setError("Please enter rent");
            return false;
        }
        if(spinner.getSelectedItemPosition() ==0)
        {
            Utils.ShowMessageDialog(getActivity(),"Please Select City");
            return false;
        }
        return true;
    }


}
