package com.example.listado_empleados.DDBB_Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity (indices = {@Index("idDepartamento")}, foreignKeys = {@ForeignKey(entity = ClsDepartamento.class, parentColumns = "id", childColumns = "idDepartamento")})
public class ClsPersona {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "id")
    private int _id;
    @ColumnInfo(name = "nombre")
    private String _nombre;
    @ColumnInfo(name = "apellidos")
    private String _apellidos;
    @ColumnInfo(name = "telefono")
    private String _telefono;
    @ColumnInfo(name = "idDepartamento")
    private int _idDepartamento;

    //Constructor
    public ClsPersona(){

    }

    @Ignore
    public ClsPersona(int id, String nombre, String apellidos, String telefono, int idDepartamnento){
        _id = id;
        _nombre = nombre;
        _apellidos = apellidos;
        _telefono = telefono;
        _idDepartamento = idDepartamnento;
    }

    @Ignore
    public ClsPersona(String nombre, String apellidos, String telefono, int idDepartamnento){
        _nombre = nombre;
        _apellidos = apellidos;
        _telefono = telefono;
        _idDepartamento = idDepartamnento;
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

    public String get_apellidos() {
        return _apellidos;
    }

    public void set_apellidos(String _apellidos) {
        this._apellidos = _apellidos;
    }

    public String get_telefono() {
        return _telefono;
    }

    public void set_telefono(String _telefono) {
        this._telefono = _telefono;
    }

    public int get_idDepartamento() {
        return _idDepartamento;
    }

    public void set_idDepartamento(int _idDepartamento) {
        this._idDepartamento = _idDepartamento;
    }
}
