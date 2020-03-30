package com.example.adventuremaps.ViewModels;

import androidx.lifecycle.ViewModel;

import com.example.adventuremaps.Models.ClsImageWithId;

import java.util.ArrayList;

public class CreateLocalizationPointImageGalleryActivityVM extends ViewModel {

    private String _actualEmailUser;
    private ArrayList<ClsImageWithId> _imagesSelected;
    private ArrayList<ClsImageWithId> _imagesToSave;
    private boolean _dialogDeleteImagesShowing;
    private boolean _searchingImage;

    public CreateLocalizationPointImageGalleryActivityVM(){
        _actualEmailUser = "";
        _imagesSelected = new ArrayList<>();
        _imagesToSave = new ArrayList<>();
        _dialogDeleteImagesShowing = false;
        _searchingImage = false;
    }

    //Get y Set
    public String get_actualEmailUser() {
        return _actualEmailUser;
    }

    public void set_actualEmailUser(String _actualEmailUser) {
        this._actualEmailUser = _actualEmailUser;
    }

    public ArrayList<ClsImageWithId> get_imagesSelected() {
        return _imagesSelected;
    }

    public void set_imagesSelected(ArrayList<ClsImageWithId> _imagesSelected) {
        this._imagesSelected = _imagesSelected;
    }

    public ArrayList<ClsImageWithId> get_imagesToSave() {
        return _imagesToSave;
    }

    public void set_imagesToSave(ArrayList<ClsImageWithId> _imagesToSave) {
        this._imagesToSave = _imagesToSave;
    }

    public boolean is_dialogDeleteImagesShowing() {
        return _dialogDeleteImagesShowing;
    }

    public void set_dialogDeleteImagesShowing(boolean _dialogDeleteImagesShowing) {
        this._dialogDeleteImagesShowing = _dialogDeleteImagesShowing;
    }

    public boolean is_searchingImage() {
        return _searchingImage;
    }

    public void set_searchingImage(boolean _searchingImage) {
        this._searchingImage = _searchingImage;
    }
}
