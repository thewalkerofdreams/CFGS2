package es.iesnervion.yeray.fragments_jugadores.DDBB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import es.iesnervion.yeray.fragments_jugadores.DDBB_Entities.ClsPlayer;
import es.iesnervion.yeray.fragments_jugadores.DDBB_Entities.ClsPlayerPosition;
import es.iesnervion.yeray.fragments_jugadores.DDBB_Entities.ClsPosition;

@Database(entities = {ClsPlayer.class, ClsPosition.class, ClsPlayerPosition.class}, version = 1, exportSchema = false)
public abstract class AppDataBase  extends RoomDatabase {

    private static AppDataBase INSTANCE;//Instancia de la base de datos

    public abstract ClsPlayer clsPlayer();
    public abstract ClsPosition clsPosition();
    public abstract ClsPlayerPosition clsPlayerPosition();

    public static AppDataBase getDataBase(final Context context){
        if(INSTANCE == null){
            synchronized (AppDataBase.class){ //Impide que 2 hilos entren de forma simultanea
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, "PlayerListApp").allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}