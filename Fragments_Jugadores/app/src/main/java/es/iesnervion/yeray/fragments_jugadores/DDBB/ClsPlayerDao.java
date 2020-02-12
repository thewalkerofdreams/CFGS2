package es.iesnervion.yeray.fragments_jugadores.DDBB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import es.iesnervion.yeray.fragments_jugadores.DDBB_Entities.ClsPlayer;
import es.iesnervion.yeray.fragments_jugadores.POJO.ClsPlayerWithPositions;

@Dao
public interface ClsPlayerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlayer(ClsPlayer player);

    @Update
    void updatePlayer(ClsPlayer player);

    @Delete
    void deletePlayer(ClsPlayer player);

    @Transaction
    @Query("SELECT * FROM ClsPlayer")
    List<ClsPlayer> getAllPlayers();

    @Transaction
    @Query("SELECT * FROM ClsPlayer WHERE ClsPlayer.id = :id")
    ClsPlayer getPlayerById(int id);

    @Query("SELECT * FROM ClsPlayer")
    public List<ClsPlayerWithPositions> loadPlayersWithPositions();
}
