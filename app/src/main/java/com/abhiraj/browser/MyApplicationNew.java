package com.abhiraj.browser;

import android.app.Application;


import io.realm.Realm;

public class MyApplicationNew extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

    }
}
