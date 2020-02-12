package es.iesnervion.yeray.fragments_jugadores.DDBB_Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class ClsPlayer {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "id")
    private int _id;
    @ColumnInfo (name = "nombre")
    private String _nombre;
    @ColumnInfo (name = "apellidos")
    private String _apellidos;

    //Constructores
    public ClsPlayer(){

    }

    @Ignore
    public ClsPlayer(String nombre, String apellidos){
        _nombre = nombre;
        _apellidos = apellidos;
    }

    @Ignore
    public ClsPlayer(int id, String nombre, String apellidos){
        _id = id;
        _nombre = nombre;
        _apellidos = apellidos;
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
}
