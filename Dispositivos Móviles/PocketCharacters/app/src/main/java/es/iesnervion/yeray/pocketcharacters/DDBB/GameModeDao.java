package es.iesnervion.yeray.pocketcharacters.DDBB;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsGameMode;

public interface GameModeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGameMode(ClsGameMode gameMode);

    @Update
    void updateGameMode(ClsGameMode gameMode);

    @Delete
    void deleteGameMode(ClsGameMode gameMode);

    @Query("SELECT * FROM ClsGameMode")
    ArrayList<ClsGameMode> getAllGameModes();

    @Query("SELECT * FROM ClsGameMode WHERE name = :name")
    ClsGameMode getGameMode(String name);
}
