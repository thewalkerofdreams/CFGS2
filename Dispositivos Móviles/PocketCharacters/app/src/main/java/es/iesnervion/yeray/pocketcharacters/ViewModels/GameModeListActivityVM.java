package es.iesnervion.yeray.pocketcharacters.ViewModels;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import es.iesnervion.yeray.pocketcharacters.Activities.GameModeListActivity;
import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.DDBB.MethodsDDBB;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsGameMode;
import es.iesnervion.yeray.pocketcharacters.R;

public class GameModeListActivityVM extends AndroidViewModel {
    private ArrayList<ClsGameMode> _gameModeList;

    public GameModeListActivityVM(Application application){
        super(application);
        _gameModeList = new ArrayList<ClsGameMode>();
        loadList();
    }

    //Get Y Set
    public ArrayList<ClsGameMode> get_gameModeList() {
        return _gameModeList;
    }

    public void set_gameModeList(ArrayList<ClsGameMode> _gameModeList) {
        this._gameModeList = _gameModeList;
    }

    //Funciones sobre la base de datos
    /*
     * Interfaz
     * Nombre: loadList
     * Comentario: Este método nos permite cargar la lista de modos de juego de la base de datos,
     * en el atributo "_gameModeList".
     * Cabecera: public void loadList()
     * Postcondiciones: El método carga la lista de modos de juego en el atributo "_gameModeList" del
     * ViewModel.
     * */
    public void loadList() {
        _gameModeList = new ArrayList<>(AppDataBase.getDataBase(getApplication()).gameModeDao().getAllGameModes());
    }
}
