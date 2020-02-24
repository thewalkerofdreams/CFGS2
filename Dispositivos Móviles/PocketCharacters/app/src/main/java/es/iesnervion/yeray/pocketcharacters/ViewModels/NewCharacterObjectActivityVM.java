package es.iesnervion.yeray.pocketcharacters.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObject;

public class NewCharacterObjectActivityVM extends AndroidViewModel {

    private ClsCharacter _actualCharacter;
    private String _objectName;
    private String _quantity;
    private ArrayList<ClsObject> _objects;

    //Constructor
    public NewCharacterObjectActivityVM(Application application){
        super(application);
        _objects = new ArrayList<ClsObject>();
    }

    //Get Y Set
    public ClsCharacter get_actualCharacter() {
        return _actualCharacter;
    }

    public void set_actualCharacter(ClsCharacter _actualCharacter) {
        this._actualCharacter = _actualCharacter;
    }

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
    /**
     * Interfaz
     * Nombre: loadList
     * Comentario: Este método nos permite cargar la lista de objetos de la base de datos,
     * en el atributo "_objects".
     * Cabecera: public void loadList()
     * Precondiciones:
     *  -La propiedad _actualCharacter debe ser diferente de null
     * Postcondiciones: El método carga la lista de objetos en el atributo "_objects" del
     * ViewModel.
     * */
    public void loadList() {
        _objects = new ArrayList<ClsObject>(AppDataBase.getDataBase(getApplication()).objectDao().getObjectsByGameMode(_actualCharacter.get_gameMode()));
    }
}
