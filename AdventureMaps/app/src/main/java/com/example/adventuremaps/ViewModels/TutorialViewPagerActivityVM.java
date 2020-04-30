package com.example.adventuremaps.ViewModels;

import androidx.lifecycle.ViewModel;

public class TutorialViewPagerActivityVM extends ViewModel {

    private int _actualSubPageSection1;
    private int _actualSubPageSection2;
    private int _actualSubPageSection3;
    private int _actualSubPageSection4;
    private int _actualSubPageSection5;

    public void TutorialViewPagerActivityVM(){
        _actualSubPageSection1 = 0;
        _actualSubPageSection2 = 0;
        _actualSubPageSection3 = 0;
        _actualSubPageSection4 = 0;
        _actualSubPageSection5 = 0;
    }

    //Get y Set
    public int get_actualSubPageSection1() {
        return _actualSubPageSection1;
    }

    public void set_actualSubPageSection1(int _actualSubPageSection1) {
        this._actualSubPageSection1 = _actualSubPageSection1;
    }

    public int get_actualSubPageSection2() {
        return _actualSubPageSection2;
    }

    public void set_actualSubPageSection2(int _actualSubPageSection2) {
        this._actualSubPageSection2 = _actualSubPageSection2;
    }

    public int get_actualSubPageSection3() {
        return _actualSubPageSection3;
    }

    public void set_actualSubPageSection3(int _actualSubPageSection3) {
        this._actualSubPageSection3 = _actualSubPageSection3;
    }

    public int get_actualSubPageSection4() {
        return _actualSubPageSection4;
    }

    public void set_actualSubPageSection4(int _actualSubPageSection4) {
        this._actualSubPageSection4 = _actualSubPageSection4;
    }

    public int get_actualSubPageSection5() {
        return _actualSubPageSection5;
    }

    public void set_actualSubPageSection5(int _actualSubPageSection5) {
        this._actualSubPageSection5 = _actualSubPageSection5;
    }
}
