package com.example.adventuremaps.ViewModels;

import androidx.lifecycle.ViewModel;

import com.example.adventuremaps.FireBaseEntities.ClsLocalizationPoint;

import java.util.ArrayList;

public class DetailsLocalizationPointActivityVM extends ViewModel {

    private String _actualEmailUser;
    private ClsLocalizationPoint _actualLocalizationPoint;
    private ArrayList<String> _localizationKeyTypes;
    private ArrayList<String> _localizationNameTypes;
    private ArrayList<String> _localizationsIdActualUser;
    private boolean _goodValorationDialogShowing;
    private boolean _badValorationDialogShowing;

    public DetailsLocalizationPointActivityVM(){
        _actualEmailUser = "";
        _actualLocalizationPoint = null;
        _localizationKeyTypes = new ArrayList<>();
        _localizationNameTypes = new ArrayList<>();
        _localizationsIdActualUser = new ArrayList<>();
        _goodValorationDialogShowing = false;
        _badValorationDialogShowing = false;
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

    public ArrayList<String> get_localizationKeyTypes() {
        return _localizationKeyTypes;
    }

    public void set_localizationKeyTypes(ArrayList<String> _localizationKeyTypes) {
        this._localizationKeyTypes = _localizationKeyTypes;
    }

    public ArrayList<String> get_localizationNameTypes() {
        return _localizationNameTypes;
    }

    public void set_localizationNameTypes(ArrayList<String> _localizationNameTypes) {
        this._localizationNameTypes = _localizationNameTypes;
    }

    public ArrayList<String> get_localizationsIdActualUser() {
        return _localizationsIdActualUser;
    }

    public void set_localizationsIdActualUser(ArrayList<String> _localizationsIdActualUser) {
        this._localizationsIdActualUser = _localizationsIdActualUser;
    }

    public boolean is_goodValorationDialogShowing() {
        return _goodValorationDialogShowing;
    }

    public void set_goodValorationDialogShowing(boolean _goodValorationDialogShowing) {
        this._goodValorationDialogShowing = _goodValorationDialogShowing;
    }

    public boolean is_badValorationDialogShowing() {
        return _badValorationDialogShowing;
    }

    public void set_badValorationDialogShowing(boolean _badValorationDialogShowing) {
        this._badValorationDialogShowing = _badValorationDialogShowing;
    }
}
