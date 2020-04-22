package com.example.adventuremaps.Models;

import com.example.adventuremaps.FireBaseEntities.ClsLocalizationPoint;
import com.mapbox.mapboxsdk.annotations.Marker;

public class ClsMarkerWithLocalizationMapbox {
    private Marker marker;
    private ClsLocalizationPoint localizationPoint;

    public ClsMarkerWithLocalizationMapbox(){

    }

    public ClsMarkerWithLocalizationMapbox(Marker marker, ClsLocalizationPoint localizationPoint){
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
