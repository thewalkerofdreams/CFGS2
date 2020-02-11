package com.example.listado_empleados.DDBB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.listado_empleados.DDBB_Entities.ClsPersona;

import java.util.List;

@Dao
public interface ClsPersonaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPerson(ClsPersona persona);

    @Update
    void updatePerson(ClsPersona persona);

    @Delete
    void deletePerson(ClsPersona persona);

    @Transaction
    @Query("SELECT * FROM ClsPersona")
    List<ClsPersona> getAllPersons();

    @Transaction
    @Query("SELECT * FROM ClsPersona WHERE ClsPersona.id = :id")
    ClsPersona getPersonById(int id);

}
