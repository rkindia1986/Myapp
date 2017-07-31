package com.sjm.myapp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Helly-PC on 06/01/2017.
 */

public class Utils {

    public static  void ShowMessageDialog(Context context ,String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(R.string.app_name);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public static String gettime(int hour, int min,int sec) {
       String s = new StringBuilder().append(String.format("%02d", hour)).append(":")
                .append(String.format("%02d", min)).append(":").append(String.format("%02d", sec)).toString();
        return s;
    }
    public static String getDatetime(String date,int hour, int min,int sec) {
        String s = new StringBuilder().append(date).append(" ").append(String.format("%02d", hour)).append(":")
                .append(String.format("%02d", min)).append(":").append(String.format("%02d", sec)).toString() ;
        return s;
    }
    public static String getDatetime(int y,int m,int d,int hour, int min,int sec) {
        String s = new StringBuilder().append(String.format("%04d", y)).append("-")
                .append(String.format("%02d", m)).append("-").append(String.format("%02d", d)).append(" ").append(String.format("%02d", hour)).append(":")
                .append(String.format("%02d", min)).append(":").append(String.format("%02d", sec)).toString() ;
        return s;
    }
    public static String getDate(int d,int m,int y) {
        String s = new StringBuilder().append(String.format("%04d", y)).append("-")
                .append(String.format("%02d", m)).append("-").append(String.format("%02d", d)).toString() ;
        return s;
    }


}
