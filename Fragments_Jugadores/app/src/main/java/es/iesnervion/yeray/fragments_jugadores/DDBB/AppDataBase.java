package es.iesnervion.yeray.fragments_jugadores.DDBB;

import android.content.ContentValues;
import android.content.Context;

import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import es.iesnervion.yeray.fragments_jugadores.DDBB_Entities.ClsPlayer;
import es.iesnervion.yeray.fragments_jugadores.DDBB_Entities.ClsPlayerPosition;
import es.iesnervion.yeray.fragments_jugadores.DDBB_Entities.ClsPosition;

@Database(entities = {ClsPlayer.class, ClsPosition.class, ClsPlayerPosition.class}, version = 1, exportSchema = false)
public abstract class AppDataBase  extends RoomDatabase {

    private static AppDataBase INSTANCE;//Instancia de la base de datos

    public abstract ClsPlayerDao clsPlayerDao();
    public abstract ClsPositionDao clsPositionDao();
    public abstract ClsPlayerPositionDao clsPlayerPositionDao();

    public static AppDataBase getDataBase(final Context context){
        if(INSTANCE == null){
            synchronized (AppDataBase.class){ //Impide que 2 hilos entren de forma simultanea
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, "PlayerListApp").allowMainThreadQueries().addCallback(rdc).build();
                }
            }
        }
        return INSTANCE;
    }

    static RoomDatabase.Callback rdc = new RoomDatabase.Callback(){
        public void onCreate (SupportSQLiteDatabase db){
            ContentValues contentValues = new ContentValues();
            contentValues.put("nombrePosicion", "portero");
            db.insert("ClsPosition", OnConflictStrategy.IGNORE, contentValues);

            contentValues = new ContentValues();
            contentValues.put("nombrePosicion", "delantero");
            db.insert("ClsPosition", OnConflictStrategy.IGNORE, contentValues);

            contentValues = new ContentValues();
            contentValues.put("nombrePosicion", "centrocampista");
            db.insert("ClsPosition", OnConflictStrategy.IGNORE, contentValues);

            contentValues = new ContentValues();
            contentValues.put("nombrePosicion", "defensa");
            db.insert("ClsPosition", OnConflictStrategy.IGNORE, contentValues);
            //Log.d(“db create “,”table created when db created first time in  onCreate”);
        }
        public void onOpen (SupportSQLiteDatabase db){
            //ContentValues contentValues = new ContentValues();
            //contentValues.put(“open_time”, date);
            //db.insert(“dbusage”, OnConflictStrategy.IGNORE, contentValues);
            //Log.d(“db open “,”adding db open date record”);
        }
    };
}