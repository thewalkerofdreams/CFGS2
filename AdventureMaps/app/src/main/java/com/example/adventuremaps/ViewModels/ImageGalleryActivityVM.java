package com.example.adventuremaps.ViewModels;

import androidx.lifecycle.ViewModel;

import com.example.adventuremaps.Activities.Models.ClsImageWithId;

import java.util.ArrayList;

public class ImageGalleryActivityVM extends ViewModel {

    private String _actualEmailUser;
    private String _actualLocalizationPointId;
    private ArrayList<ClsImageWithId> _imagesToLoad;
    private ArrayList<ClsImageWithId> _imagesSelected;
    private boolean _dialogDeleteImagesShowing;
    private ArrayList<String> _urlImagesFromFireBase;

    public ImageGalleryActivityVM(){
        _actualEmailUser = "";
        _actualLocalizationPointId = "";
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

    public String get_actualLocalizationPointId() {
        return _actualLocalizationPointId;
    }

    public void set_actualLocalizationPointId(String _actualLocalizationPointId) {
        this._actualLocalizationPointId = _actualLocalizationPointId;
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
