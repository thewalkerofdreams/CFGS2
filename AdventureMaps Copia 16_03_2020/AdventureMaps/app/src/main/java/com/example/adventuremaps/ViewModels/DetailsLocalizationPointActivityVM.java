package com.example.adventuremaps.ViewModels;

import androidx.lifecycle.ViewModel;

import com.example.adventuremaps.FireBaseEntities.ClsLocalizationPoint;

import java.util.ArrayList;

public class DetailsLocalizationPointActivityVM extends ViewModel {

    private String _actualEmailUser;
    private ClsLocalizationPoint _actualLocalizationPoint;
    private ArrayList<String> _localizationTypes;
    private boolean _favourite;

    public DetailsLocalizationPointActivityVM(){
        _actualEmailUser = "";
        _actualLocalizationPoint = null;
        _localizationTypes = new ArrayList<>();
        _favourite = false;
    }

    //Get y Set
    public String get_actualEmailUser() {
        return _actualEmailUser;
    }

    public void set_actualEmailUser(String _actualEmailUser) {
        this._actualEmailUser = _actualEmailUser;
    }

    public ClsLocalizationPoint get_actualLocalizationPoint() {
        return _actualLocalizationPoint;
    }

    public void set_actualLocalizationPoint(ClsLocalizationPoint _actualLocalizationPoint) {
        this._actualLocalizationPoint = _actualLocalizationPoint;
    }

    public ArrayList<String> get_localizationTypes() {
        return _localizationTypes;
    }

    public void set_localizationTypes(ArrayList<String> _localizationTypes) {
        this._localizationTypes = _localizationTypes;
    }

    public boolean is_favourite() {
        return _favourite;
    }

    public void set_favourite(boolean _favourite) {
        this._favourite = _favourite;
    }

    //Funciones añadidas

    /**
     * Interfaz
     * Nombre: getAllTypesOfTheLocalizationPoint
     * Comentario: Este método nos permite obtener todos los tipos del punto de localización actual.
     * Los tipos se almacenarán en la variable _localizationTypes del ViewModel actual.
     * Caebcera: public void getAllTypesOfTheLocalizationPoint()
     * Postcondiciones: El método obtiene y almacena todos los tipos de la localización actual en el
     * atributo _localizationTypes del VM actual.
     */
    public void getAllTypesOfTheLocalizationPoint(){

    }
}
