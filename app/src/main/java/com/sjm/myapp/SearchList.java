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
        listview.setAdapter(searchListAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SearchList.this,ViewDtails.class);
                intent.putExtra("selected",i);
                startActivity(intent);
            }
        });
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
                convertView = li.inflate(R.layout.listraw, null);
                v.txtTitle = (TextView) convertView.findViewById(R.id.txt1);
                v.txtAddress = (TextView) convertView.findViewById(R.id.txt2);
                v.txtCity = (TextView) convertView.findViewById(R.id.txt3);

                // v.checkbox.setChecked(false);
                //   v.checkbox.setChecked(listChecked.get(position));
                convertView.setTag(v);
            } else {
                v = (ViewHolder) convertView.getTag();
            }

            v.txtTitle.setText(Search_Fragment.categoryListModel.getLstCustomer().get(position).getName() );
            v.txtAddress.setText(Search_Fragment.categoryListModel.getLstCustomer().get(position).getCity());
            v.txtCity.setText(Search_Fragment.categoryListModel.getLstCustomer().get(position).getAmount());

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