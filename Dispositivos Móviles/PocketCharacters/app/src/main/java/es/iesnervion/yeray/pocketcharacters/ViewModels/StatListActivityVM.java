package es.iesnervion.yeray.pocketcharacters.ViewModels;

import android.app.Application;
import android.widget.BaseAdapter;

import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsStat;

public class StatListActivityVM extends AndroidViewModel {
    private ArrayList<ClsStat> _statList;
    private String _gameMode;
    private ClsStat _statToDelete;
    private boolean _openDialogCreateStat;
    private boolean _openDialogDeleteStat;

    public StatListActivityVM(Application application){
        super(application);
        _statList = new ArrayList<ClsStat>();
        _gameMode = "";
        _openDialogCreateStat = false;
        _openDialogDeleteStat = false;
        _statToDelete = null;
    }

    //Get Y Set
    public ArrayList<ClsStat> get_statList() {
        return _statList;
    }

    public void set_statList(ArrayList<ClsStat> _statList) {
        this._statList = _statList;
    }

    public String get_gameMode() {
        return _gameMode;
    }

    public void set_gameMode(String _gameMode) {
        this._gameMode = _gameMode;
        loadList();//Cargamos la lista de stats a partir del nuevo gameMode
    }

    public boolean is_openDialogCreateStat() {
        return _openDialogCreateStat;
    }

    public void set_openDialogCreateStat(boolean _openDialogCreateStat) {
        this._openDialogCreateStat = _openDialogCreateStat;
    }

    public boolean is_openDialogDeleteStat() {
        return _openDialogDeleteStat;
    }

    public void set_openDialogDeleteStat(boolean _openDialogDeleteStat) {
        this._openDialogDeleteStat = _openDialogDeleteStat;
    }

    public ClsStat get_statToDelete() {
        return _statToDelete;
    }

    public void set_statToDelete(ClsStat _statToDelete) {
        this._statToDelete = _statToDelete;
    }

    //Funciones sobre la base de datos
    /**
     * Interfaz
     * Nombre: loadList
     * Comentario: Este método nos permite cargar la lista de stats de un GameMode especificado por la
     * propiedad _gameMode del viewModel, en el atributo "_statList".
     * Cabecera: public void loadList()
     * Postcondiciones: El método carga la lista de stats en el atributo "_statList" del
     * ViewModel.
     * */
    public void loadList() {
        _statList = new ArrayList<ClsStat>(AppDataBase.getDataBase(getApplication()).statDao().getStatsByGameMode(_gameMode));
    }
}
