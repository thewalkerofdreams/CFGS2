package com.iesnervion.usuario.examensegundaevaluacionpablojarana.VMyRepositorios;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO.Futbolista;
import com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO.FutbolistaPosicion;
import com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO.Posicion;

import java.util.List;

/**
 * Created by pjarana on 21/02/18.
 */

public class AddJugadorViewModel extends AndroidViewModel {

    private LiveData<List<Posicion>> liveDataPosiciones;
    private MutableLiveData<Integer> idPosicion=new MutableLiveData<>();
    private AddJugadorRepository repository;
    public AddJugadorViewModel(@NonNull Application application) {
        super(application);
        repository=new AddJugadorRepository(application);
        liveDataPosiciones=repository.getLiveDataPosicion();
    }

    public LiveData<List<Posicion>> getLiveDataPosiciones() {
        return liveDataPosiciones;
    }
    public void insertFutbolistaPosicion(FutbolistaPosicion futbolistaPosicion)
    {
        this.repository.insertFutbolistaPosicion(futbolistaPosicion);
    }
    public MutableLiveData<Integer> getIdPosicion() {
        return idPosicion;
    }

    public void getIDdePosicion(String demarcacion)
    {
        this.repository.getIDdePosicion(demarcacion);
        this.idPosicion.setValue(repository.getIdPosicion().getValue());
    }
    public void setIdPosicion(MutableLiveData<Integer> idPosicion) {
        this.idPosicion = idPosicion;
    }

    public void setLiveDataPosiciones(LiveData<List<Posicion>> liveDataPosiciones) {
        this.liveDataPosiciones = liveDataPosiciones;
    }

    public void insertFutbolista(Futbolista futbolista)
    {
        this.repository.insertFutbolista(futbolista);
    }


}
