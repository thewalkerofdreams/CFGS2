package es.iesnervion.yeray.pocketcharacters.DDBB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsStat;

@Dao
public interface StatDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertStat(ClsStat stat);

    @Update
    void updateStat(ClsStat stat);

    @Delete
    void deleteStat(ClsStat stat);

    @Transaction
    @Query("SELECT * FROM ClsStat")
    List<ClsStat> getAllStats();

    @Transaction
    @Query("SELECT * FROM ClsStat WHERE name = :name")
    ClsStat getStat(String name);

    @Transaction
    @Query("SELECT * FROM ClsStat WHERE gameMode = :gameMode")
    List<ClsStat> getStatsByGameMode(String gameMode);

    @Transaction
    @Query("SELECT * FROM ClsStat WHERE gameMode = :gameMode AND name = :name")
    ClsStat getStatByGameModeAndName(String gameMode, String name);

    @Transaction
    @Query("SELECT * FROM (" +
            "SELECT ClsStat.id, ClsStat.name, ClsStat.gameMode FROM ClsStat " +
            "EXCEPT " +
            "SELECT ClsStat.id, ClsStat.name, ClsStat.gameMode FROM ClsStat " +
            "INNER JOIN ClsCharacterAndStat ON ClsStat.id = ClsCharacterAndStat.idStat " +
            "WHERE ClsCharacterAndStat.idCharacter = :characterId) WHERE gameMode = :gameMode")
    List<ClsStat> getStatsByGameModeAndWithoutCharacterId(String gameMode, int characterId);
}
