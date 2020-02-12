package com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by pjarana on 21/02/18.
 */
@Dao
public interface MyDAO {

    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    public void insertFutbolista(Futbolista...futbolista);
    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    public void insertFutbolistaPosicion(FutbolistaPosicion...f);
    @Query("Select*from Futbolistas")
    public LiveData<List<Futbolista>> getAllFutbolistas();
    @Query("Select*from Futbolistas where ID=:id")
    public Futbolista getFutbolistaPorID(int id);
    @Query("Select*from FutbolistasPosiciones")
    public LiveData<List<FutbolistaPosicion>> getAllFutbolistasPosiciones();
    @Query("Select*from Posiciones where demarcarcion=:demarcacion")
    public int getIDdePosicion(String demarcacion);
    @Query("Select*from posiciones")
    public LiveData<List<Posicion>> getAllPosiciones();
    @Query("Select*from Posiciones as P INNER JOIN FutbolistasPosiciones AS FP ON P.ID=FP.idPosicion WHERE FP.idFutbolista=:id")
    public LiveData<List<Posicion>> getPosicionesDeFutbolista(int id);
    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    public void insertPosicion(Posicion...p);
    @Query("Select*from Futbolistas where ID=(Select MAX(ID)from Futbolistas)")
    public int getUltimoIDFutbolista();
}
