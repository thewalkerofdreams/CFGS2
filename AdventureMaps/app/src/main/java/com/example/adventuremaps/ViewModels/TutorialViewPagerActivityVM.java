package com.example.adventuremaps.ViewModels;

import androidx.lifecycle.ViewModel;

public class TutorialViewPagerActivityVM extends ViewModel {

    private int _actualSubPageSection1;

    public void TutorialViewPagerActivityVM(){
        _actualSubPageSection1 = 0;
    }

    //Get y Set
    public int get_actualSubPageSection1() {
        return _actualSubPageSection1;
    }

    public void set_actualSubPageSection1(int _actualSubPageSection1) {
        this._actualSubPageSection1 = _actualSubPageSection1;
    }
}
