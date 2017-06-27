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
    public static final String UUID = "UUID";
    public static final String LICENCEKEY = "LICENCEKEY";
    public static final String MASTERPASS = "MASTERPASS";

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

    public void setUUID(String UUIDd) {
        editor.putString(UUID, UUIDd);
        editor.commit();
    }

    public String getUUID() {
        return pref.getString(UUID, "");
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
