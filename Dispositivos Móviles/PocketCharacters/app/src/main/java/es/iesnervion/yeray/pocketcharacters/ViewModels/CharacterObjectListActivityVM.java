package es.iesnervion.yeray.pocketcharacters.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacterAndStat;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObject;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObjectAndCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsStat;
import es.iesnervion.yeray.pocketcharacters.EntitiesModels.ClsObjectAndQuantity;
import es.iesnervion.yeray.pocketcharacters.EntitiesModels.ClsStatModel;

public class CharacterObjectListActivityVM extends AndroidViewModel {
    private ClsCharacter _character;
    MutableLiveData<ArrayList<ClsObjectAndQuantity>> _objectList;
    MutableLiveData<ClsObjectAndQuantity> _objectSelected;

    public CharacterObjectListActivityVM(Application application) {
        super(application);
        _objectList = new MutableLiveData<ArrayList<ClsObjectAndQuantity>>();
        _objectList.setValue(new ArrayList<ClsObjectAndQuantity>());
        _objectSelected = new MutableLiveData<ClsObjectAndQuantity>();
    }

    //Get Y Set
    public ClsCharacter get_character() {
        return _character;
    }

    public void set_character(ClsCharacter _character) {
        this._character = _character;
        loadObjectList();
    }

    public LiveData<ArrayList<ClsObjectAndQuantity>> get_objectList() {
        return _objectList;
    }

    public void set_objectList(ArrayList<ClsObjectAndQuantity> objectList) {
        this._objectList.setValue(objectList);
    }

    public LiveData<ClsObjectAndQuantity> get_objectSelected() {
        return _objectSelected;
    }

    public void set_objectSelected(ClsObjectAndQuantity objectSelected) {
        this._objectSelected.setValue(objectSelected);
    }

    /*
     * Interfaz
     * Nombre: loadObjectList
     * Comentario: Este método nos permite cargar el listado de objetos
     * de la base de datos, según el id de personaje de este viewmodel.
     * Cabecera: private void loadObjectList()
     * Postcondiciones: El método carga la lista de objetos.
     * */
    public void loadObjectList(){
        //TODO Necesito saber como hacer los innner join con room para quitar esta burrada
        _objectList.setValue(new ArrayList<ClsObjectAndQuantity>());
        ArrayList<ClsObject> objects = new ArrayList<>(AppDataBase.getDataBase(getApplication()).objectDao().getObjectsByGameMode(_character.get_gameMode()));

        for(int i = 0; i < objects.size(); i++){
            ClsObjectAndCharacter objectAndCharacter = AppDataBase.getDataBase(getApplication()).objectAndCharacterDao().getObjectAndCharacter(
                    _character.get_id(), objects.get(i).get_id());
            if(objectAndCharacter != null){
                ClsObjectAndQuantity objectAndQuantity = new ClsObjectAndQuantity(objects.get(i), objectAndCharacter.get_quantity());
                _objectList.getValue().add(objectAndQuantity);
            }
        }
    }
}
