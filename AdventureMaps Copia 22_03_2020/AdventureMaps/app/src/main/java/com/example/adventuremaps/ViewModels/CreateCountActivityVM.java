package com.example.adventuremaps.ViewModels;

import androidx.lifecycle.ViewModel;

public class CreateCountActivityVM extends ViewModel {

    private String _nickName;
    private String _email;
    private String _password01;
    private String _password02;

    public CreateCountActivityVM(){
        _nickName = "";
        _email = "";
        _password01 = "";
        _password02 = "";
    }

    //Get y Set
    public String get_nickName() {
        return _nickName;
    }

    public void set_nickName(String _nickName) {
        this._nickName = _nickName;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_password01() {
        return _password01;
    }

    public void set_password01(String _password01) {
        this._password01 = _password01;
    }

    public String get_password02() {
        return _password02;
    }

    public void set_password02(String _password02) {
        this._password02 = _password02;
    }
}
