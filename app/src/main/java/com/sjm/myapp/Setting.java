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

public class Setting extends AppCompatActivity {
    private static final String TAG = "Setting";
    ProgressDialog pd;
    private Unbinder unbinder;

    @BindView(R.id.btn_masterpass)
    Button btn_masterpass;
    @BindView(R.id.btn_setlink)
    Button btn_setlink;

    @BindView(R.id.edt_set_wlink)
    EditText edt_set_wlink;
    @BindView(R.id.edt_set_masterpass)
    EditText edt_set_masterpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setting);
        unbinder = ButterKnife.bind(this);
        setTitle(getString(R.string.action_settings).toUpperCase());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btn_masterpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn_setlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
