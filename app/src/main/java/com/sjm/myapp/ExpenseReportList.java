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

import com.sjm.myapp.Fragment.manageExp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Helly-PC on 06/01/2017.
 */

public class ExpenseReportList extends AppCompatActivity {
    private static final String TAG = "ExpenseReportList";
    ProgressDialog pd;
    private Unbinder unbinder;
    @BindView(R.id.txttotal)
    TextView txttotal;
    @BindView(R.id.listview)
    ListView listview;
    ExpenseListAdapter searchListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.expensereport);
        unbinder = ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("EXPENSE REPORT LIST");

        searchListAdapter = new ExpenseListAdapter();
        listview.setAdapter(searchListAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        int total = 0;
        for (int i = 0; i < manageExp.expenseReport.getLstExpense().size(); i++) {
            total = total + Integer.parseInt(manageExp.expenseReport.getLstExpense().get(i).getAmount());
        }
        txttotal.setText(total+"");
    }

    class ExpenseListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return manageExp.expenseReport.getLstExpense().size();
        }

        @Override
        public Object getItem(int position) {
            return manageExp.expenseReport.getLstExpense().get(position);
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
                convertView = li.inflate(R.layout.expense, null);
                v.txtTitle = (TextView) convertView.findViewById(R.id.txt1);
                v.txtAddress = (TextView) convertView.findViewById(R.id.txt2);
                v.txtif = (TextView) convertView.findViewById(R.id.txt4);
                v.txtCity = (TextView) convertView.findViewById(R.id.txt3);

                // v.checkbox.setChecked(false);
                //   v.checkbox.setChecked(listChecked.get(position));
                convertView.setTag(v);
            } else {
                v = (ViewHolder) convertView.getTag();
            }

            v.txtTitle.setText(manageExp.expenseReport.getLstExpense().get(position).getAmount());
            v.txtAddress.setText(manageExp.expenseReport.getLstExpense().get(position).getDescription());
            v.txtCity.setText(manageExp.expenseReport.getLstExpense().get(position).getExpense_type());
            v.txtif.setText(manageExp.expenseReport.getLstExpense().get(position).getId());

            return convertView;
        }
    }

    private class ViewHolder {
        TextView txtTitle;
        TextView txtAddress;
        TextView txtif;
        TextView txtCity;

        //  Button btnDone;

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}