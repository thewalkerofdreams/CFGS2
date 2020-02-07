package es.iesnervion.yeray.pocketcharacters.DDBB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsGameMode;

@Dao
public interface GameModeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGameMode(ClsGameMode gameMode);

    @Update
    void updateGameMode(ClsGameMode gameMode);

    @Delete
    void deleteGameMode(ClsGameMode gameMode);

    @Transaction
    @Query("SELECT * FROM ClsGameMode")
    List<ClsGameMode> getAllGameModes();

    @Transaction
    @Query("SELECT * FROM ClsGameMode WHERE name = :name")
    ClsGameMode getGameMode(String name);
}
