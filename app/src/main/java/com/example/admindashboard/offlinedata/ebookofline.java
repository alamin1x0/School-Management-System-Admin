package com.example.admindashboard.offlinedata;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class ebookofline  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
