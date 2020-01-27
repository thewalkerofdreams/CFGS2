package es.iesnervion.yeray.pocketcharacters.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsStat;

public class NewCharacterStatActivityVM extends AndroidViewModel {

    private ClsCharacter _actualCharacter;
    private String _statName;
    private String _statValue;
    private ArrayList<ClsStat> _stats;

    public NewCharacterStatActivityVM(Application application){
        super(application);
        _stats = new ArrayList<ClsStat>();
    }

    //Get Y Set
    public ClsCharacter get_actualCharacter() {
        return _actualCharacter;
    }

    public void set_actualCharacter(ClsCharacter _actualCharacter) {
        this._actualCharacter = _actualCharacter;
    }

    public String get_statName() {
        return _statName;
    }

    public void set_statName(String _statName) {
        this._statName = _statName;
    }

    public String get_statValue() {
        return _statValue;
    }

    public void set_statValue(String _statValue) {
        this._statValue = _statValue;
    }

    public ArrayList<ClsStat> get_stats() {
        return _stats;
    }

    public void set_stats(ArrayList<ClsStat> _stats) {
        this._stats = _stats;
    }


    //Funciones sobre la base de datos
    /**
     * Interfaz
     * Nombre: loadList
     * Comentario: Este método nos permite cargar la lista de stats con un gameMode específio
     * de la base de datos en el atributo "_stats".
     * Cabecera: public void loadList()
     * Precondiciones:
     *  -La propiedad _actualCharacter del viewModel debe ser diferente de null
     * Postcondiciones: El método carga la lista de stats en el atributo "_stats" del
     * ViewModel.
     * */
    public void loadList() {
        _stats = new ArrayList<ClsStat>(AppDataBase.getDataBase(getApplication()).statDao().getStatsByGameMode(_actualCharacter.get_gameMode()));
    }
}
