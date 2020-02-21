package es.iesnervion.yeray.pocketcharacters.DDBB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObjectAndCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesModels.ClsStatModel;

@Dao
public interface ObjectAndCharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertObjectAndCharacter(ClsObjectAndCharacter objectAndCharacter);

    @Update
    void updateObjectAndCharacter(ClsObjectAndCharacter objectAndCharacter);

    @Delete
    void deleteObjectAndCharacter(ClsObjectAndCharacter objectAndCharacter);

    @Transaction
    @Query("DELETE FROM ClsObjectAndCharacter WHERE ClsObjectAndCharacter.idCharacter = :characterId")
    void deleteObjectAndCharacter(int characterId);

    @Transaction
    @Query("SELECT * FROM ClsObjectAndCharacter")
    List<ClsObjectAndCharacter> getAllObjectsAndCharacters();

    @Transaction
    @Query("SELECT * FROM ClsObjectAndCharacter WHERE idCharacter = :idCharacter AND idObject = :idObject")
    ClsObjectAndCharacter getObjectAndCharacter(int idCharacter, int idObject);

    @Transaction
    @Query("SELECT * FROM ClsObjectAndCharacter WHERE idObject = :idObject")
    List<ClsObjectAndCharacter> getObjectAndCharacter(int idObject);

    @Transaction
    @Query("SELECT ClsObjectAndCharacter.idCharacter, ClsObjectAndCharacter.idObject, ClsObjectAndCharacter.quantity FROM ClsObject " +
            "INNER JOIN ClsObjectAndCharacter ON ClsObject.id = ClsObjectAndCharacter.idObject " +
            "INNER JOIN ClsCharacter ON ClsObjectAndCharacter.idCharacter = ClsCharacter.id " +
            "WHERE ClsCharacter.id = :idCharacter")
    List<ClsObjectAndCharacter> getObjectAndQuantityByCharacter(int idCharacter);
}
