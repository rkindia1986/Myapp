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

import com.sjm.myapp.Fragment.Search_Fragment;
import com.sjm.myapp.pojo.RentRecord;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Helly-PC on 06/01/2017.
 */

public class RentList extends AppCompatActivity {
    private static final String TAG = "RentList";
    ProgressDialog pd;
    private Unbinder unbinder;

    @BindView(R.id.listview)
    ListView listview;
    PaymentListAdapter searchListAdapter;
    ArrayList<RentRecord> rentRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.searchlist);
        unbinder = ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("RENT RECORDS");
        rentRecords = ViewDtails.rentRecordsList.getLstrentrecord();
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
                v.txtpayamt = (TextView) convertView.findViewById(R.id.txt2);
                v.txtPayStatus = (TextView) convertView.findViewById(R.id.txt3);
                v.txtrentstart = (TextView) convertView.findViewById(R.id.txt4);
                v.rentENd = (TextView) convertView.findViewById(R.id.txt5);

                // v.chec+kbox.setChecked(false);
                //   v.checkbox.setChecked(listChecked.get(position));
                convertView.setTag(v);
            } else {
                v = (ViewHolder) convertView.getTag();
            }

            v.txtid.setText("ID: " + rentRecords.get(position).getId());
            v.txtpayamt.setText("Amt: " + rentRecords.get(position).getPayment_amount());
            v.txtPayStatus.setText("Ststus: " + rentRecords.get(position).getPayment_status());
            v.txtrentstart.setText("Rent Start: " + rentRecords.get(position).getRent_start_date());
            v.rentENd.setText("Rent End: " + rentRecords.get(position).getRent_end_date());

            return convertView;
        }
    }

    private class ViewHolder {
        TextView txtid;
        TextView txtpayamt;
        TextView txtPayStatus;
        TextView txtrentstart;
        TextView rentENd;

        //  Button btnDone;

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}