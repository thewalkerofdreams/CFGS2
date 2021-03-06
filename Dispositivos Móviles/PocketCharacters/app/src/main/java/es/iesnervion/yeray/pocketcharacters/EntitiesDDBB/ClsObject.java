package es.iesnervion.yeray.pocketcharacters.EntitiesDDBB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity (indices = {@Index("gameMode"), @Index("type")}, foreignKeys = {@ForeignKey(entity = ClsGameMode.class, parentColumns = "name", childColumns = "gameMode"),
        @ForeignKey(entity = ClsObjectType.class, parentColumns = "name", childColumns = "type")})
public class ClsObject implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int _id;
    @ColumnInfo(name = "type")
    private String _type;
    @ColumnInfo(name = "name")
    private String _name;
    @ColumnInfo(name = "description")
    private String _description;
    @ColumnInfo(name = "gameMode")
    private String _gameMode;

    //Constructor
    public ClsObject(){
    }

    @Ignore
    public ClsObject(String type, String name, String description, String gameMode){
        _type = type;
        _name = name;
        _description = description;
        _gameMode = gameMode;
    }

    @Ignore
    public ClsObject(int id, String type, String name, String description, String gameMode){
        _id = id;
        _type = type;
        _name = name;
        _description = description;
        _gameMode = gameMode;
    }

    //Get Y Set
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

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
