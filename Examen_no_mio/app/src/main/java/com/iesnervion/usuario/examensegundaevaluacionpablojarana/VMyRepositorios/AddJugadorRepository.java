package com.iesnervion.usuario.examensegundaevaluacionpablojarana.VMyRepositorios;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO.Database;
import com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO.Futbolista;
import com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO.FutbolistaPosicion;
import com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO.MyDAO;
import com.iesnervion.usuario.examensegundaevaluacionpablojarana.DAO.Posicion;

import java.util.List;

/**
 * Created by pjarana on 21/02/18.
 */

public class AddJugadorRepository {

    private MyDAO myDAO;
    private LiveData<List<Posicion>> liveDataPosicion;
    private MutableLiveData<Integer> idPosicion=new MutableLiveData<>();
    private MutableLiveData<Integer> idFutbolista=new MutableLiveData<>();
    public AddJugadorRepository(Application app)
    {
        myDAO= Database.getDatabase(app).getEquipoDao();
        liveDataPosicion=myDAO.getAllPosiciones();
    }

    public LiveData<List<Posicion>> getLiveDataPosicion() {
        return liveDataPosicion;
    }

    public void setLiveDataPosicion(LiveData<List<Posicion>> liveDataPosicion) {
        this.liveDataPosicion = liveDataPosicion;
    }
    public void getIDdePosicion(String demarcacion)
    {
        new getIDdePosicion().execute(demarcacion);

    }

    public void getUltimoIDFutbolista()
    {
        new getUltimoIDFutbolista().execute();
    }
    public void insertFutbolista(Futbolista...futbolistas)
    {
        new insertFutbolista().execute(futbolistas);
    }
    public void insertFutbolistaPosicion(FutbolistaPosicion futbolistaPosicion)
    {
        new insertFutbolistaPosicion().execute(futbolistaPosicion);
    }

    public MutableLiveData<Integer> getIdFutbolista() {
        return idFutbolista;
    }

    public void setIdFutbolista(MutableLiveData<Integer> idFutbolista) {
        this.idFutbolista = idFutbolista;
    }

    public MutableLiveData<Integer> getIdPosicion() {
        return idPosicion;
    }

    public void setIdPosicion(MutableLiveData<Integer> idPosicion) {
        this.idPosicion = idPosicion;
    }

    private class getIDdePosicion extends AsyncTask<String,Void,Integer>
    {
        @Override
        protected Integer doInBackground(String... strings) {
            int IDPosicion=0;
            for(int i=0;i<strings.length;i++)
            {
                IDPosicion=myDAO.getIDdePosicion(strings[i]);

            }
            return IDPosicion;
        }

        @Override
        protected void onPostExecute(Integer aVoid) {
            super.onPostExecute(aVoid);
            AddJugadorRepository.this.idPosicion.setValue(aVoid);
        }
    }

    private class getUltimoIDFutbolista extends AsyncTask<Void,Void,Integer>
    {

        @Override
        protected Integer doInBackground(Void... voids) {
            int idFutbolista=0;
            idFutbolista=myDAO.getUltimoIDFutbolista();
            return idFutbolista;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            AddJugadorRepository.this.getIdFutbolista().setValue(integer);
        }
    }

    private class insertFutbolistaPosicion extends AsyncTask<FutbolistaPosicion,Void,Void>
    {

        @Override
        protected Void doInBackground(FutbolistaPosicion... futbolistaPosicions) {
            myDAO.insertFutbolistaPosicion(futbolistaPosicions);
            return null;
        }
    }
    private class insertFutbolista extends AsyncTask<Futbolista,Void,Void>
    {

        @Override
        protected Void doInBackground(Futbolista... futbolistas) {
            myDAO.insertFutbolista(futbolistas);
            return null;
        }
    }
}
