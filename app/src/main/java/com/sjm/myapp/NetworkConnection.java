package com.sjm.myapp;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Jitesh Dalsaniya on 03-Oct-15.
 */
public class NetworkConnection {

    public static boolean isNetworkAvailable(Context context){
        return ((ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}
