package com.sjm.myapp.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Helly-PC on 06/26/2017.
 */

public class PlanAlert {
    @SerializedName("customer_info")
    Customer customer;
    @SerializedName("installation_history")
    Installation_History installation_history;

    public Installation_History getInstallation_history() {
        return installation_history;
    }

    public void setInstallation_history(Installation_History installation_history) {
        this.installation_history = installation_history;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
