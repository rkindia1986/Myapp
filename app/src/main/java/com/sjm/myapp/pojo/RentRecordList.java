package com.sjm.myapp.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Helly-PC on 06/27/2017.
 */

public class RentRecordList {
    @SerializedName("rent_summmary")
    RentSummary rentSummary;

    @SerializedName("rent_record")
    ArrayList<RentRecord> rentRecords;

    public RentSummary getRentSummary() {
        return rentSummary;
    }

    public void setRentSummary(RentSummary rentSummary) {
        this.rentSummary = rentSummary;
    }

    public ArrayList<RentRecord> getRentRecords() {
        return rentRecords;
    }

    public void setRentRecords(ArrayList<RentRecord> rentRecords) {
        this.rentRecords = rentRecords;
    }
}
