package es.iesnervion.yeray.pocketcharacters.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObjectType;

public class ObjectTypeListActivityVM extends AndroidViewModel {
    private ArrayList<ClsObjectType> _typeList;
    private int _regionSelected;

    public ObjectTypeListActivityVM(Application application){
        super(application);
        _typeList = new ArrayList<ClsObjectType>();
        loadList();
    }

    //Get Y Set
    public ArrayList<ClsObjectType> get_typeList() {
        return _typeList;
    }

    public void set_typeList(ArrayList<ClsObjectType> _typeList) {
        this._typeList = _typeList;
    }

    public int get_regionSelected() {
        return _regionSelected;
    }

    public void set_regionSelected(int _regionSelected) {
        this._regionSelected = _regionSelected;
    }

    //Funciones sobre la BBDD
    /**
     * Interfaz
     * Nombre: loadList
     * Comentario: Este método nos permite cargar la lista de tipos de objeto de la base de datos,
     * en el atributo "_typeList".
     * Cabecera: public void loadList()
     * Postcondiciones: El método carga la lista de tipos de objeto en el atributo "_typeList" del
     * ViewModel.
     * */
    public void loadList() {
        _typeList = new ArrayList<ClsObjectType>(AppDataBase.getDataBase(getApplication()).objectTypeDao().getAllObjectTypes());
    }
}
