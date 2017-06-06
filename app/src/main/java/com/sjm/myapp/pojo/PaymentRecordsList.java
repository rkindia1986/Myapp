package com.sjm.myapp.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Helly-PC on 06/06/2017.
 */

public class PaymentRecordsList {
    String status = "";
    String message ="";
    @SerializedName("result")
    List<PaymentRecord> lstPaymentrecords;

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

    public List<PaymentRecord> getLstPaymentrecords() {
        return lstPaymentrecords;
    }

    public void setLstPaymentrecords(List<PaymentRecord> lstPaymentrecords) {
        this.lstPaymentrecords = lstPaymentrecords;
    }
}
