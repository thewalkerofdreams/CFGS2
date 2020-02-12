package es.iesnervion.yeray.fragments_jugadores.DDBB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import es.iesnervion.yeray.fragments_jugadores.DDBB_Entities.ClsPosition;

@Dao
public interface ClsPositionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPosition(ClsPosition position);

    @Update
    void updatePosition(ClsPosition position);

    @Delete
    void deletePosition(ClsPosition position);

    @Transaction
    @Query("SELECT * FROM ClsPosition")
    List<ClsPosition> getAllPositions();

    @Transaction
    @Query("SELECT * FROM ClsPosition WHERE ClsPosition.id = :id")
    ClsPosition getPositionById(int id);

    @Transaction
    @Query("SELECT * FROM ClsPosition WHERE ClsPosition.nombrePosicion = :nombre")
    ClsPosition getPositionByName(String nombre);
}
