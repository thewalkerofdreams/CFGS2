package es.iesnervion.yeray.pocketcharacters.DDBB;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.Entities.ClsObjectAndQuantity;

public interface ObjectAndQuantityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertObjectAndQuantity(ClsObjectAndQuantity objectAndQuantity);

    @Update
    void updateObjectAndQuantity(ClsObjectAndQuantity objectAndQuantity);

    @Delete
    void deleteObjectAndQuantity(ClsObjectAndQuantity objectAndQuantity);

    @Query("SELECT * FROM ClsObjectAndQuantity")
    ArrayList<ClsObjectAndQuantity> getAllObjectsAndQuantities();

    @Query("SELECT * FROM ClsObjectAndQuantity WHERE name = :name")
    ClsObjectAndQuantity getObjectAndQuantity(String name);
}
