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
                customer.setIsSync(cursor.getInt(cursor.getColumnIndex(CUST_SYNC)));
                cursor.moveToNext();
                slist.add(customer);
            }
            cursor.close();
            db.close();
        }
        return slist;
    }

    public void UpdateCustomer(Customer customer) {

        Gson gson = new GsonBuilder().create();
        try {
            JSONObject jsonObject = new JSONObject(gson.toJson(customer));
            SQLiteDatabase database = this.getWritableDatabase();
            database.execSQL("delete from customer where cust_id='" + customer.getId() + "'");
            ContentValues values = new ContentValues();
            values.put(CUST_DETAILS, jsonObject.toString());
            values.put(CUST_SYNC, customer.getIsSync());
            values.put(CUST_ID, customer.getId());
            database.insert("customer", null, values);
            database.close();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    // Getting single contact
    public ArrayList<String> Get_AllCity() {
        String query ="select * from city";
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
            database.execSQL("delete from city where details='" + slist + "'");
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
