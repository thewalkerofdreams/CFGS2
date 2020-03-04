package es.iesnervion.yeray.pocketcharacters.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObject;

public class ObjectListSimpleActivityVM extends AndroidViewModel {

    private ArrayList<ClsObject> _objectList;
    private int _regionSelected;
    private String _actualGameMode;
    private ClsObject _objectToDelete;
    private boolean _openDialogDeleteObject;

    //Constructor
    public ObjectListSimpleActivityVM(Application application){
        super(application);
        _objectList = new ArrayList<ClsObject>();
        _actualGameMode = "";
        _objectToDelete = null;
        _openDialogDeleteObject = false;
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

    public String get_actualGameMode() {
        return _actualGameMode;
    }

    public void set_actualGameMode(String _actualGameMode) {
        this._actualGameMode = _actualGameMode;
        loadList();
    }

    public ClsObject get_objectToDelete() {
        return _objectToDelete;
    }

    public void set_objectToDelete(ClsObject _objectToDelete) {
        this._objectToDelete = _objectToDelete;
    }

    public boolean is_openDialogDeleteObject() {
        return _openDialogDeleteObject;
    }

    public void set_openDialogDeleteObject(boolean _openDialogDeleteObject) {
        this._openDialogDeleteObject = _openDialogDeleteObject;
    }

    //Funciones sobre la BBDD
    /**
     * Interfaz
     * Nombre: loadList
     * Comentario: Este método nos permite cargar la lista de objetos de la base de datos,
     * con un GameMode específico en el atributo "_objectList".
     * Cabecera: public void loadList()
     * Postcondiciones: El método carga la lista de objetos en el atributo "_objectList" del
     * ViewModel.
     * */
    public void loadList() {
        _objectList = new ArrayList<ClsObject>(AppDataBase.getDataBase(getApplication()).objectDao().getObjectsByGameMode(_actualGameMode));
    }
}
