package es.iesnervion.yeray.pocketcharacters.ViewModels;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.lifecycle.AndroidViewModel;
import androidx.room.Ignore;

import java.io.Serializable;
import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObject;
import es.iesnervion.yeray.pocketcharacters.EntitiesModels.ClsCharacterModel;
public class CharacterDetailsActivityVM extends AndroidViewModel implements Serializable {

    private ClsCharacterModel _inCharacter;
    private ClsCharacterModel _outCharacter;

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

    /*
    * Interfaz
    * Nombre: updateCharacter
    * Comentario: Este método nos permite actualizar un personaje. El personaje
    * obtendrá los datos del atributo "outCharacter".
    * Cabecera: public void updateCharacter()
    * Postcondiciones: El método modifica el estado de un personaje en la base de datos.
    * */
    public void updateCharacter(){
        AppDataBase.getDataBase(getApplication()).characterDao().updateCharacter(_outCharacter.get_character());
    }
}
