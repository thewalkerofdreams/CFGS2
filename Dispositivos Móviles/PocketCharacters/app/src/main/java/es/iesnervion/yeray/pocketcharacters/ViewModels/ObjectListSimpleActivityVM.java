package es.iesnervion.yeray.pocketcharacters.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObject;

public class ObjectListSimpleActivityVM extends AndroidViewModel {

    private ArrayList<ClsObject> _objectList;
    private int _regionSelected;

    public ObjectListSimpleActivityVM(Application application){
        super(application);
        _objectList = new ArrayList<ClsObject>();
    }

    //Get Y Set
    public ArrayList<ClsObject> get_objectList() {
        return _objectList;
    }

    public void set_objectList(ArrayList<ClsObject> _objectList) {
        this._objectList = _objectList;
    }

    public int get_regionSelected() {
        return _regionSelected;
    }

    public void set_regionSelected(int _regionSelected) {
        this._regionSelected = _regionSelected;
    }

    //Funciones sobre la BBDD
    /*
     * Interfaz
     * Nombre: loadList
     * Comentario: Este método nos permite cargar la lista de objetos de la base de datos,
     * con un GameMode específico en el atributo "_objectList".
     * Cabecera: public void loadList()
     * Postcondiciones: El método carga la lista de objetos en el atributo "_objectList" del
     * ViewModel.
     * */
    public void loadList(String gameMode) {
        _objectList = new ArrayList<ClsObject>(AppDataBase.getDataBase(getApplication()).objectDao().getObjectsByGameMode(gameMode));
    }
}
