package com.sjm.myapp.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sjm.myapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Helly-PC on 05/31/2017.
 */

public class Update_Customer extends Fragment {
    private static final String TAG = "Update_Customer";
    ProgressDialog pd;
    private Unbinder unbinder;
    @BindView(R.id.edt_search_add)
    EditText edt_search_add;
    @BindView(R.id.btn_loaddata)
    Button btn_loaddata;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.update_customer, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }
}
