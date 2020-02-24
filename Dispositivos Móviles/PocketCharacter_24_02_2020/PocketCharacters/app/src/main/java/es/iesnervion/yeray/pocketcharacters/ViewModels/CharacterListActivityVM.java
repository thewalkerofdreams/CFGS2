package es.iesnervion.yeray.pocketcharacters.ViewModels;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacter;

public class CharacterListActivityVM extends AndroidViewModel implements Serializable {
    private ArrayList<ClsCharacter> _characterList;

    public CharacterListActivityVM(Application application){
        super(application);
        _characterList = new ArrayList<ClsCharacter>();
        loadList();
    }

    //Get Y Set
    public ArrayList<ClsCharacter> get_characterList() {
        return _characterList;
    }

    public void set_characterList(ArrayList<ClsCharacter> _characterList) {
        this._characterList = _characterList;
    }

    //Funciones sobre la base de datos
    /**
    * Interfaz
    * Nombre: loadList
    * Comentario: Este método nos permite cargar la lista de personajes de la base de datos,
    * en el atributo "_characterList".
    * Cabecera: public void loadList()
    * Postcondiciones: El método carga la lista de personajes en el atributo "_characterList" del
    * ViewModel.
    * */
    public void loadList(){
        _characterList = new ArrayList<ClsCharacter>(AppDataBase.getDataBase(getApplication()).characterDao().getAllCharacters());
    }

    /**
    * Interfaz
    * Nombre: deleteCharacter
    * Comentario: Este método nos permite eliminar a un personaje de la base de datos de la aplicación
    * junto con todas sus dependencias (Stats y objetos).
    * Cabecera: public void deleteCharacter(ClsCharacter character)
    * Entrada:
    *   -Character character
    * Postcondiciones: El método elimina a un personaje de la base de datos.
    * */
    public void deleteCharacter(ClsCharacter character){
        AppDataBase.getDataBase(getApplication()).characterAndStatDao().deleteCharacterAndStat(character.get_id());
        AppDataBase.getDataBase(getApplication()).objectAndCharacterDao().deleteObjectAndCharacter(character.get_id());
        AppDataBase.getDataBase(getApplication()).characterDao().deleteCharacter(character);
    }
}
