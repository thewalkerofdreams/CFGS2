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

@Dao
public interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCharacter(ClsCharacter character);

    @Update
    void updateCharacter(ClsCharacter character);

    @Delete
    void deleteCharacter(ClsCharacter character);

    @Query("SELECT * FROM ClsCharacter")
    List<ClsCharacter> getAllCharacters();

    @Query("SELECT * FROM ClsCharacter WHERE characterName = :characterName")
    ClsCharacter getCharacter(String characterName);
}
