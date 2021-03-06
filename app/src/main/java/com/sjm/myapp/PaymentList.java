package com.sjm.myapp;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.sjm.myapp.pojo.PaymentRecord;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Helly-PC on 06/01/2017.
 */

public class PaymentList extends AppCompatActivity {
    private static final String TAG = "PaymentList";
    ProgressDialog pd;
    private Unbinder unbinder;
    String custno = "";

    @BindView(R.id.listview)
    ListView listview;
    PaymentListAdapter searchListAdapter;
    ArrayList<PaymentRecord> paymentRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.paymentlist);
        unbinder = ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("PAYMENT RECORDS");

        paymentRecords = ViewDtails.paymentRecordsList.getLstPaymentrecords();
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
            return paymentRecords.size();
        }

        @Override
        public Object getItem(int position) {
            return paymentRecords.get(position);
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
                convertView = li.inflate(R.layout.listraw, null);
                v.txtid = (TextView) convertView.findViewById(R.id.txt1);
                v.txtamt = (TextView) convertView.findViewById(R.id.txt2);
                v.txtdate = (TextView) convertView.findViewById(R.id.txt3);

                // v.checkbox.setChecked(false);
                //   v.checkbox.setChecked(listChecked.get(position));
                convertView.setTag(v);
            } else {
                v = (ViewHolder) convertView.getTag();
            }

            v.txtid.setText(ViewDtails.customer.getCustomer_no());
            v.txtdate.setText(paymentRecords.get(position).getCreated_at());
            v.txtamt.setText(paymentRecords.get(position).getPayment_amount());

            return convertView;
        }
    }

    private class ViewHolder {
        TextView txtid;
        TextView txtamt;
        TextView txtdate;

        //  Button btnDone;

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}