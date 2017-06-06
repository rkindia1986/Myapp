package com.sjm.myapp.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Helly-PC on 06/02/2017.
 */

public class InstallationHistory
{
    int status=0;
    String message="";
    @SerializedName("result")
    List<Installation> lstInstallation;
}
