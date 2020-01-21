package es.iesnervion.yeray.pocketcharacters.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.Entities.ClsCharacter;

public class CharacterListActivityVM extends AndroidViewModel {
    private int _idUser;
    private ArrayList<ClsCharacter> _characterList;

    public CharacterListActivityVM(Application application){
        super(application);
    }

    //Get Y Set
    public int get_idUser() {
        return _idUser;
    }

    public void set_idUser(int _idUser) {
        this._idUser = _idUser;
    }

    public ArrayList<ClsCharacter> get_characterList() {
        return _characterList;
    }

    public void set_characterList(ArrayList<ClsCharacter> _characterList) {
        this._characterList = _characterList;
    }

    //Funciones sobre la base de datos
    /*
    * Interfaz
    * Nombre: setListOfCharacteresFromUser
    * Comentario: Este método nos permite modificar el estado del atributo "_characterList",
    * contendrá la lista de personajes del usuario.
    * Cabecera: public void setListOfCharacteresFromUser()
    * Precondiciones:
    *   -El atributo _idUser debe apuntar al usuario correcto.
    * Postcondiciones: El método carga la lista de personajes del usuario en el atributo "_characterList".
    * */
    public void setListOfCharacteresFromUser(){
        ExecutorService myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            _characterList = new ArrayList<>(AppDataBase.getDataBase(getApplication()).characterDao().getAllCharacters()); //TODO Preguntar a pablo
        });
    }
}
