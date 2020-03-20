package com.example.adventuremaps.ViewModels;

import androidx.lifecycle.ViewModel;

import com.example.adventuremaps.Activities.Models.ClsImageWithId;
import com.example.adventuremaps.FireBaseEntities.ClsLocalizationPoint;

import java.util.ArrayList;

public class ImageGalleryActivityVM extends ViewModel {

    private String _actualEmailUser;
    private ClsLocalizationPoint _actualLocalizationPoint;
    private ArrayList<ClsImageWithId> _imagesToLoad;
    private ArrayList<ClsImageWithId> _imagesSelected;
    private boolean _dialogDeleteImagesShowing;

    public ImageGalleryActivityVM(){
        _actualEmailUser = "";
        _actualLocalizationPoint = null;
        _imagesToLoad = new ArrayList<>();
        _imagesSelected = new ArrayList<>();
        _dialogDeleteImagesShowing = false;
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

    public ArrayList<ClsImageWithId> get_imagesToLoad() {
        return _imagesToLoad;
    }

    public void set_imagesToLoad(ArrayList<ClsImageWithId> _imagesToLoad) {
        this._imagesToLoad = _imagesToLoad;
    }

    public ArrayList<ClsImageWithId> get_imagesSelected() {
        return _imagesSelected;
    }

    public void set_imagesSelected(ArrayList<ClsImageWithId> _imagesSelected) {
        this._imagesSelected = _imagesSelected;
    }

    public boolean is_dialogDeleteImagesShowing() {
        return _dialogDeleteImagesShowing;
    }

    public void set_dialogDeleteImagesShowing(boolean _dialogDeleteImagesShowing) {
        this._dialogDeleteImagesShowing = _dialogDeleteImagesShowing;
    }
}
