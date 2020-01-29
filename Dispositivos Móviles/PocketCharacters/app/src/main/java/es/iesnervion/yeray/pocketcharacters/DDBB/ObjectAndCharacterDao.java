package es.iesnervion.yeray.pocketcharacters.DDBB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
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

    @Query("SELECT * FROM ClsObjectAndCharacter")
    List<ClsObjectAndCharacter> getAllObjectsAndCharacters();

    @Query("SELECT * FROM ClsObjectAndCharacter WHERE idCharacter = :idCharacter AND idObject = :idObject")
    ClsObjectAndCharacter getObjectAndCharacter(int idCharacter, int idObject);

    @Query("SELECT ClsObjectAndCharacter.idCharacter, ClsObjectAndCharacter.idObject, ClsObjectAndCharacter.quantity FROM ClsObject " +
            "INNER JOIN ClsObjectAndCharacter ON ClsObject.id = ClsObjectAndCharacter.idObject " +
            "INNER JOIN ClsCharacter ON ClsObjectAndCharacter.idCharacter = ClsCharacter.id " +
            "WHERE ClsCharacter.id = :idCharacter")
    List<ClsObjectAndCharacter> getObjectAndQuantityByCharacter(int idCharacter);
}
