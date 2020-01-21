package es.iesnervion.yeray.pocketcharacters.DDBB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import es.iesnervion.yeray.pocketcharacters.Entities.ClsCharacter;
import es.iesnervion.yeray.pocketcharacters.Entities.ClsGameMode;
import es.iesnervion.yeray.pocketcharacters.Entities.ClsObjectAndQuantity;
import es.iesnervion.yeray.pocketcharacters.Entities.ClsStat;

@Database(entities = {ClsCharacter.class, ClsGameMode.class, ClsObjectAndQuantity.class, ClsStat.class}, version = 1)
public abstract class AppDataBase  extends RoomDatabase {

    private static AppDataBase INSTANCE;//Instancia de la base de datos

    public abstract CharacterDao characterDao();
    public abstract GameModeDao gameModeDao();
    public abstract ObjectAndQuantityDao objectAndQuantityDao();
    public abstract StatDao statDao();

    public static AppDataBase getDataBase(final Context context){
        if(INSTANCE == null){
            synchronized (AppDataBase.class){ //Impide que 2 hilos entren de forma simultanea
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, "PocketCharacterDDBB").build();
                }
            }
        }
        return INSTANCE;
    }
}