package com.sjm.myapp.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Helly-PC on 06/01/2017.
 */

public class SearchCustomer {
    int status=0;
    String message="";
    @SerializedName("result")
    List<Customer> lstCustomer;
    String totalRecord="";

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Customer> getLstCustomer() {
        return lstCustomer;
    }

    public void setLstCustomer(ArrayList<Customer> lstCustomer) {
        this.lstCustomer = lstCustomer;
    }

    public String getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(String totalRecord) {
        this.totalRecord = totalRecord;
    }
}
