package es.iesnervion.yeray.fragments_jugadores.DDBB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import es.iesnervion.yeray.fragments_jugadores.DDBB_Entities.ClsPlayerPosition;

@Dao
public interface ClsPlayerPositionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlayerPosition(ClsPlayerPosition playerPosition);

    @Update
    void updatePlayerPosition(ClsPlayerPosition playerPosition);

    @Delete
    void deletePlayerPosition(ClsPlayerPosition playerPosition);

    @Transaction
    @Query("SELECT * FROM ClsPlayerPosition")
    List<ClsPlayerPosition> getAllPlayerPositions();

    @Transaction
    @Query("SELECT * FROM ClsPlayerPosition WHERE ClsPlayerPosition.idPlayer = :playerId")
    ClsPlayerPosition getPlayerPositionByPlayerId(int playerId);

    @Transaction
    @Query("SELECT * FROM ClsPlayerPosition WHERE ClsPlayerPosition.idPosition = :positionId")
    ClsPlayerPosition getPlayerPositionByPositionId(int positionId);
}
