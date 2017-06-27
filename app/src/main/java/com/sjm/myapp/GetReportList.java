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

public class GetReportList extends AppCompatActivity {
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

        setTitle("REPORT LIST");

        rentRecords = ViewDtails.rentRecordsList.getLstrentrecord();
        searchListAdapter = new PaymentListAdapter();
        listview.setAdapter(searchListAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(GetReportList.this, ViewDtails.class);
                intent.putExtra("selected", i);
                startActivity(intent);
            }
        });
    }

    class PaymentListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return Search_Fragment.categoryListModel.getLstCustomer().size();
        }

        @Override
        public Object getItem(int position) {
            return Search_Fragment.categoryListModel.getLstCustomer().get(position);
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
                v.txtTitle = (TextView) convertView.findViewById(R.id.txt1);
                v.txtAddress = (TextView) convertView.findViewById(R.id.txt2);
                v.txtCity = (TextView) convertView.findViewById(R.id.txt3);

                // v.chec+kbox.setChecked(false);
                //   v.checkbox.setChecked(listChecked.get(position));
                convertView.setTag(v);
            } else {
                v = (ViewHolder) convertView.getTag();
            }

            v.txtTitle.setText(rentRecords.get(position).getCustomer_id());
            v.txtAddress.setText(rentRecords.get(position).getCreated_at());
            v.txtCity.setText(rentRecords.get(position).getPayment_amount());

            return convertView;
        }
    }

    private class ViewHolder {
        TextView txtTitle;
        TextView txtAddress;
        TextView txtCity;

        //  Button btnDone;

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}