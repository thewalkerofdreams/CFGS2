package es.iesnervion.yeray.pocketcharacters.DDBB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacterAndStat;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsGameMode;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObject;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObjectAndCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObjectType;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsStat;

@Database(entities = {ClsCharacter.class, ClsGameMode.class, ClsObject.class, ClsObjectType.class, ClsStat.class, ClsCharacterAndStat.class,
ClsObjectAndCharacter.class}, version = 1, exportSchema = false)
public abstract class AppDataBase  extends RoomDatabase {

    private static AppDataBase INSTANCE;//Instancia de la base de datos

    public abstract CharacterDao characterDao();
    public abstract GameModeDao gameModeDao();
    public abstract ObjectDao objectDao();
    public abstract ObjectTypeDao objectTypeDao();
    public abstract StatDao statDao();
    public abstract ObjectAndCharacterDao objectAndCharacterDao();
    public abstract CharacterAndStatDao characterAndStatDao();

    public static AppDataBase getDataBase(final Context context){
        if(INSTANCE == null){
            synchronized (AppDataBase.class){ //Impide que 2 hilos entren de forma simultanea
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, "PocketCharacterDDBB").allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}