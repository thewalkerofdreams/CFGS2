package es.iesnervion.yeray.pocketcharacters.EntitiesDDBB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity (indices = {@Index("gameMode")}, foreignKeys = {@ForeignKey(entity = ClsGameMode.class, parentColumns = "name", childColumns = "gameMode")})
public class ClsStat {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "id")
    private int _id;
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
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

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
