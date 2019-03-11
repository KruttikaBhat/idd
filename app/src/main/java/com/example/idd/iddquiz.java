package com.example.idd;

import android.app.Application;

import com.firebase.client.Firebase;

public class iddquiz extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
