package es.iesnervion.yeray.pocketcharacters.DDBB;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacterAndStat;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObjectAndCharacter;

public interface ObjectAndCharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertObjectAndCharacter(ClsObjectAndCharacter objectAndCharacter);

    @Update
    void updateObjectAndCharacter(ClsObjectAndCharacter objectAndCharacter);

    @Delete
    void deleteObjectAndCharacter(ClsObjectAndCharacter objectAndCharacter);

    @Query("SELECT * FROM ClsObjectAndCharacter")
    ArrayList<ClsObjectAndCharacter> getAllObjectsAndCharacters();

    @Query("SELECT * FROM ClsObjectAndCharacter WHERE idCharacter = :idCharacter")
    ClsObjectAndCharacter getObjectAndCharacter(String idCharacter);
}
