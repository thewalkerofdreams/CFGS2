package com.example.adventuremaps.ViewModels;

import androidx.lifecycle.ViewModel;

public class MainActivityVM extends ViewModel {

    private String _email;
    private String _password;

    public MainActivityVM(){
        _email = "";
        _password = "";
    }

    //Get y Set
    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_password() {
        return _password;
    }

    public void set_password(String _password) {
        this._password = _password;
    }
}
