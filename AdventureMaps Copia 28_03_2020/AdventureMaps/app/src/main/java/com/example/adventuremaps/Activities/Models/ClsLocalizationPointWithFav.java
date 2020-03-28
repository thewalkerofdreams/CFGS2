package com.example.adventuremaps.Activities.Models;

import com.example.adventuremaps.FireBaseEntities.ClsLocalizationPoint;

public class ClsLocalizationPointWithFav {
    private ClsLocalizationPoint _localizationPoint;
    private boolean _favourite;

    public ClsLocalizationPointWithFav(){
        _localizationPoint = null;
        _favourite = false;
    }

    public ClsLocalizationPointWithFav(ClsLocalizationPoint _localizationPoint, boolean _favourite){
        this._localizationPoint = _localizationPoint;
        this._favourite = _favourite;
    }

    //Get y Set
    public ClsLocalizationPoint get_localizationPoint() {
        return _localizationPoint;
    }

    public void set_localizationPoint(ClsLocalizationPoint _localizationPoint) {
        this._localizationPoint = _localizationPoint;
    }

    public boolean is_favourite() {
        return _favourite;
    }

    public void set_favourite(boolean _favourite) {
        this._favourite = _favourite;
    }
}