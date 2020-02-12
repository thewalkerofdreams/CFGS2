package es.iesnervion.yeray.fragments_jugadores.DDBB_Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class ClsPosition {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "id")
    private int _id;
    @ColumnInfo (name = "nombrePosicion")
    private String _nombrePosicion;

    //Constructores
    public ClsPosition(){

    }

    @Ignore
    public ClsPosition(String nombrePosicion){
        _nombrePosicion = nombrePosicion;
    }

    @Ignore
    public ClsPosition(int id, String nombrePosicion){
        _id = id;
        _nombrePosicion = nombrePosicion;
    }

    //Get y Set

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_nombrePosicion() {
        return _nombrePosicion;
    }

    public void set_nombrePosicion(String _nombrePosicion) {
        this._nombrePosicion = _nombrePosicion;
    }
}
