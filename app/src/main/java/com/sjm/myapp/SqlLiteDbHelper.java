package com.sjm.myapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sjm.myapp.pojo.Customer;
import com.sjm.myapp.pojo.SearchCustomer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class SqlLiteDbHelper extends SQLiteOpenHelper {

    // All Static variables
// Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "cable.sqlite";
    private static final String DB_PATH_SUFFIX = "/databases/";
    static Context ctx;
    public static String CUST_SYNC = "sync";
    public static String CUST_ID = "cust_id";
    public static String CUST_DETAILS = "details";


    public SqlLiteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx = context;
    }

    // Getting single contact
    public ArrayList<Customer> Get_AllCustomers(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Customer> slist = new ArrayList<Customer>();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                Gson gson = new GsonBuilder().create();
                Customer customer = gson.fromJson(cursor.getString(cursor.getColumnIndex(CUST_DETAILS)), Customer.class);
                customer.setSync(cursor.getString(cursor.getColumnIndex(CUST_SYNC)));
                cursor.moveToNext();
                slist.add(customer);
            }
            cursor.close();
            db.close();
        }
        return slist;
    }

    // Getting single contact
    public ArrayList<Customer> Get_AllCustomers2(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Customer> slist = new ArrayList<Customer>();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Customer customer = new Customer();
                customer.setSync(cursor.getString(cursor.getColumnIndex(CUST_SYNC)));
                customer.setId(cursor.getString(cursor.getColumnIndex("id")));
                customer.setSyncid(cursor.getString(cursor.getColumnIndex("syncid")));
                customer.setCustomer_no(cursor.getString(cursor.getColumnIndex("customer_no")));
                customer.setName(cursor.getString(cursor.getColumnIndex("name")));
                customer.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                customer.setCity(cursor.getString(cursor.getColumnIndex("city")));
                customer.setAmount(cursor.getString(cursor.getColumnIndex("amount")));
                customer.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                customer.setRent_amount(cursor.getString(cursor.getColumnIndex("rent_amount")));
                customer.setCaf_no_1(cursor.getString(cursor.getColumnIndex("caf_no_1")));
                customer.setCaf_no_2(cursor.getString(cursor.getColumnIndex("caf_no_2")));
                customer.setStb_account_no_1(cursor.getString(cursor.getColumnIndex("stb_account_no_1")));
                customer.setStb_account_no_2(cursor.getString(cursor.getColumnIndex("stb_account_no_2")));
                customer.setNu_id_no_1(cursor.getString(cursor.getColumnIndex("nu_id_no_1")));
                customer.setNu_id_no_2(cursor.getString(cursor.getColumnIndex("nu_id_no_2")));

                customer.setConnection_type(cursor.getString(cursor.getColumnIndex("connection_type")));

                customer.setCustomer_connection_status(cursor.getString(cursor.getColumnIndex("customer_connection_status")));

                customer.setRent_start_date(cursor.getString(cursor.getColumnIndex("rent_start_date")));
                customer.setRent_end_date(cursor.getString(cursor.getColumnIndex("rent_end_date")));
                customer.setNo_of_month(cursor.getString(cursor.getColumnIndex("no_of_month")));

                customer.setUpdated_by(cursor.getString(cursor.getColumnIndex("updated_by")));
                customer.setUpdated_at(cursor.getString(cursor.getColumnIndex("updated_at")));
                customer.setCreated_at(cursor.getString(cursor.getColumnIndex("created_at")));
                customer.setCreated_by(cursor.getString(cursor.getColumnIndex("created_by")));
                slist.add(customer);
            } while (cursor.moveToNext());

            cursor.close();
            db.close();
        }
        return slist;
    }

    public Customer Get_Customers(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Customer customer = null;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                customer = new Customer();
                customer.setSync(cursor.getString(cursor.getColumnIndex(CUST_SYNC)));
                customer.setId(cursor.getString(cursor.getColumnIndex("id")));
                customer.setSyncid(cursor.getString(cursor.getColumnIndex("syncid")));
                customer.setCustomer_no(cursor.getString(cursor.getColumnIndex("customer_no")));
                customer.setName(cursor.getString(cursor.getColumnIndex("name")));
                customer.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                customer.setCity(cursor.getString(cursor.getColumnIndex("city")));
                customer.setAmount(cursor.getString(cursor.getColumnIndex("amount")));
                customer.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                customer.setRent_amount(cursor.getString(cursor.getColumnIndex("rent_amount")));
                customer.setCaf_no_1(cursor.getString(cursor.getColumnIndex("caf_no_1")));
                customer.setCaf_no_2(cursor.getString(cursor.getColumnIndex("caf_no_2")));
                customer.setStb_account_no_1(cursor.getString(cursor.getColumnIndex("stb_account_no_1")));
                customer.setStb_account_no_2(cursor.getString(cursor.getColumnIndex("stb_account_no_2")));
                customer.setNu_id_no_1(cursor.getString(cursor.getColumnIndex("nu_id_no_1")));
                customer.setNu_id_no_2(cursor.getString(cursor.getColumnIndex("nu_id_no_2")));

                customer.setConnection_type(cursor.getString(cursor.getColumnIndex("connection_type")));

                customer.setCustomer_connection_status(cursor.getString(cursor.getColumnIndex("customer_connection_status")));

                customer.setRent_start_date(cursor.getString(cursor.getColumnIndex("rent_start_date")));
                customer.setRent_end_date(cursor.getString(cursor.getColumnIndex("rent_end_date")));
                customer.setNo_of_month(cursor.getString(cursor.getColumnIndex("no_of_month")));

                customer.setUpdated_by(cursor.getString(cursor.getColumnIndex("updated_by")));
                customer.setUpdated_at(cursor.getString(cursor.getColumnIndex("updated_at")));
                customer.setCreated_at(cursor.getString(cursor.getColumnIndex("created_at")));
                customer.setCreated_by(cursor.getString(cursor.getColumnIndex("created_by")));

            } while (cursor.moveToNext());

            cursor.close();
            db.close();
        }
        return customer;
    }

    public boolean checkCustomer(Customer customer) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor c = database.rawQuery("select * from Customer_Master where customer_no='" + customer.getCustomer_no() + "'", null);
        if (c != null) {
            if (c.getCount() > 0) {
                return true;
            }
        }
        database.close();
        return false;
    }

    public void UpdateCustomerConnection(String custno, String ConnStatus) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("Update Customer_Master set customer_connection_status='" + ConnStatus + "',sync='0' where customer_no='" + custno + "'");
        database.close();
    }

    public void DeleteCustomer(String custno) {
        SQLiteDatabase database = this.getWritableDatabase();

        database.execSQL("delete from Customer_Master where customer_no='" + custno + "'");
        database.close();
        //TODO delete relevent table data also
    }

    public void UpdateCustomer(Customer customer) {
        SQLiteDatabase database = this.getWritableDatabase();
        if (!customer.getCustomer_no().equalsIgnoreCase("0"))
            database.execSQL("delete from Customer_Master where customer_no='" + customer.getCustomer_no() + "'");
        ContentValues values = new ContentValues();

        values.put("sync", customer.getSync());
        values.put("id", customer.getId());
        values.put("syncid", customer.getSyncid());
        values.put("customer_no", customer.getCustomer_no());
        values.put("name", customer.getName());
        values.put("address", customer.getAddress());
        values.put("city", customer.getCity());
        values.put("amount", customer.getAmount());
        values.put("phone", customer.getPhone());
        values.put("rent_amount", customer.getRent_amount());
        values.put("stb_account_no_1", customer.getStb_account_no_1());
        values.put("nu_id_no_1", customer.getNu_id_no_1());
        values.put("caf_no_1", customer.getCaf_no_1());
        values.put("stb_account_no_2", customer.getStb_account_no_2());
        values.put("nu_id_no_2", customer.getNu_id_no_2());
        values.put("caf_no_2", customer.getCaf_no_2());
        values.put("connection_type", customer.getConnection_type());
        values.put("customer_connection_status", customer.getCustomer_connection_status());
        values.put("rent_start_date", customer.getRent_start_date());
        values.put("no_of_month", customer.getNo_of_month());
        values.put("updated_by", customer.getUpdated_by());
        values.put("updated_at", customer.getUpdated_at());
        values.put("created_by", customer.getCreated_by());
        values.put("created_at", customer.getCreated_at());
        values.put("rent_end_date", customer.getRent_end_date());
        database.insert("Customer_Master", null, values);
        database.close();
    }


    public void InsertDeleted(String custid) {
        SQLiteDatabase database = this.getWritableDatabase();
        if (!custid.contains("temp")) {
            database.execSQL("delete from Deleted where id='" + custid + "'");
            ContentValues values = new ContentValues();
            values.put("id", custid);
            database.insert("Deleted", null, values);
            database.close();
        } else {


        }
    }

    public void Deleted(String custid) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("delete from Deleted where id='" + custid + "'");
        database.close();

    }

    public void UpdateCustomer(Customer customer, String tempid) {


        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("sync", customer.getSync());
        values.put("id", customer.getId());
        values.put("syncid", customer.getSyncid());
        values.put("customer_no", customer.getCustomer_no());
        values.put("name", customer.getName());
        values.put("address", customer.getAddress());
        values.put("city", customer.getCity());
        values.put("amount", customer.getAmount());
        values.put("phone", customer.getPhone());
        values.put("rent_amount", customer.getRent_amount());
        values.put("stb_account_no_1", customer.getStb_account_no_1());
        values.put("nu_id_no_1", customer.getNu_id_no_1());
        values.put("caf_no_1", customer.getCaf_no_1());
        values.put("stb_account_no_2", customer.getStb_account_no_2());
        values.put("nu_id_no_2", customer.getNu_id_no_2());
        values.put("caf_no_2", customer.getCaf_no_2());
        values.put("connection_type", customer.getConnection_type());
        values.put("customer_connection_status", customer.getCustomer_connection_status());
        values.put("rent_start_date", customer.getRent_start_date());
        values.put("no_of_month", customer.getNo_of_month());
        values.put("updated_by", customer.getUpdated_by());
        values.put("updated_at", customer.getUpdated_at());
        values.put("created_by", customer.getCreated_by());
        values.put("created_at", customer.getCreated_at());
        values.put("rent_end_date", customer.getRent_end_date());
        database.execSQL("delete from Customer_Master where syncid like'" + tempid + "'");
        database.insert("Customer_Master", null, values);
        database.close();
    }

    // Getting single contact
    public ArrayList<String> Get_AllCity() {
        String query = "select * from city";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> slist = new ArrayList<String>();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                slist.add(cursor.getString(cursor.getColumnIndex(CUST_DETAILS)));
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
        }

        return slist;
    }


    public void UpdateCity(ArrayList<String> slist) {

        SQLiteDatabase database = this.getWritableDatabase();
        for (int i = 0; i < slist.size(); i++) {
            database.execSQL("delete from city where details like'" + slist.get(i) + "'");
            ContentValues values = new ContentValues();
            values.put(CUST_DETAILS, slist.get(i));

            database.insert("city", null, values);
        }
        database.close();


    }


    public void CopyDataBaseFromAsset() throws IOException {

        InputStream myInput = ctx.getAssets().open(DATABASE_NAME);

// Path to the just created empty db
        String outFileName = getDatabasePath();

// if the path doesn't exist first, create it
        File f = new File(ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
        if (!f.exists())
            f.mkdir();

// Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

// transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

// Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    private static String getDatabasePath() {
        return ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX
                + DATABASE_NAME;
    }

    public SQLiteDatabase openDataBase() throws SQLException {
        File dbFile = ctx.getDatabasePath(DATABASE_NAME);

        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                System.out.println("Copying sucess from Assets folder");
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }

        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// TODO Auto-generated method stub

    }
}
