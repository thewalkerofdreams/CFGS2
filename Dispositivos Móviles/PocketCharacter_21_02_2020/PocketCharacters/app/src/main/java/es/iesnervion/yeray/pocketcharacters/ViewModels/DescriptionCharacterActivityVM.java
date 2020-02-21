package es.iesnervion.yeray.pocketcharacters.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.io.Serializable;

import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesModels.ClsCharacterModel;

public class DescriptionCharacterActivityVM extends AndroidViewModel implements Serializable {

    private ClsCharacterModel _actualCharacter;
    public DescriptionCharacterActivityVM(Application application){
        super(application);
    }

    //Get y Set
    public ClsCharacterModel get_actualCharacter() {
        return _actualCharacter;
    }

    public void set_actualCharacter(ClsCharacter _actualCharacter) {
        this._actualCharacter = new ClsCharacterModel(_actualCharacter, getApplication());
    }
}
