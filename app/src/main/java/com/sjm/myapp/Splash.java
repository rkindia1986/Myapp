package com.sjm.myapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sjm.myapp.pojo.Installation_History;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Helly-PC on 08/02/2017.
 */

public class Splash extends AppCompatActivity {
    private static final String TAG = "Splash";

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= 11) {
                        recreate();
                    } else {
                        Intent intent = getIntent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        finish();
                        overridePendingTransition(0, 0);

                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                } else {
                    Toast.makeText(Splash.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                ActivityCompat.requestPermissions(Splash.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
                        1);
            } else {
                init();
            }
        } else {
            init();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (com.sjm.myapp.Application.preferences.getLICENCEKEY().trim().equalsIgnoreCase("")) {
        } else {
            startActivity(new Intent(Splash.this, MainActivity.class));
            finish();
        }

    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (result == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public void init() {


        SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(Splash.this);
        sqlLiteDbHelper.openDataBase();
        String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.e("android_id", android_id + "");
        TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        Application.preferences.setDeviceId(android_id + "22");
        Application.preferences.setimei_number(mngr.getDeviceId() + "22");


        setTitle("Welcome");

        if (com.sjm.myapp.Application.preferences.getLICENCEKEY().trim().equalsIgnoreCase(""))
            getInstallation();
        else {
            startActivity(new Intent(Splash.this, MainActivity.class));
            finish();
        }


    }

    public void getInstallation() {
        if (com.sjm.myapp.Application.preferences.getUSerid().equalsIgnoreCase("") &&
                com.sjm.myapp.Application.preferences.getLICENCEKEY().equalsIgnoreCase("")) {
            if (NetworkConnection.isNetworkAvailable(Splash.this)) {
                try {

                    ApiService api = RetroClient.getApiService();
                    Call<String> call = api.welcome("welcome", com.sjm.myapp.Application.preferences.getDeviceId(), com.sjm.myapp.Application.preferences.getimei_number());
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Log.e(TAG, "call getDetailsByQr: " + call.toString());

                            Log.e(TAG, "onResponse getDetailsByQr: " + response.body());

                            parseWelcomeResponse(response.body());
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                            Log.e(TAG, "onFailure getDetailsByQr: " + t.getMessage());
                            Utils.ShowMessageDialog(Splash.this, "Error Occurred");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Utils.ShowMessageDialog(Splash.this, "No Connection Available");
            }
        }
    }

    private void parseWelcomeResponse(String body) {
        String message = "";
        boolean Status = false;
        try {
            JSONObject j = new JSONObject(body);
            if (j != null) {
                if (body.contains("status")) {

                    if (j.getInt("status") == 0) {
                        message = j.optString("message");
                        Status = false;
                    } else if (j.getInt("status") == 1) {
                        Status = true;
                        message = j.optString("message");
                        if (message.equalsIgnoreCase("Awesome! Your account verified.")) {
                            Gson gson = new GsonBuilder().create();
                            try {

                                Installation_History installation_history = gson.fromJson(j.optJSONObject("result").toString(), Installation_History.class);
                                if (installation_history != null && installation_history.getLicence_key() != null && installation_history.getLicence_key().length() > 5 && installation_history.getVerify_licence_key().equalsIgnoreCase("1")) {
                                    com.sjm.myapp.Application.preferences.setLICENCEKEY(installation_history.getLicence_key());
                                    com.sjm.myapp.Application.preferences.setUSerid(installation_history.getUser_id());
                                    com.sjm.myapp.Application.preferences.setMASTERPASS(installation_history.getMaster_password());
                                    com.sjm.myapp.Application.preferences.setverify_licence_key(installation_history.getLicence_key());
                                    com.sjm.myapp.Application.preferences.setDetails(j.getJSONObject("result").toString());
                                }
                            } catch (Exception e) {

                            }
                        } else {
                            Status = false;
                            message = j.optString("message");
                        }
                    } else {
                        Status = false;
                        message = "Error Occurred";
                    }
                }
            } else {
                Status = false;
                message = "Error Occurred";
            }

        } catch (Exception e) {
            e.printStackTrace();
            Status = false;
            message = "Error Occurred";

        }
        if (Status) {
            SHowDialog(message);
        } else {
            Utils.ShowMessageDialog(Splash.this, message);
        }
    }

    public void SHowDialog(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Splash.this);
        alertDialog.setTitle(R.string.app_name);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (Build.VERSION.SDK_INT >= 11) {
                            Splash.this.recreate();
                        } else {
                            Intent intent = Splash.this.getIntent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            Splash.this.finish();
                            Splash.this.overridePendingTransition(0, 0);

                            startActivity(intent);
                            Splash.this.overridePendingTransition(0, 0);
                        }
                    }
                });

        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (Application.preferences.getLICENCEKEY().equalsIgnoreCase(""))
            getMenuInflater().inflate(R.menu.main, menu);
        else
            getMenuInflater().inflate(R.menu.mainedit, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // startActivity(new Intent(Splash.this, Setting.class));

        } else if (id == R.id.action_addlicence) {
            startActivity(new Intent(Splash.this, AddLicence.class));

        } else if (id == R.id.action_editlicence) {
            startActivity(new Intent(Splash.this, EditLicence.class));

        } else if (id == R.id.action_contactus) {
            startActivity(new Intent(Splash.this, ContactUs.class));

        }


        return super.onOptionsItemSelected(item);
    }
}
