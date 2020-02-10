package com.example.listado_empleados.DDBB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.listado_empleados.DDBB_Entities.ClsDepartamento;
import com.example.listado_empleados.DDBB_Entities.ClsPersona;

@Database(entities = {ClsPersona.class, ClsDepartamento.class}, version = 1, exportSchema = false)
public abstract class AppDataBase  extends RoomDatabase {

    private static AppDataBase INSTANCE;//Instancia de la base de datos

    public abstract ClsPersonaDao clsPersonaDao();
    public abstract ClsDepartamentoDao clsDepartamentoDao();

    public static AppDataBase getDataBase(final Context context){
        if(INSTANCE == null){
            synchronized (AppDataBase.class){ //Impide que 2 hilos entren de forma simultanea
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, "EmployeeListApp").allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}