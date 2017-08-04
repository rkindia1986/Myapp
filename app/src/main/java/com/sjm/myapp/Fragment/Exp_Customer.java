package com.sjm.myapp.Fragment;

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
import android.widget.Spinner;

import com.sjm.myapp.Constant;
import com.sjm.myapp.ExpiringList;
import com.sjm.myapp.R;
import com.sjm.myapp.SqlLiteDbHelper;
import com.sjm.myapp.Utils;
import com.sjm.myapp.pojo.Customer;
import com.sjm.myapp.pojo.SearchCustomer;

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

public class Exp_Customer extends Fragment {
    private static final String TAG = "Exp_Customer";
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.btn_loaddata)
    Button btn_loaddata;
    ProgressDialog pd;
    private Unbinder unbinder;
    private SqlLiteDbHelper sqlLiteDbHelper;
    ArrayList<String> cityList = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ArrayList<Customer> customers = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exp_cust, container, false);
        unbinder = ButterKnife.bind(this, view);
        sqlLiteDbHelper = new SqlLiteDbHelper(getActivity());
        sqlLiteDbHelper.openDataBase();
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, cityList);
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);

        getCityList();

        btn_loaddata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCustomer();
            }
        });
        return view;
    }

    public void getCustomer() {

        String Startdate = "", EndDate = "";
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Date myDate = calendar.getTime();

        Startdate = simpleDateFormat.format(myDate);
        if (myDate != null) {
            calendar.add(Calendar.DAY_OF_MONTH, 11);
            myDate = calendar.getTime();
            EndDate = simpleDateFormat.format(myDate);

        }


        String city = "%";
        if (spinner.getSelectedItemPosition() > 0) {
            city = spinner.getSelectedItem().toString().toString();
        }
        customers.clear();

        String s = "select * from Customer_Master where city like '" + city + "' and rent_end_date >= '" + Startdate + "' and rent_end_date<='" + EndDate + "'";
        Log.e(TAG, "EXpiring Customer: " + s);
        customers = sqlLiteDbHelper.Get_AllCustomers2(s);
        if (customers != null && customers.size() > 0) {
            Constant.categoryListModel = new SearchCustomer();
            Constant.categoryListModel.setLstCustomer(customers);
            Intent intent = new Intent(getContext(), ExpiringList.class);
            intent.putExtra("query",s);
            startActivity(intent);
        } else {
            Utils.ShowMessageDialog(getContext(), "No Record Available");
        }


    }

    public void getCityList() {

        cityList.clear();
        cityList = sqlLiteDbHelper.Get_AllCity();
        cityList.add(0, "Select City");
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, cityList);
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);
    }
}
