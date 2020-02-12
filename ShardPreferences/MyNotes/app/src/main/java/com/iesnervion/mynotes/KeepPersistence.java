package com.iesnervion.mynotes;

import com.google.firebase.database.FirebaseDatabase;

public class KeepPersistence extends android.app.Application{

    @Override
    public void onCreate(){
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
