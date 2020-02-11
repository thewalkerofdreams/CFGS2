package com.example.listadopersonas_va.DDBB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.listadopersonas_va.DDBB_Entities.ClsPersona;

@Database(entities = {ClsPersona.class}, version = 1, exportSchema = false)
public abstract class AppDataBase  extends RoomDatabase {

    private static AppDataBase INSTANCE;//Instancia de la base de datos

    public abstract ClsPersonaDao clsPersonaDao();

    public static AppDataBase getDataBase(final Context context){
        if(INSTANCE == null){
            synchronized (AppDataBase.class){ //Impide que 2 hilos entren de forma simultanea
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, "PersonListApp").allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}