package com.iesnervion.usuario.examensegundaevaluacionpablojarana.VMyRepositorios;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO.Database;
import com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO.Futbolista;
import com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO.FutbolistaPosicion;
import com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO.MyDAO;
import com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO.Posicion;
import com.iesnervion.usuario.examensegundaevaluacionpablojarana.Models.FutbolistaConPosiciones;

import java.util.List;

/**
 * Created by pjarana on 21/02/18.
 */

public class ListRepository {

    private MyDAO myDAO;
    private LiveData<List<FutbolistaPosicion>> liveDataFutbolistaPosicion;
    private LiveData<List<Posicion>> liveDataPosicionesDeFutbolista;
    private MutableLiveData<FutbolistaConPosiciones> futbolistaConPosiciones=new MutableLiveData<>();
    public ListRepository(Application app)
    {
        myDAO= Database.getDatabase(app).getEquipoDao();
        liveDataFutbolistaPosicion=myDAO.getAllFutbolistasPosiciones();
    }


    public LiveData<List<Posicion>> getLiveDataPosicionesDeFutbolista() {
        liveDataPosicionesDeFutbolista=myDAO.getPosicionesDeFutbolista(futbolistaConPosiciones.getValue().getId());
        new getFutbolistaPorID().execute(futbolistaConPosiciones.getValue().getId());
        return liveDataPosicionesDeFutbolista;
    }

    public LiveData<List<FutbolistaPosicion>> getLiveDataFutbolistaPosicion() {
        return liveDataFutbolistaPosicion;
    }

    public void setLiveDataFutbolistaPosicion(LiveData<List<FutbolistaPosicion>> liveDataFutbolistaPosicion) {
        this.liveDataFutbolistaPosicion = liveDataFutbolistaPosicion;
    }

    public MutableLiveData<FutbolistaConPosiciones> getFutbolistaConPosiciones() {
        return futbolistaConPosiciones;
    }

    public void setFutbolistaConPosiciones(MutableLiveData<FutbolistaConPosiciones> futbolistaConPosiciones) {
        this.futbolistaConPosiciones = futbolistaConPosiciones;
    }

    private class getFutbolistaPorID extends AsyncTask<Integer,Void,Void>
    {


        @Override
        protected Void doInBackground(Integer... integers) {
            for(int i=0;i<integers.length;i++) {
                Futbolista futbolista = myDAO.getFutbolistaPorID(integers[i]);
                futbolistaConPosiciones.getValue().setNombre(futbolista.getNombre());
                futbolistaConPosiciones.getValue().setApellidos(futbolista.getApellidos());
            }
            return null;
        }
    }
}
