package es.iesnervion.yeray.pocketcharacters.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObject;
import es.iesnervion.yeray.pocketcharacters.EntitiesModels.ClsObjectAndQuantity;
import es.iesnervion.yeray.pocketcharacters.EntitiesModels.ClsObjectTuple;

public class CharacterObjectListActivityVM extends AndroidViewModel {
    private ClsCharacter _character;
    private MutableLiveData<ArrayList<ClsObjectAndQuantity>> _objectList;
    private MutableLiveData<ClsObjectAndQuantity> _objectSelected;

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

    /**
     * Interfaz
     * Nombre: loadObjectList
     * Comentario: Este método nos permite cargar el listado de objetos
     * de la base de datos, según el id de personaje del atributo "_character"
     * de este viewmodel.
     * Cabecera: private void loadObjectList()
     * Postcondiciones: El método carga la lista de objetos.
     * */
    public void loadObjectList(){
        ArrayList<ClsObjectTuple> tuples = new ArrayList<>(AppDataBase.getDataBase(getApplication()).objectDao().getObjectListFromCharacter(_character.get_id()));
        _objectList.setValue(new ArrayList<ClsObjectAndQuantity>());
        for (ClsObjectTuple object: tuples) {
            _objectList.getValue().add(new ClsObjectAndQuantity(new ClsObject(object.getId(), object.getType(), object.getName(), object.getDescription(), object.getGameMode()), object.getQuantity()));
        }
    }
}
