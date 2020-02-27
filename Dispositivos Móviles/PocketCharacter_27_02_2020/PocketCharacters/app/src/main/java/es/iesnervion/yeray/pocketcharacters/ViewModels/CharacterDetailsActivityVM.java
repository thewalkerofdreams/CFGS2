package es.iesnervion.yeray.pocketcharacters.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.io.Serializable;

import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesModels.ClsCharacterModel;
public class CharacterDetailsActivityVM extends AndroidViewModel implements Serializable {

    private ClsCharacterModel _inCharacter;
    private ClsCharacterModel _outCharacter;
    //Constructor
    public CharacterDetailsActivityVM(Application application){
        super(application);
    }

    //Get Y Set
    public ClsCharacterModel get_inCharacter() {
        return _inCharacter;
    }

    public void set_inCharacter(ClsCharacter character) {
        this._inCharacter = new ClsCharacterModel(character, getApplication());
    }

    public ClsCharacterModel get_outCharacter() {
        return _outCharacter;
    }

    public void set_outCharacter(ClsCharacter character) {
        this._outCharacter = new ClsCharacterModel(character, getApplication());
    }
}
