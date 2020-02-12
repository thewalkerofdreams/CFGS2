package com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by pjarana on 21/02/18.
 */
@android.arch.persistence.room.Database(entities = {Futbolista.class,FutbolistaPosicion.class,Posicion.class},version = 1)
public abstract class Database extends RoomDatabase {

    public static Database INSTANCE;
    public abstract MyDAO getEquipoDao();


    public static Database getDatabase(final Context context)
    {
        if(INSTANCE==null)
        {
            synchronized (Database.class)
            {
                if(INSTANCE==null)
                {
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(),Database.class,"Equipo").allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}
