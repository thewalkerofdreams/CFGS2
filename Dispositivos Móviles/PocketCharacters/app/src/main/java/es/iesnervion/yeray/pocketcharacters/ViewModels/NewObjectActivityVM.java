package es.iesnervion.yeray.pocketcharacters.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObjectType;

public class NewObjectActivityVM extends AndroidViewModel {

    private String _objectType;
    private String _objectName;
    private String _objectDescription;
    private ArrayList<ClsObjectType> _objectTypes;
    private String _actualGameMode;

    //Constructor
    public NewObjectActivityVM(Application application){
        super(application);
        _objectTypes = new ArrayList<>();
        loadList();
    }

    //Get Y Set
    public String get_objectType() {
        return _objectType;
    }

    public void set_objectType(String _objectType) {
        this._objectType = _objectType;
    }

    public String get_objectName() {
        return _objectName;
    }

    public void set_objectName(String _objectName) {
        this._objectName = _objectName;
    }

    public String get_objectDescription() {
        return _objectDescription;
    }

    public void set_objectDescription(String _objectDescription) {
        this._objectDescription = _objectDescription;
    }

    public ArrayList<ClsObjectType> get_objectTypes() {
        return _objectTypes;
    }

    public void set_objectTypes(ArrayList<ClsObjectType> _objectTypes) {
        this._objectTypes = _objectTypes;
    }

    public String get_actualGameMode() {
        return _actualGameMode;
    }

    public void set_actualGameMode(String _actualGameMode) {
        this._actualGameMode = _actualGameMode;
    }

    //Funciones sobre la base de datos
    /**
     * Interfaz
     * Nombre: loadList
     * Comentario: Este método nos permite cargar la lista tipos de objetos de la base de datos,
     * en el atributo "_objectTypes".
     * Cabecera: public void loadList()
     * Postcondiciones: El método carga la lista de tipos de objeto en el atributo "_objectTypes" del
     * ViewModel.
     * */
    public void loadList() {
        _objectTypes = new ArrayList<ClsObjectType>(AppDataBase.getDataBase(getApplication()).objectTypeDao().getAllObjectTypes());
    }
}
