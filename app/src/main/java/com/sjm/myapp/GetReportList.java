package com.sjm.myapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sjm.myapp.Fragment.GetReport;
import com.sjm.myapp.Fragment.Search_Fragment;
import com.sjm.myapp.pojo.RentRecord;
import com.sjm.myapp.pojo.RentRecordList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Helly-PC on 06/01/2017.
 */

public class GetReportList extends AppCompatActivity {
    private static final String TAG = "RentList";
    ProgressDialog pd;
    private Unbinder unbinder;

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.txt_rentstart)
    TextView txt_rentstart;
    @BindView(R.id.txt_rentend)
    TextView txt_rentend;
    @BindView(R.id.txt_renttotal)
    TextView txt_renttotal;
    PaymentListAdapter searchListAdapter;

    ArrayList<RentRecord> rentRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.getreport_list);
        unbinder = ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("REPORT LIST");

        rentRecords = GetReport.rentRecordList.getRentRecords();
        txt_rentend.setText("Rent End Date : " + GetReport.rentRecordList.getRentSummary().getRent_end_date());
        txt_rentstart.setText("Rent Start Date : " +  GetReport.rentRecordList.getRentSummary().getRent_from_date());
        txt_renttotal.setText("Total : " +  GetReport.rentRecordList.getRentSummary().getTotal_based_on_total());

        searchListAdapter = new PaymentListAdapter();
        listview.setAdapter(searchListAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    class PaymentListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return rentRecords.size();
        }

        @Override
        public Object getItem(int position) {
            return rentRecords.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //   View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hirizontal_list_item, null);
            final ViewHolder v;
            if (convertView == null) {
                v = new ViewHolder();
                LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = li.inflate(R.layout.rentraw, null);
                v.txtid = (TextView) convertView.findViewById(R.id.txt1);
                v.txtcustname = (TextView) convertView.findViewById(R.id.txt2);
                v.txtAdd = (TextView) convertView.findViewById(R.id.txt3);
                v.txtpaymentstatus = (TextView) convertView.findViewById(R.id.txt4);
                v.txtpayamt = (TextView) convertView.findViewById(R.id.txt5);
                v.txtcreated = (TextView) convertView.findViewById(R.id.txt6);


                // v.chec+kbox.setChecked(false);
                //   v.checkbox.setChecked(listChecked.get(position));
                convertView.setTag(v);
            } else {
                v = (ViewHolder) convertView.getTag();
            }

            v.txtid.setText("Cust Id: " + rentRecords.get(position).getCustomer_id());
            v.txtcreated.setText("Created : " +  rentRecords.get(position).getCreated_at());
            v.txtcustname.setText("Name:" + rentRecords.get(position).getName());
            v.txtAdd.setText("Add:" + rentRecords.get(position).getAddress() + ", " +rentRecords.get(position).getCity());
            v.txtpaymentstatus.setText("Status:" + rentRecords.get(position).getPayment_status());
            v.txtpayamt.setText("Amt:" + rentRecords.get(position).getPayment_amount());

            return convertView;
        }
    }

    private class ViewHolder {
        TextView txtid;
        TextView txtcustname;
        TextView txtAdd;
        TextView txtpaymentstatus;
        TextView txtpayamt;
        TextView txtcreated;




        //  Button btnDone;

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}