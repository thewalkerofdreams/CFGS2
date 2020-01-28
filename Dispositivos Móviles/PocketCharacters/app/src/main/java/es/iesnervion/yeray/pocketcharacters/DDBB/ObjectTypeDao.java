package es.iesnervion.yeray.pocketcharacters.DDBB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObjectType;

@Dao
public interface ObjectTypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertObjectType(ClsObjectType object);

    @Update
    void updateObjectType(ClsObjectType object);

    @Delete
    void deleteObjectType(ClsObjectType object);

    @Query("SELECT * FROM ClsObjectType")
    List<ClsObjectType> getAllObjectTypes();

    @Query("SELECT * FROM ClsObjectType WHERE name = :name")
    ClsObjectType getObjectType(String name);
}
