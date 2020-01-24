package es.iesnervion.yeray.pocketcharacters.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacter;

public class CharacterListActivityVM extends AndroidViewModel {
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
    /*
    * Interfaz
    * Nombre: loadList
    * Comentario: Este método nos permite cargar la lista de personajes de la base de datos,
    * en el atributo "_characterList".
    * Cabecera: private void loadList()
    * Postcondiciones: El método carga la lista de persoanjes en el atributo "_characterList" del
    * ViewModel.
    * */
    private void loadList(){
        ExecutorService myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            _characterList = new ArrayList<ClsCharacter>(AppDataBase.getDataBase(getApplication()).characterDao().getAllCharacters());
        });
    }
}
