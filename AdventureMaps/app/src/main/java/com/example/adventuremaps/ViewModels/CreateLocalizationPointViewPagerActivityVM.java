package com.example.adventuremaps.ViewModels;

import androidx.lifecycle.ViewModel;

import com.example.adventuremaps.Models.ClsImageWithId;

import java.util.ArrayList;

public class CreateLocalizationPointViewPagerActivityVM extends ViewModel {
    private ArrayList<ClsImageWithId> _imagesToLoad;
    private int _positionSelectedImage;

    public CreateLocalizationPointViewPagerActivityVM(){
        _imagesToLoad = new ArrayList<>();
        _positionSelectedImage = 0;
    }

    //Get y Set
    public ArrayList<ClsImageWithId> get_imagesToLoad() {
        return _imagesToLoad;
    }

    public void set_imagesToLoad(ArrayList<ClsImageWithId> _imagesToLoad) {
        this._imagesToLoad = _imagesToLoad;
    }

    public int get_positionSelectedImage() {
        return _positionSelectedImage;
    }

    public void set_positionSelectedImage(int _positionSelectedImage) {
        this._positionSelectedImage = _positionSelectedImage;
    }
}
