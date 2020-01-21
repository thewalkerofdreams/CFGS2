package es.iesnervion.yeray.pocketcharacters.DDBB;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.Entities.ClsCharacter;
import es.iesnervion.yeray.pocketcharacters.Entities.ClsStat;

public interface StatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStat(ClsStat stat);

    @Update
    void updateStat(ClsStat stat);

    @Delete
    void deleteStat(ClsStat stat);

    @Query("SELECT * FROM ClsStat")
    ArrayList<ClsStat> getAllStats();

    @Query("SELECT * FROM ClsStat WHERE name = :name")
    ClsStat getStat(String name);
}
