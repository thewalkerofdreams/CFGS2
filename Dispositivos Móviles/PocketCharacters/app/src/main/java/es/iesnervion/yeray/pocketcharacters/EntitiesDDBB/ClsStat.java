package es.iesnervion.yeray.pocketcharacters.EntitiesDDBB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (foreignKeys = {@ForeignKey(entity = ClsGameMode.class, parentColumns = "name", childColumns = "gameMode")})
public class ClsStat {
    @PrimaryKey
    @ColumnInfo(name = "name")
    private String _name;
    @ColumnInfo(name = "gameMode")
    private String _gameMode;

    //Constructores
    public ClsStat(){
    }
    @Ignore
    public ClsStat(String name, String gameMode){
        _name = name;
        _gameMode = gameMode;
    }

    //Get Y Set
    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_gameMode() {
        return _gameMode;
    }

    public void set_gameMode(String _gameMode) {
        this._gameMode = _gameMode;
    }
}
