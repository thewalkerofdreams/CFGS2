package es.iesnervion.yeray.pocketcharacters.DDBB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacterAndStat;
import es.iesnervion.yeray.pocketcharacters.EntitiesModels.ClsStatModel;

@Dao
public interface CharacterAndStatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCharacterAndStat(ClsCharacterAndStat characterAndStat);

    @Update
    void updateCharacterAndStat(ClsCharacterAndStat characterAndStat);

    @Delete
    void deleteCharacterAndStat(ClsCharacterAndStat characterAndStat);

    @Transaction
    @Query("DELETE FROM ClsCharacterAndStat WHERE idCharacter = :characterId")
    void deleteCharacterAndStat(int characterId);

    @Transaction
    @Query("SELECT * FROM ClsCharacterAndStat")
    List<ClsCharacterAndStat> getAllCharactersAndStats();

    @Transaction
    @Query("SELECT * FROM ClsCharacterAndStat WHERE idCharacter = :idCharacter AND idStat = :idStat")
    ClsCharacterAndStat getCharacterAndStat(int idCharacter, int idStat);

    @Transaction
    @Query("SELECT * FROM ClsCharacterAndStat WHERE idStat = :idStat")
    List<ClsCharacterAndStat> getCharacterAndStat(int idStat);

    @Transaction
    @Query("SELECT ClsCharacterAndStat.idCharacter, ClsCharacterAndStat.idStat, ClsCharacterAndStat.value FROM ClsStat " +
            "INNER JOIN ClsCharacterAndStat ON ClsStat.id = ClsCharacterAndStat.idStat " +
            "WHERE ClsStat.name = :statName AND ClsCharacterAndStat.idCharacter = :idCharacter")
    ClsCharacterAndStat getCharacterAndStat(int idCharacter, String statName);

    @Transaction
    @Query("SELECT ClsStat.name, ClsCharacterAndStat.value FROM ClsStat " +
            "INNER JOIN ClsCharacterAndStat ON ClsStat.id = ClsCharacterAndStat.idStat " +
            "INNER JOIN ClsCharacter ON ClsCharacterAndStat.idCharacter = ClsCharacter.id " +
            "WHERE ClsCharacter.id = :idCharacter ORDER BY ClsStat.name")
    List<ClsStatModel> getStatsAndValueByCharacter(int idCharacter);
}
