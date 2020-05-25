package com.example.adventuremaps.ViewModels;

import androidx.lifecycle.ViewModel;

import com.example.adventuremaps.Models.ClsImageWithId;
import com.example.adventuremaps.FireBaseEntities.ClsLocalizationPoint;

import java.util.ArrayList;

public class ImageGalleryViewPagerActivityVM extends ViewModel {

    private String _actualUserEmail;
    private ClsLocalizationPoint _actualLocalizationPoint;
    private ArrayList<ClsImageWithId> _imagesToLoad;
    private int _positionSelectedImage;
    private float _generalRatingOfActualImage;
    private int _numberOfValorations;

    public ImageGalleryViewPagerActivityVM(){
        _actualUserEmail = "";
        _actualLocalizationPoint = null;
        _imagesToLoad = new ArrayList<>();
        _positionSelectedImage = -1;
        _generalRatingOfActualImage = 0;
        _numberOfValorations = 0;
    }

    //Get y Set
    public String get_actualUserEmail() {
        return _actualUserEmail;
    }

    public void set_actualUserEmail(String _actualUserEmail) {
        this._actualUserEmail = _actualUserEmail;
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

    public int get_positionSelectedImage() {
        return _positionSelectedImage;
    }

    public void set_positionSelectedImage(int _positionSelectedImage) {
        this._positionSelectedImage = _positionSelectedImage;
    }

    public float get_generalRatingOfActualImage() {
        return _generalRatingOfActualImage;
    }

    public void set_generalRatingOfActualImage(float _generalRatingOfActualImage) {
        this._generalRatingOfActualImage = _generalRatingOfActualImage;
    }

    public int get_numberOfValorations() {
        return _numberOfValorations;
    }

    public void set_numberOfValorations(int _numberOfValorations) {
        this._numberOfValorations = _numberOfValorations;
    }
}
