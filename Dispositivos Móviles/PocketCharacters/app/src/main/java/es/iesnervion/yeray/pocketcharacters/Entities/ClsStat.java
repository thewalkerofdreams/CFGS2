package es.iesnervion.yeray.pocketcharacters.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (foreignKeys = {@ForeignKey(entity = ClsGameMode.class, parentColumns = "name", childColumns = "gameMode"),
        @ForeignKey(entity = ClsCharacter.class, parentColumns = "id", childColumns = "characterId")})
public class ClsStat {
    @PrimaryKey
    @ColumnInfo(name = "name")
    private String _name;
    @ColumnInfo(name = "value")
    private String _value;//no sabemos si el valor de la estadística será numérico
    @ColumnInfo(name = "characterId")
    private int _characterId;
    @ColumnInfo(name = "gameMode")
    private String _gameMode;

    //Constructores
    public ClsStat(){
        _name = "DEFAULT";
        _value = "0";
        _characterId = 0;
        _gameMode = "DEFAULT";
    }
    @Ignore
    public ClsStat(String name, String value, int characterId, String gameMode){
        _name = name;
        _value = value;
        _characterId = characterId;
        _gameMode = gameMode;
    }

    //Get Y Set
    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_value() {
        return _value;
    }

    public void set_value(String _value) {
        this._value = _value;
    }

    public int get_characterId() {
        return _characterId;
    }

    public void set_characterId(int _characterId) {
        this._characterId = _characterId;
    }

    public String get_gameMode() {
        return _gameMode;
    }

    public void set_gameMode(String _gameMode) {
        this._gameMode = _gameMode;
    }
}
