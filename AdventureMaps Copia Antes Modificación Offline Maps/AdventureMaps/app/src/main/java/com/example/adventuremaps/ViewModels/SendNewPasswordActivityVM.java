package com.example.adventuremaps.ViewModels;

import androidx.lifecycle.ViewModel;

public class SendNewPasswordActivityVM extends ViewModel {

    private String _email;

    public SendNewPasswordActivityVM(){
        _email = "";
    }

    //Get y Set
    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }
}
