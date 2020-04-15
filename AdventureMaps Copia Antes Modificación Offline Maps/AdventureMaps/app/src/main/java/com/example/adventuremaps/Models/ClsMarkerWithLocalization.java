package com.example.adventuremaps.Models;

import com.example.adventuremaps.FireBaseEntities.ClsLocalizationPoint;
import com.google.android.gms.maps.model.Marker;

public class ClsMarkerWithLocalization {
    private Marker marker;
    private ClsLocalizationPoint localizationPoint;

    public  ClsMarkerWithLocalization(){

    }

    public  ClsMarkerWithLocalization(Marker marker, ClsLocalizationPoint localizationPoint){
        this.marker = marker;
        this.localizationPoint = localizationPoint;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public ClsLocalizationPoint getLocalizationPoint() {
        return localizationPoint;
    }

    public void setLocalizationPoint(ClsLocalizationPoint localizationPoint) {
        this.localizationPoint = localizationPoint;
    }
}
