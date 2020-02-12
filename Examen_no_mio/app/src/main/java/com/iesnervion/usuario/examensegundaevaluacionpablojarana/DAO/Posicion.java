package com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by pjarana on 21/02/18.
 */
@Entity(tableName = "Posiciones")
public class Posicion {

    @ColumnInfo(name = "ID")
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String demarcarcion;

    public Posicion()
    {
        this.id=0;
        this.demarcarcion="";
    }
    public Posicion(String demarcarcion)
    {
        this.id=0;
        this.demarcarcion=demarcarcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDemarcarcion() {
        return demarcarcion;
    }

    public void setDemarcarcion(String demarcarcion) {
        this.demarcarcion = demarcarcion;
    }
}
