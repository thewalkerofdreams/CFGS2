package es.iesnervion.yeray.pocketcharacters.DDBB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacterAndStat;

@Dao
public interface CharacterAndStatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCharacterAndStat(ClsCharacterAndStat characterAndStat);

    @Update
    void updateCharacterAndStat(ClsCharacterAndStat characterAndStat);

    @Delete
    void deleteCharacterAndStat(ClsCharacterAndStat characterAndStat);

    @Query("SELECT * FROM ClsCharacterAndStat")
    List<ClsCharacterAndStat> getAllCharactersAndStats();

    @Query("SELECT * FROM ClsCharacterAndStat WHERE idCharacter = :idCharacter")
    ClsCharacterAndStat getCharacterAndStat(String idCharacter);
}
