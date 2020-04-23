package com.example.adventuremaps.Models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MyClusterItem implements ClusterItem {
    private final LatLng mPosition;
    private String mTitle;
    private String mSnippet;
    private String tag;
    private boolean itemSelected;

    public MyClusterItem(double lat, double lng, String tag, boolean itemSelected) {
        mPosition = new LatLng(lat, lng);
        this.tag = tag;
        this.itemSelected = itemSelected;
    }

    public MyClusterItem(double lat, double lng, String title, String snippet) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    public String getTag(){
        return tag;
    }

    public boolean getItemSelected(){
        return itemSelected;
    }
}
