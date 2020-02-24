package es.iesnervion.yeray.pocketcharacters.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsGameMode;

public class GameModeListActivityVM extends AndroidViewModel {
    private ArrayList<ClsGameMode> _gameModeList;
    private ClsGameMode _gameModeToDelete;
    private boolean _dialogDeleteShowing;
    private boolean _dialogCreateShowing;

    public GameModeListActivityVM(Application application){
        super(application);
        _gameModeList = new ArrayList<ClsGameMode>();
        loadList();
        _gameModeToDelete = null;
        _dialogDeleteShowing = false;
        _dialogCreateShowing = false;
    }

    //Get Y Set
    public ArrayList<ClsGameMode> get_gameModeList() {
        return _gameModeList;
    }

    public void set_gameModeList(ArrayList<ClsGameMode> _gameModeList) {
        this._gameModeList = _gameModeList;
    }

    public ClsGameMode get_gameModeToDelete() {
        return _gameModeToDelete;
    }

    public void set_gameModeToDelete(ClsGameMode _gameModeToDelete) {
        this._gameModeToDelete = _gameModeToDelete;
    }

    public boolean is_dialogDeleteShowing() {
        return _dialogDeleteShowing;
    }

    public void set_dialogDeleteShowing(boolean _dialogDeleteShowing) {
        this._dialogDeleteShowing = _dialogDeleteShowing;
    }

    public boolean is_dialogCreateShowing() {
        return _dialogCreateShowing;
    }

    public void set_dialogCreateShowing(boolean _dialogCreateShowing) {
        this._dialogCreateShowing = _dialogCreateShowing;
    }

    //Funciones sobre la base de datos
    /**
     * Interfaz
     * Nombre: loadList
     * Comentario: Este método nos permite cargar la lista de modos de juego de la base de datos,
     * en el atributo "_gameModeList".
     * Cabecera: public void loadList()
     * Postcondiciones: El método carga la lista de modos de juego en el atributo "_gameModeList" del
     * ViewModel.
     * */
    public void loadList() {
        _gameModeList = new ArrayList<>(AppDataBase.getDataBase(getApplication()).gameModeDao().getAllGameModes());
    }
}
