package es.iesnervion.yeray.pocketcharacters.EntitiesModels;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacterAndStat;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsStat;

public class ClsCharacterModel implements Serializable {
    private ClsCharacter _character;
    private ArrayList<ClsStatModel> _stats;
    private ArrayList<ClsObjectAndQuantity> _objects;
    private Context _context;

    public ClsCharacterModel(ClsCharacter character, Context context){
        _character = character;
        _stats = new ArrayList<>();
        _objects = new ArrayList<>();
        _context = context;
        loadStats();
    }

    //Get Y Set
    public ClsCharacter get_character() {
        return _character;
    }

    public void set_character(ClsCharacter _character) {
        this._character = _character;
    }

    public ArrayList<ClsStatModel> get_stats() {
        return _stats;
    }

    public void set_stats(ArrayList<ClsStatModel> _stats) {
        this._stats = _stats;
    }

    public ArrayList<ClsObjectAndQuantity> get_objects() {
        return _objects;
    }

    public void set_objects(ArrayList<ClsObjectAndQuantity> _objects) {
        this._objects = _objects;
    }

    public Context get_context() {
        return _context;
    }

    public void set_context(Context _context) {
        this._context = _context;
    }

    /**
    * Interfaz
    * Nombre: loadStats
    * Comentario: Este método nos permite cargar los stats del personaje.
    * Cabecera: public void loadStats()
    * Postcondiciones: El método carga los stats del personaje en el atributo "-stats".
    * */
    public void loadStats(){
        /*ArrayList<ClsStat> stats = new ArrayList<>(AppDataBase.getDataBase(_context).statDao().getStatsByGameMode(_character.get_gameMode()));

        for(int i = 0; i < stats.size(); i++){
            ClsCharacterAndStat characterAndStats = AppDataBase.getDataBase(_context).characterAndStatDao().getCharacterAndStat(
                    _character.get_id(), stats.get(i).get_id());
            if(characterAndStats != null){
                ClsStatModel statModel = new ClsStatModel(stats.get(i).get_name(), characterAndStats.get_value());
                _stats.add(statModel);
            }
        }*/
        _stats = new ArrayList<>(AppDataBase.getDataBase(_context).characterAndStatDao().getStatsAndValueByCharacter(_character.get_id()));
    }
}
