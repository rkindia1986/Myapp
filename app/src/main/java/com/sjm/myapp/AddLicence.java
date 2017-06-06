package com.sjm.myapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Helly-PC on 06/02/2017.
 */

public class AddLicence extends AppCompatActivity {
    private static final String TAG = "AddLicence";
    ProgressDialog pd;
    private Unbinder unbinder;

    @BindView(R.id.btnsubmit)
    Button btnsubmit;
    @BindView(R.id.btnclear)
    Button btnclear;

    @BindView(R.id.edt_licencekey)
    EditText edt_licencekey;
    @BindView(R.id.edt_op_name)
    EditText edt_op_name;
    @BindView(R.id.edt_op_contactno)
    EditText edt_op_contactno;
    @BindView(R.id.edt_cab_name)
    EditText edt_cab_name;
    @BindView(R.id.edt_op_add)
    EditText edt_op_add;
    @BindView(R.id.edt_web_link)
    EditText edt_web_link;
    @BindView(R.id.edt_web_loginpass)
    EditText edt_web_loginpass;
    @BindView(R.id.edt_master_pass)
    EditText edt_master_pass;
    @BindView(R.id.edt_web_loginid)
    EditText edt_web_loginid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.addlicence);
        unbinder = ButterKnife.bind(this);
        setTitle(getString(R.string.adlice).toUpperCase());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_cab_name.setText("");
                edt_licencekey.setText("");
                edt_master_pass.setText("");
                edt_op_add.setText("");
                edt_op_contactno.setText("");
                edt_op_name.setText("");
                edt_web_loginpass.setText("");
                edt_web_loginid.setText("");
                edt_web_link.setText("");
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
