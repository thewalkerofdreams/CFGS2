package com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by pjarana on 21/02/18.
 */
@Entity(tableName = "Futbolistas")
public class Futbolista {

    @ColumnInfo(name = "ID")
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nombre;
    private String apellidos;

    public Futbolista()
    {
        id=0;
        nombre="";
        apellidos="";
    }
    public Futbolista(String nombre,String apellidos)
    {
        this.id=0;
        this.nombre=nombre;
        this.apellidos=apellidos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
}
