package com.sjm.myapp;

/*Created by Android-1 on 3/24/2017.*/

public class Application extends android.app.Application{

    public static Preferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = new Preferences(getApplicationContext());
    }
}
