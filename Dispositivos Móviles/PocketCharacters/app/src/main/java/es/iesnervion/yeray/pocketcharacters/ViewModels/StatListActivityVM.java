package es.iesnervion.yeray.pocketcharacters.ViewModels;

import android.app.Application;
import android.widget.BaseAdapter;

import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsStat;

public class StatListActivityVM extends AndroidViewModel {
    private ArrayList<ClsStat> _statList;

    public StatListActivityVM(Application application){
        super(application);
        _statList = new ArrayList<ClsStat>();
    }

    //Get Y Set
    public ArrayList<ClsStat> get_statList() {
        return _statList;
    }

    public void set_statList(ArrayList<ClsStat> _statList) {
        this._statList = _statList;
    }

    //Funciones sobre la base de datos
    /*
     * Interfaz
     * Nombre: loadList
     * Comentario: Este método nos permite cargar la lista de stats de un GameMode de la base de datos,
     * en el atributo "_statList".
     * Cabecera: public void loadList(String gameMode)
     * Entrada:
     *  -String gameMode
     * Postcondiciones: El método carga la lista de stats en el atributo "_statList" del
     * ViewModel.
     * */
    public void loadList(String gameMode) {
        _statList = new ArrayList<ClsStat>(AppDataBase.getDataBase(getApplication()).statDao().getStatsByGameMode(gameMode));
    }
}
