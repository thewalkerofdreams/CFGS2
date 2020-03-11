package com.example.adventuremaps.Activities.Models;

import com.google.android.gms.maps.model.Marker;

public class ClsMarkerWithPriority {
    private Marker marker;
    private int priority;

    public ClsMarkerWithPriority(){

    }

    public ClsMarkerWithPriority(Marker marker, int priority){
        this.marker = marker;
        this.priority = priority;
    }

    //Get y Set
    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
