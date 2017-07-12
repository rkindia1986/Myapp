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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sjm.myapp.Fragment.Search_Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Helly-PC on 06/01/2017.
 */

public class SearchList extends AppCompatActivity {
    private static final String TAG = "Backup";
    ProgressDialog pd;
    private Unbinder unbinder;

    @BindView(R.id.listview)
    ListView listview;
    SearchListAdapter searchListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.searchlist);
        unbinder = ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("SEARCH LIST");

        searchListAdapter = new SearchListAdapter();
        searchListAdapter.notifyDataSetChanged();
        listview.setAdapter(searchListAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SearchList.this, ViewDtails.class);
                intent.putExtra("selected", i);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        searchListAdapter.notifyDataSetChanged();
    }

    class SearchListAdapter extends BaseAdapter {


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
                convertView = li.inflate(R.layout.searchlistraw, null);
                v.txtname = (TextView) convertView.findViewById(R.id.txt1);
                v.txtdate = (TextView) convertView.findViewById(R.id.txt7);
                v.txtAddress = (TextView) convertView.findViewById(R.id.txt2);
                v.txtAmount = (TextView) convertView.findViewById(R.id.txt3);
                v.txtCNo = (TextView) convertView.findViewById(R.id.txtCno);
                v.txtConn = (TextView) convertView.findViewById(R.id.txt5);
                v.lytparnt = (LinearLayout) convertView.findViewById(R.id.lytparent);
                // v.checkbox.setChecked(false);
                //   v.checkbox.setChecked(listChecked.get(position));
                convertView.setTag(v);
            } else {
                v = (ViewHolder) convertView.getTag();
            }

            v.txtname.setText(Search_Fragment.categoryListModel.getLstCustomer().get(position).getName());
            v.txtAddress.setText("Address: " + Search_Fragment.categoryListModel.getLstCustomer().get(position).getAddress() + ", " + Search_Fragment.categoryListModel.getLstCustomer().get(position).getCity());
            v.txtAmount.setText("Amount: " + Search_Fragment.categoryListModel.getLstCustomer().get(position).getAmount());
            v.txtCNo.setText("C No: " + Search_Fragment.categoryListModel.getLstCustomer().get(position).getCustomer_no());
            v.txtConn.setText("Conn: " +  Search_Fragment.categoryListModel.getLstCustomer().get(position).getCustomer_connection_status());
            v.txtdate.setText("Start: " + Search_Fragment.categoryListModel.getLstCustomer().get(position).getRent_start_date() );
            if (Search_Fragment.categoryListModel.getLstCustomer().get(position).getCustomer_connection_status().equalsIgnoreCase("off")) {
                v.lytparnt.setBackgroundColor(getResources().getColor(R.color.red));
            } else {
                v.lytparnt.setBackgroundColor(getResources().getColor(R.color.green));
            }
            return convertView;
        }
    }

    private class ViewHolder {
        TextView txtname;
        TextView txtAddress;
        TextView txtAmount;

        TextView txtCNo;
        TextView txtConn;
        TextView txtdate;
        LinearLayout lytparnt;

        //  Button btnDone;

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}