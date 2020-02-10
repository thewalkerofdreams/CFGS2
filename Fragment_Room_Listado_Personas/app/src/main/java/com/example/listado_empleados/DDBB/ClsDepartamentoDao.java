package com.example.listado_empleados.DDBB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.listado_empleados.DDBB_Entities.ClsDepartamento;

import java.util.List;

@Dao
public interface ClsDepartamentoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDepartament(ClsDepartamento departamento);

    @Update
    void updateDepartament(ClsDepartamento departamento);

    @Delete
    void deleteDepartament(ClsDepartamento departamento);

    @Transaction
    @Query("SELECT * FROM ClsDepartamento")
    List<ClsDepartamento> getAllDepartaments();

    @Transaction
    @Query("SELECT * FROM ClsDepartamento WHERE ClsDepartamento.id = :idDepartamento")
    ClsDepartamento getAllDepartamentById(int idDepartamento);
}
