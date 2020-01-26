package es.iesnervion.yeray.pocketcharacters.DDBB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObject;

@Dao
public interface ObjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertObject(ClsObject object);

    @Update
    void updateObject(ClsObject object);

    @Delete
    void deleteObject(ClsObject object);

    @Query("SELECT * FROM ClsObject")
    List<ClsObject> getAllObjects();

    @Query("SELECT * FROM ClsObject WHERE name = :name")
    ClsObject getObject(String name);

    @Query("SELECT * FROM ClsObject WHERE gameMode = :gameMode")
    List<ClsObject> getObjectsByGameMode(String gameMode);

    @Query("SELECT * FROM ClsObject WHERE gameMode = :gameMode AND name = :name")
    ClsObject getObjectByGameModeAndName(String gameMode, String name);

    @Query("SELECT * FROM ClsObject WHERE gameMode = :gameMode AND type = :type AND name = :name")
    ClsObject getObjectByGameModeObjectNameAndType(String gameMode, String type, String name);
}
