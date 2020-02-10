package com.example.listado_empleados.DDBB_Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class ClsDepartamento {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "id")
    private int _id;
    @ColumnInfo(name = "nombre")
    private String _nombre;

    //Cosntructores
    public ClsDepartamento(){

    }

    @Ignore
    public ClsDepartamento(String nombre){
        _nombre = nombre;
    }

    //Get y Set

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_nombre() {
        return _nombre;
    }

    public void set_nombre(String _nombre) {
        this._nombre = _nombre;
    }
}
