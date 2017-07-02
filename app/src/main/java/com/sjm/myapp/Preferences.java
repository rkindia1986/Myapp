package com.sjm.myapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

@SuppressLint("CommitPrefEdits")
public class Preferences {
    SharedPreferences pref;
    // Editor for Shared preferences
    Editor editor;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "CableApp";
    public static final String DeviceId = "DeviceId";
    public static final String imei_number = "UUID";
    public static final String LICENCEKEY = "LICENCEKEY";
    public static final String MASTERPASS = "MASTERPASS";
    public static final String USerid = "user_id";
    public static final String verify_licence_key = "verify_licence_key";

    public void setverify_licence_key(String DeviceIdd) {
        editor.putString(verify_licence_key, DeviceIdd);
        editor.commit();
    }

    public String getverify_licence_key() {
        return pref.getString(verify_licence_key, "0");
    }


    public void setUSerid(String DeviceIdd) {
        editor.putString(USerid, DeviceIdd);
        editor.commit();
    }

    public String getUSerid() {
        return pref.getString(USerid, "");
    }

    public Preferences(Context contx) {
        pref = contx.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void ClearPreference() {
        editor.clear();
        editor.commit();
    }


    public void setDeviceId(String DeviceIdd) {
        editor.putString(DeviceId, DeviceIdd);
        editor.commit();
    }

    public String getDeviceId() {
        return pref.getString(DeviceId, "");
    }

    public void setimei_number(String UUIDd) {
        editor.putString(imei_number, UUIDd);
        editor.commit();
    }

    public String getimei_number() {
        return pref.getString(imei_number, "");
    }


    public void setMASTERPASS(String MASTERPASSd) {
        editor.putString(MASTERPASS, MASTERPASSd);
        editor.commit();
    }

    public String getMASTERPASS() {
        return pref.getString(MASTERPASS, "");
    }



    public void setLICENCEKEY(String LICENCEKEYd) {
        editor.putString(LICENCEKEY, LICENCEKEYd);
        editor.commit();
    }

    public String getLICENCEKEY() {
        return pref.getString(LICENCEKEY, "");
    }



}
