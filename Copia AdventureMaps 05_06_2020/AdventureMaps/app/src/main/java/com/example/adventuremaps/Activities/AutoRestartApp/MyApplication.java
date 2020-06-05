package com.example.adventuremaps.Activities.AutoRestartApp;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    public static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;//Almacenamos la instancia de la app
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    public static MyApplication getInstance() {//Obtenemos la instancia de la app
        return instance;
    }
}