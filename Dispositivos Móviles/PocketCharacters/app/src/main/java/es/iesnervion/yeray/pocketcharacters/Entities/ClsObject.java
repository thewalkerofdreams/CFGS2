package es.iesnervion.yeray.pocketcharacters.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;

public class ClsObject {
    @ColumnInfo(name = "type")
    private String _type;
    @ColumnInfo(name = "name")
    private String _name;
    @ColumnInfo(name = "description")
    private String _description;
    @ColumnInfo(name = "gameMode")
    private String _gameMode;

    //Constructor
    @Ignore
    public ClsObject(){
        _type = "DEFAULT";
        _name = "DEFAULT";
        _description = "DEFAULT";
        _gameMode = "DEFAULT";
    }
    @Ignore
    public ClsObject(String type, String name, String description, String gameMode){
        _type = type;
        _name = name;
        _description = description;
        _gameMode = gameMode;
    }

    //Get Y Set
    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public String get_gameMode() {
        return _gameMode;
    }

    public void set_gameMode(String _gameMode) {
        this._gameMode = _gameMode;
    }
}
