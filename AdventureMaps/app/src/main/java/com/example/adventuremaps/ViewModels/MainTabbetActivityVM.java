package com.example.adventuremaps.ViewModels;

import androidx.lifecycle.ViewModel;

public class MainTabbetActivityVM extends ViewModel {

    private String _actualEmailUser;

    public MainTabbetActivityVM(){
        _actualEmailUser = "";
    }

    //Get y Set
    public String get_actualEmailUser() {
        return _actualEmailUser;
    }

    public void set_actualEmailUser(String _actualEmailUser) {
        this._actualEmailUser = _actualEmailUser;
    }
}
