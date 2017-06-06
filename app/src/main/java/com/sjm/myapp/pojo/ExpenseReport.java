package com.sjm.myapp.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Helly-PC on 06/02/2017.
 */

public class ExpenseReport {
    int status=0;
    String message="";
    @SerializedName("result")
    List<Expense> lstExpense;

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

    public List<Expense> getLstExpense() {
        return lstExpense;
    }

    public void setLstExpense(List<Expense> lstExpense) {
        this.lstExpense = lstExpense;
    }
}
