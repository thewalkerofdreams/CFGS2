package com.iesnervion.usuario.examensegundaevaluacionpablojarana.VMyRepositorios;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO.FutbolistaPosicion;
import com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO.Posicion;
import com.iesnervion.usuario.examensegundaevaluacionpablojarana.Models.FutbolistaConPosiciones;

import java.util.List;

/**
 * Created by pjarana on 21/02/18.
 */

public class ListaViewModel extends AndroidViewModel {

    private ListRepository repository;
    private LiveData<List<FutbolistaPosicion>>liveDataFutbolistaPosicion;
    private LiveData<List<Posicion>>liveDataPosicionesDeFutbolista;
    private MutableLiveData<FutbolistaConPosiciones> futbolista=new MutableLiveData<>();
    public ListaViewModel(@NonNull Application application) {
        super(application);
        repository=new ListRepository(application);
        liveDataFutbolistaPosicion=repository.getLiveDataFutbolistaPosicion();
    }

    public MutableLiveData<FutbolistaConPosiciones> getFutbolista() {
        return futbolista;
    }

    public void setFutbolista(FutbolistaConPosiciones futbolista) {
        this.futbolista.setValue(futbolista);
        this.repository.getFutbolistaConPosiciones().setValue(futbolista);
    }

    public void getPosicionesDeFutbolista()
    {

        liveDataPosicionesDeFutbolista=repository.getLiveDataPosicionesDeFutbolista();
    }

    public LiveData<List<FutbolistaPosicion>> getLiveDataFutbolistaPosicion() {
        return liveDataFutbolistaPosicion;
    }

    public void setLiveDataFutbolistaPosicion(LiveData<List<FutbolistaPosicion>> liveDataFutbolistaPosicion) {
        this.liveDataFutbolistaPosicion = liveDataFutbolistaPosicion;
    }

    public LiveData<List<Posicion>> getLiveDataPosicionesDeFutbolista() {
        return liveDataPosicionesDeFutbolista;
    }

    public void setLiveDataPosicionesDeFutbolista(LiveData<List<Posicion>> liveDataPosicionesDeFutbolista) {
        this.liveDataPosicionesDeFutbolista = liveDataPosicionesDeFutbolista;
    }
}
