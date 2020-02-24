package es.iesnervion.yeray.pocketcharacters.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.Serializable;
import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesModels.ClsStatModel;

public class CharacterStatsListActivityVM extends AndroidViewModel implements Serializable {
    private ClsCharacter _character;
    private MutableLiveData<ArrayList<ClsStatModel>> _statList;
    private MutableLiveData<ClsStatModel> _statSelected;

    public CharacterStatsListActivityVM(Application application) {
        super(application);
        _statList = new MutableLiveData<ArrayList<ClsStatModel>>();
        _statList.setValue(new ArrayList<ClsStatModel>());
        _statSelected = new MutableLiveData<ClsStatModel>();
    }

    //Get Y Set
    public ClsCharacter get_character() {
        return _character;
    }

    public void set_character(ClsCharacter _character) {
        this._character = _character;
        loadStatList();
    }

    public LiveData<ArrayList<ClsStatModel>> get_statList() {
        return _statList;
    }

    public void set_statList(ArrayList<ClsStatModel> statList) {
        this._statList.setValue(statList);
    }

    public LiveData<ClsStatModel> get_statSelected() {
        return _statSelected;
    }

    public void set_statSelected(ClsStatModel statSelected) {
        this._statSelected.setValue(statSelected);
    }

    /**
     * Interfaz
     * Nombre: loadStatList
     * Comentario: Este método nos permite cargar el listado de stats
     * de la base de datos, según el id de personaje del atributo "_character"
     * de este viewmodel.
     * Cabecera: private void loadStatList()
     * Postcondiciones: El método carga la lista de stats.
     * */
    public void loadStatList(){
        _statList.setValue(new ArrayList<>(AppDataBase.getDataBase(getApplication()).characterAndStatDao().getStatsAndValueByCharacter(_character.get_id())));
    }
}
