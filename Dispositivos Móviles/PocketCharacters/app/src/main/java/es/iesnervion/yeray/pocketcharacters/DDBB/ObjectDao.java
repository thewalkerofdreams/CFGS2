package es.iesnervion.yeray.pocketcharacters.DDBB;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObject;

public interface ObjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertObject(ClsObject object);

    @Update
    void updateObject(ClsObject object);

    @Delete
    void deleteObject(ClsObject object);

    @Query("SELECT * FROM ClsObject")
    ArrayList<ClsObject> getAllObjects();

    @Query("SELECT * FROM ClsObject WHERE name = :name")
    ClsObject getObject(String name);
}
