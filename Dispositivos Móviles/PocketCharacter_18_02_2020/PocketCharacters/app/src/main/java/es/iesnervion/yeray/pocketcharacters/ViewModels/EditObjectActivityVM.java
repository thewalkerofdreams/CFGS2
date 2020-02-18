package es.iesnervion.yeray.pocketcharacters.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObject;

public class EditObjectActivityVM extends AndroidViewModel {
    private ClsObject _inObject;//Contiene el objeto con los datos iniciales
    private ClsObject _outObject;//Contiene el objeto con los cambios

    public EditObjectActivityVM(Application application){
        super(application);
        _inObject = new ClsObject();
    }

    //Get Y Set
    public ClsObject get_inObject() {
        return _inObject;
    }

    public void set_inObject(ClsObject _object) {
        this._inObject = _object;
    }

    public ClsObject get_outObject() {
        return _outObject;
    }

    public void set_outObject(ClsObject _outObject) {
        this._outObject = _outObject;
    }
}
