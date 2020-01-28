package es.iesnervion.yeray.pocketcharacters.DDBB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
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

    @Query("SELECT * FROM ClsStat")
    List<ClsStat> getAllStats();

    @Query("SELECT * FROM ClsStat WHERE name = :name")
    ClsStat getStat(String name);

    @Query("SELECT * FROM ClsStat WHERE gameMode = :gameMode")
    List<ClsStat> getStatsByGameMode(String gameMode);

    @Query("SELECT * FROM ClsStat WHERE gameMode = :gameMode AND name = :name")
    ClsStat getStatByGameModeAndName(String gameMode, String name);
}
