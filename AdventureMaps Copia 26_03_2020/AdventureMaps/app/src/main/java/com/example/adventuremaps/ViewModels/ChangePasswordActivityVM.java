package com.example.adventuremaps.ViewModels;

import androidx.lifecycle.ViewModel;

public class ChangePasswordActivityVM extends ViewModel {

    private String _email;
    private String _oldPassword;
    private String _newPassword01;
    private String _newPassword02;

    public ChangePasswordActivityVM(){
        _email = "";
        _oldPassword = "";
        _newPassword01 = "";
        _newPassword02 = "";
    }

    //Get y Set
    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_oldPassword() {
        return _oldPassword;
    }

    public void set_oldPassword(String _oldPassword) {
        this._oldPassword = _oldPassword;
    }

    public String get_newPassword01() {
        return _newPassword01;
    }

    public void set_newPassword01(String _newPassword01) {
        this._newPassword01 = _newPassword01;
    }

    public String get_newPassword02() {
        return _newPassword02;
    }

    public void set_newPassword02(String _newPassword02) {
        this._newPassword02 = _newPassword02;
    }
}
