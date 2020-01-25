package es.iesnervion.yeray.pocketcharacters.DDBB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacterAndStat;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsStat;
import es.iesnervion.yeray.pocketcharacters.EntitiesModels.ClsStatModel;

@Dao
public interface CharacterAndStatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCharacterAndStat(ClsCharacterAndStat characterAndStat);

    @Update
    void updateCharacterAndStat(ClsCharacterAndStat characterAndStat);

    @Delete
    void deleteCharacterAndStat(ClsCharacterAndStat characterAndStat);

    @Query("SELECT * FROM ClsCharacterAndStat")
    List<ClsCharacterAndStat> getAllCharactersAndStats();

    @Query("SELECT * FROM ClsCharacterAndStat WHERE idCharacter = :idCharacter AND idStat = :idStat")
    ClsCharacterAndStat getCharacterAndStat(int idCharacter, int idStat);

    /*@Query("SELECT S.name, CS.value FROM ClsStat AS S " +
            "INNER JOIN ClsCharacterAndStat AS CS ON S.id = CS.idStat " +
            "INNER JOIN ClsCharacter AS C ON CS.idCharacter = C.id " +
            "WHERE C.id = :idCharacter")
    ArrayList<ClsStatModel> getStatsAndValueByCharacter(int idCharacter);*/

}
