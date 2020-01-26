package es.iesnervion.yeray.pocketcharacters.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObject;

public class NewCharacterObjectActivityVM extends AndroidViewModel {

    private String _objectName;
    private String _quantity;
    private ArrayList<ClsObject> _objects;

    public NewCharacterObjectActivityVM(Application application){
        super(application);
        _objects = new ArrayList<ClsObject>();
    }

    //Get Y Set
    public String get_objectName() {
        return _objectName;
    }

    public void set_objectName(String _objectName) {
        this._objectName = _objectName;
    }

    public String get_quantity() {
        return _quantity;
    }

    public void set_quantity(String _quantity) {
        this._quantity = _quantity;
    }

    public ArrayList<ClsObject> get_objects() {
        return _objects;
    }

    public void set_objects(ArrayList<ClsObject> _objects) {
        this._objects = _objects;
    }

    //Funciones sobre la base de datos
    /*
     * Interfaz
     * Nombre: loadList
     * Comentario: Este método nos permite cargar la lista de objetos de la base de datos,
     * en el atributo "_objects".
     * Cabecera: public void loadList()
     * Postcondiciones: El método carga la lista de objetos en el atributo "_objects" del
     * ViewModel.
     * */
    public void loadList(String gameMode) {
        _objects = new ArrayList<ClsObject>(AppDataBase.getDataBase(getApplication()).objectDao().getObjectsByGameMode(gameMode));
    }
}
