package es.iesnervion.yeray.pocketcharacters.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsGameMode;

public class GameModeListActivityVM extends AndroidViewModel {
    private ArrayList<ClsGameMode> _gameModeList;

    public GameModeListActivityVM(Application application){
        super(application);
        _gameModeList = new ArrayList<ClsGameMode>();
        loadList();
    }

    //Get Y Set
    public ArrayList<ClsGameMode> get_gameModeList() {
        return _gameModeList;
    }

    public void set_gameModeList(ArrayList<ClsGameMode> _gameModeList) {
        this._gameModeList = _gameModeList;
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
