package com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by pjarana on 21/02/18.
 */
@Entity(tableName = "FutbolistasPosiciones",primaryKeys = {"idFutbolista","idPosicion"},foreignKeys = {@ForeignKey(parentColumns = "ID",entity=Futbolista.class,childColumns = "idFutbolista"),@ForeignKey(parentColumns = "ID",entity = Posicion.class,childColumns = "idPosicion")})
public class FutbolistaPosicion {

    private int idFutbolista;
    private int idPosicion;

    public FutbolistaPosicion()
    {
        this.idFutbolista=0;
        this.idPosicion=0;
    }

    public FutbolistaPosicion(int idFutbolista, int idPosicion)
    {
        this.idFutbolista=idFutbolista;
        this.idPosicion=idPosicion;
    }

    public int getIdFutbolista() {
        return idFutbolista;
    }

    public void setIdFutbolista(int idFutbolista) {
        this.idFutbolista = idFutbolista;
    }

    public int getIdPosicion() {
        return idPosicion;
    }

    public void setIdPosicion(int idPosicion) {
        this.idPosicion = idPosicion;
    }
}
