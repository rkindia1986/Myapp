package com.sjm.myapp.pojo;

/**
 * Created by Helly-PC on 06/14/2017.
 */

public class RentRecord {
    String id="";
    String customer_id="";
    String payment_status="";
    String payment_amount="";
    String  rent_start_date="";
    String  rent_end_date="";
    String created_at="";
    String  updated_at="";
    String  created_by="";
    String  updated_by="";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(String payment_amount) {
        this.payment_amount = payment_amount;
    }

    public String getRent_start_date() {
        return rent_start_date;
    }

    public void setRent_start_date(String rent_start_date) {
        this.rent_start_date = rent_start_date;
    }

    public String getRent_end_date() {
        return rent_end_date;
    }

    public void setRent_end_date(String rent_end_date) {
        this.rent_end_date = rent_end_date;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }
}
