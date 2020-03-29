package com.example.adventuremaps.ViewModels;

import androidx.lifecycle.ViewModel;

import com.example.adventuremaps.FireBaseEntities.ClsLocalizationPoint;

import java.util.ArrayList;

public class EditLocalizationPointActivityVM extends ViewModel {

    private String _actualEmailUser;
    private ClsLocalizationPoint _actualLocalizationPoint;
    private ArrayList<String> _localizationTypes;
    private ArrayList<String> _localizationsIdActualUser;
    private String _newName;
    private String _newDescription;
    private boolean _favourite;

    public EditLocalizationPointActivityVM(){
        _actualEmailUser = "";
        _actualLocalizationPoint = null;
        _localizationTypes = new ArrayList<>();
        _favourite = false;
        _localizationsIdActualUser = new ArrayList<>();
        _newName = "";
        _newDescription = "";
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

    public String get_newName() {
        return _newName;
    }

    public void set_newName(String _newName) {
        this._newName = _newName;
    }

    public String get_newDescription() {
        return _newDescription;
    }

    public void set_newDescription(String _newDescription) {
        this._newDescription = _newDescription;
    }

    public ArrayList<String> get_localizationsIdActualUser() {
        return _localizationsIdActualUser;
    }

    public void set_localizationsIdActualUser(ArrayList<String> _localizationsIdActualUser) {
        this._localizationsIdActualUser = _localizationsIdActualUser;
    }
}
