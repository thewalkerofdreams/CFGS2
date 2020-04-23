package com.example.adventuremaps.ViewModels;

import androidx.lifecycle.ViewModel;

import com.example.adventuremaps.Models.ClsImageWithId;

import java.util.ArrayList;

public class CreateLocalizationPointActivityVM extends ViewModel {

    private String _actualEmailUser;
    private String _name;
    private String _description;
    private ArrayList<String> _localizationTypes;
    private double _latitude;
    private double _longitude;
    private boolean _favourite;
    private ArrayList<ClsImageWithId> _imagesToSave;

    public CreateLocalizationPointActivityVM(){
        _actualEmailUser = "";
        _name = "";
        _description = "";
        _localizationTypes = new ArrayList<>();
        _latitude = 0.0;
        _longitude = 0.0;
        _favourite = false;
        _imagesToSave = new ArrayList<>();
    }

    //Get y Set
    public String get_actualEmailUser() {
        return _actualEmailUser;
    }

    public void set_actualEmailUser(String _actualEmailUser) {
        this._actualEmailUser = _actualEmailUser;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public ArrayList<String> get_localizationTypes() {
        return _localizationTypes;
    }

    public void set_localizationTypes(ArrayList<String> _localizationTypes) {
        this._localizationTypes = _localizationTypes;
    }

    public double get_latitude() {
        return _latitude;
    }

    public void set_latitude(double _latitude) {
        this._latitude = _latitude;
    }

    public double get_longitude() {
        return _longitude;
    }

    public void set_longitude(double _longitude) {
        this._longitude = _longitude;
    }

    public boolean is_favourite() {
        return _favourite;
    }

    public void set_favourite(boolean _favourite) {
        this._favourite = _favourite;
    }

    public ArrayList<ClsImageWithId> get_imagesToSave() {
        return _imagesToSave;
    }

    public void set_imagesToSave(ArrayList<ClsImageWithId> _imagesToSave) {
        this._imagesToSave = _imagesToSave;
    }

}
