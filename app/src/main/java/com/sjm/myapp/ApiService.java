package com.sjm.myapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {


    @GET("index.php")
    Call<String> SearchCustomer(@Query("method") String method, @Query("customer_name") String customer_name, @Query("customer_no") String customer_no, @Query("address") String address, @Query("city") String city, @Query("customer_connection_status") String customer_connection_status, @Query("stb_account_no") String stb_account_no, @Query("user_id") String user_id);

    @GET("index.php")
//add_customer
    Call<String> add_customer(@Query("method") String method, @Query("name") String name, @Query("customer_no") String customer_no, @Query("address") String address, @Query("city") String city, @Query("amount") String amount, @Query("phone") String phone, @Query("rent_amount") String rent_amount, @Query("stb_account_no_1") String stb_account_no_1, @Query("nu_id_no_1") String nu_id_no_1, @Query("caf_no_1") String caf_no_1, @Query("stb_account_no_2") String stb_account_no_2, @Query("nu_id_no_2") String nu_id_no_2, @Query("caf_no_2") String caf_no_2, @Query("connection_type") String connection_type, @Query("customer_connection_status") String customer_connection_status, @Query("rent_start_date") String rent_start_date, @Query("no_of_month") String no_of_month, @Query("user_id") String user_id);
    //http://                  method=add_customer             &name=nilesh_edit&customer_no=7878&address=ahm_edit&                                                   city=rjt_eit&amount=2501&phone=556141&rent_amount=12151&                                                                                 stb_account_no_1=12&nu_id_no_1=123&caf_no_1=34&stb_account_no_2=45&nu_id_no_2=65&caf_no_2=67&connection_type=manual&customer_connection_status=on&rent_start_date=2017-05-06&no_of_month=8&user_id=1

    @GET("index.php")
//delete_customer
    Call<String> DeleteCustomer(@Query("method") String method, @Query("customer_id") String customer_id);
    //http://eserviceinfo.in/webzine/index.php?method=delete_customer&customer_id=4


    @GET("index.php")
//edit_customer
    Call<String> Edit_customer(@Query("method") String method, @Query("name") String name, @Query("customer_no") String customer_no, @Query("address") String address, @Query("city") String city, @Query("amount") String amount, @Query("phone") String phone, @Query("rent_amount") String rent_amount, @Query("stb_account_no_1") String stb_account_no_1, @Query("nu_id_no_1") String nu_id_no_1, @Query("caf_no_1") String caf_no_1, @Query("stb_account_no_2") String stb_account_no_2, @Query("nu_id_no_2") String nu_id_no_2, @Query("caf_no_2") String caf_no_2, @Query("connection_type") String connection_type, @Query("customer_connection_status") String customer_connection_status, @Query("rent_start_date") String rent_start_date, @Query("no_of_month") String no_of_month, @Query("user_id") String user_id);


    @GET("index.php")
//update_customer_connection_status
    Call<String> update_customer_connection_status(@Query("method") String method, @Query("customer_id") String customer_id, @Query("user_id") String user_id, @Query("customer_connection_status") String customer_connection_status);
    //http://eserviceinfo.in/webzine/index.php?method=update_customer_connection_status&customer_id=5&user_id=1&customer_connection_status=on

    @GET("index.php")
//get_city_list
    Call<String> get_city_list(@Query("method") String method);


    @GET("index.php")
//add_customer_payment_amount
    Call<String> add_customer_payment_amount(@Query("method") String method, @Query("customer_id") String customer_id, @Query("user_id") String user_id, @Query("payment_amount") String payment_amount);
    //http://eserviceinfo.in/webzine/index.php?method=add_customer_payment_amount&customer_id=6&user_id=1&payment_amount=150

    @GET("index.php")
//rent_record_by_customer_id
    Call<String> rent_record_by_customer_id(@Query("method") String method, @Query("user_id") String user_id);


    @GET("index.php")
//payment_record
    Call<String> payment_record(@Query("method") String method, @Query("user_id") String user_id);
    //http://eserviceinfo.in/webzine/index.php?method=add_customer_payment_amount&customer_id=6&user_id=1&payment_amount=150

    @GET("index.php")
//get_detail_by_customer_no
    Call<String> get_detail_by_customer_no(@Query("method") String method, @Query("customer_no") String customer_no);
    //http://eserviceinfo.in/webzine/index.php?method=get_detail_by_customer_no&customer_no=78

    @GET("index.php")
//get_customer_detail
    Call<String> get_customer_detail(@Query("method") String method, @Query("customer_id") String customer_id);
    //http://eserviceinfo.in/webzine/index.php?method=get_customer_detail&customer_id=5

    @GET("index.php")
//add_expense_income
    Call<String> add_expense_income(@Query("method") String method, @Query("expense_type") String expense_type, @Query("description") String description, @Query("amount") String amount, @Query("expense_date") String expense_date, @Query("user_id") String user_id);
    //http://eserviceinfo.in/webzine/index.php?method=add_expense_income&expense_type=expense&description=nilesh%20descirpiton&amount=400&user_id=1&expense_date=2017-02-01


    @GET("index.php")
//get_expense_report
    Call<String> get_expense_report(@Query("method") String method, @Query("expense_type") String expense_type, @Query("start_date") String start_date, @Query("end_date") String end_date);


    @GET("index.php")
//update_new_month_rent
    Call<String> update_new_month_rent(@Query("method") String method, @Query("rent_amount") String rent_amount, @Query("city") String city, @Query("user_id") String user_id);


    @GET("index.php")
//expiring_customers_list
    Call<String> expiring_customers_list(@Query("method") String method);


    @GET("index.php")
//add_installation_history
    Call<String> add_installation_history(@Query("method") String method, @Query("cable_operator_name") String cable_operator_name, @Query("cable_operator_contact_no") String cable_operator_contact_no, @Query("cable_network_name") String cable_network_name, @Query("operator_address") String operator_address, @Query("website_link") String website_link, @Query("website_login_id") String website_login_id, @Query("website_login_password") String website_login_pwd, @Query("master_password") String master_pwd, @Query("licence_key") String licence_key, @Query("device_id") String device_id);

    @GET("index.php")
//update_installation_history
    Call<String> update_installation_history(@Query("method") String method, @Query("cable_operator_name") String cable_operator_name, @Query("cable_operator_contact_no") String cable_operator_contact_no, @Query("cable_network_name") String cable_network_name, @Query("operator_address") String operator_address, @Query("website_link") String website_link, @Query("website_login_id") String website_login_id, @Query("website_login_password") String website_login_pwd, @Query("master_password") String master_pwd, @Query("licence_key") String licence_key, @Query("device_id") String device_id);

    @GET("index.php")
//get_installation_history
    Call<String> get_installation_history(@Query("method") String method, @Query("licence_key") String licence_key);

    @GET("index.php")
//send_sms_by_customer
    Call<String> send_sms_by_customer(@Query("method") String method, @Query("licence_key") String licence_key, @Query("customer_id") String customer_id, @Query("sms_status") String sms_status);

    @GET("index.php")
//get_rent_report
    Call<String> get_rent_report(@Query("method") String method, @Query("rent_start_date") String start_date, @Query("rent_end_date") String end_date, @Query("customer_connection_status") String customer_connection_status, @Query("payment_status") String payment_status, @Query("city") String city);

    @GET("index.php")
//expiring_customers_list
    Call<String> get_expcust(@Query("method") String method);

    @GET("index.php")
//expiring_customers_list
    Call<String> welcome(@Query("method") String method, @Query("device_id") String device_id, @Query("imei_number") String imei_number);

    @GET("index.php")
//send_multiple_sms
    Call<String> send_multiple_sms(@Query("method") String method, @Query("payment_status") String payment_status, @Query("city") String city, @Query("user_id") String user_id);

    @GET("index.php")
//send_multiple_sms
    Call<String> send_multiple_Customsms(@Query("method") String method, @Query("payment_status") String payment_status, @Query("city") String city, @Query("user_id") String user_id, @Query("message") String message);

    @GET("index.php")
//change_master_password
    Call<String> change_master_password(@Query("method") String method, @Query("licence_key") String licence_key, @Query("device_id") String device_id, @Query("old_password") String old_password, @Query("new_password") String new_password, @Query("confirm_password") String confirm_password);

    @GET("index.php")
    //update_website_link
    Call<String> update_website_link(@Query("method") String method, @Query("licence_key") String licence_key, @Query("device_id") String device_id, @Query("website_link") String website_link);

    @GET("index.php")
        //rent_record_by_customer
    //http://surfinternet.in/webservice/index.php?method=rent_record_by_customer&rent_start_date=2017-05-09&rent_end_date=2017-08-06&customer_id=7&user_id=2&rent_amount=210&rent_status=DUE
    Call<String> insertRentRecord(@Query("method") String method, @Query("rent_start_date") String rentstartdate, @Query("rent_end_date") String rent_end_date, @Query("customer_id") String customer_id, @Query("user_id") String user_id, @Query("rent_amount") String rent_amount, @Query("rent_status") String rent_status);

}

