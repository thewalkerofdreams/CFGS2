package com.example.listadopersonas_va.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.listadopersonas_va.DDBB.AppDataBase;
import com.example.listadopersonas_va.DDBB_Entities.ClsPersona;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class MainPageVM extends AndroidViewModel {    //hago extends androidviewmodel pq necesito el context

    private MutableLiveData<ArrayList<ClsPersona>> _personList;
    private MutableLiveData<ClsPersona> _personSelected;
    private MutableLiveData<ClsPersona> _personCreated;
    private GregorianCalendar fechaActual;
    private MutableLiveData<Boolean> isAddBtnPressed;

    //Constructor
    public MainPageVM(@NonNull Application application) {
        super(application);
        this._personList = new MutableLiveData<>();
        this._personList.setValue(new ArrayList<ClsPersona>());//Le damos un valor por defecto a la lista
        cargarListadoPersonas();

        this._personSelected = new MutableLiveData<>();
        this._personCreated = new MutableLiveData<>();
        this.fechaActual = new GregorianCalendar();//Obtenemos la fecha actual
        this.isAddBtnPressed = new MutableLiveData<>();
        this.isAddBtnPressed.setValue(false);

    }

    //Get y Set

    public LiveData<Boolean> isAddBtnPressed(){
        if(this.isAddBtnPressed == null){
            this.isAddBtnPressed = new MutableLiveData<>();
        }
        return this.isAddBtnPressed;
    }
    public void setIsAddBtnPressed(Boolean isAddBtnPressed){
        this.isAddBtnPressed.setValue(isAddBtnPressed);
    }


    public LiveData<ClsPersona> get_personCreated(){
        if(this._personCreated == null){
            this._personCreated = new MutableLiveData<>();
        }
        return this._personCreated;
    }
    public void set_personCreated(ClsPersona personCreated){
        this._personCreated.setValue(personCreated);
    }


    public GregorianCalendar getFechaActual(){
        return this.fechaActual;
    }


    public LiveData<ClsPersona> get_personSelected(){
        if(this._personSelected == null){
            this._personSelected = new MutableLiveData<>();
        }
        return this._personSelected;
    }

    public void set_personSelected(ClsPersona _personSelected){
        this._personSelected.setValue(_personSelected);
    }

    public LiveData<ArrayList<ClsPersona>> getContactList(){
        if(this._personList == null || this._personList.getValue() == null){
            this._personList = new MutableLiveData<>();

            cargarListadoPersonas();
        }

        return _personList;
    }

    public void set_personList(ArrayList<ClsPersona> _personList) {
        this._personList.setValue(_personList);
    }

    //Metodo añadido
    public void cargarListadoPersonas() {
        ArrayList<ClsPersona> personas = new ArrayList<ClsPersona>(AppDataBase.getDataBase(getApplication().getBaseContext()).clsPersonaDao().getAllPersons());
        _personList.setValue(personas);
    }
}
