package es.iesnervion.yeray.pocketcharacters.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsGameMode;

public class NewCharacterActivityVM extends AndroidViewModel {

    private String _gameMode;
    private String _characterName;
    private String _chapterName;
    private String _story;
    private ArrayList<ClsGameMode> _gameModes;

    public NewCharacterActivityVM(Application application){
        super(application);
        _gameModes = new ArrayList<>();
        loadList();
    }

    //Get Y Set
    public String get_gameMode() {
        return _gameMode;
    }

    public void set_gameMode(String _gameMode) {
        this._gameMode = _gameMode;
    }

    public String get_characterName() {
        return _characterName;
    }

    public void set_characterName(String _characterName) {
        this._characterName = _characterName;
    }

    public String get_chapterName() {
        return _chapterName;
    }

    public void set_chapterName(String _chapterName) {
        this._chapterName = _chapterName;
    }

    public String get_story() {
        return _story;
    }

    public void set_story(String _story) {
        this._story = _story;
    }

    public ArrayList<ClsGameMode> get_gameModes() {
        return _gameModes;
    }

    public void set_gameModes(ArrayList<ClsGameMode> _gameModes) {
        this._gameModes = _gameModes;
    }

    //Funciones sobre la base de datos
    /*
     * Interfaz
     * Nombre: loadList
     * Comentario: Este método nos permite cargar la lista de GameModes de la base de datos,
     * en el atributo "_gameModes".
     * Cabecera: public void loadList()
     * Postcondiciones: El método carga la lista de GameModes en el atributo "_gameModes" del
     * ViewModel.
     * */
    public void loadList() {
        _gameModes = new ArrayList<>(AppDataBase.getDataBase(getApplication()).gameModeDao().getAllGameModes());
    }
}
