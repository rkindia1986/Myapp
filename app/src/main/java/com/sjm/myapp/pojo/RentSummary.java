package com.sjm.myapp.pojo;

/**
 * Created by Helly-PC on 06/27/2017.
 */

public class RentSummary {

    String total_based_on_total;
    String rent_from_date;
    String rent_end_date;

    public String getTotal_based_on_total() {
        return total_based_on_total;
    }

    public void setTotal_based_on_total(String total_based_on_total) {
        this.total_based_on_total = total_based_on_total;
    }

    public String getRent_from_date() {
        return rent_from_date;
    }

    public void setRent_from_date(String rent_from_date) {
        this.rent_from_date = rent_from_date;
    }

    public String getRent_end_date() {
        return rent_end_date;
    }

    public void setRent_end_date(String rent_end_date) {
        this.rent_end_date = rent_end_date;
    }
}
