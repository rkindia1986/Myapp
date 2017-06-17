package com.sjm.myapp.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Helly-PC on 06/06/2017.
 */

public class RentRecordsList {
    String status = "";
    String message ="";
    @SerializedName("result")
    ArrayList<RentRecord> lstrentrecord;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<RentRecord> getLstrentrecord() {
        return lstrentrecord;
    }

    public void setLstrentrecord(ArrayList<RentRecord> lstrentrecord) {
        this.lstrentrecord = lstrentrecord;
    }
}
