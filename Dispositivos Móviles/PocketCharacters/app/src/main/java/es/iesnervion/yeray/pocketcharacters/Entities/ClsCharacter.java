package es.iesnervion.yeray.pocketcharacters.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;

@Entity(foreignKeys = @ForeignKey(entity = ClsGameMode.class, parentColumns = "name", childColumns = "gameMode"))
public class ClsCharacter {
    @PrimaryKey
    @ColumnInfo(name = "id")
    private int _id;
    @ColumnInfo(name = "gameMode")
    private String _gameMode;//D&D, Pathfinder, mascarada...
    @ColumnInfo(name = "characterName")
    private String _characterName;
    @ColumnInfo(name = "chapterName")
    private String _chapterName;//Sesión de juego
    @ColumnInfo(name = "story")
    private String _story;
    @Ignore
    private ArrayList<ClsStat> _stats;
    @ColumnInfo(name = "creationDate")
    private Date _creationDate;
    @Ignore
    private ArrayList<ClsObjectAndQuantity> _inventory;

    //Constructores
    public ClsCharacter(){
        _id = 0;
        _gameMode = "DEFAULT";
        _characterName = "DEFAULT";
        _chapterName = "DEFAULT";
        _story = "DEFAULT";
        _stats = new ArrayList<ClsStat>();
        _creationDate = null;
        _inventory = new ArrayList<ClsObjectAndQuantity>();
    }
    @Ignore
    public ClsCharacter(int id, String gameMode, String characterName, String chapterName, String story, ArrayList<ClsStat> stats, Date creationDate, ArrayList<ClsObjectAndQuantity> inventory){
        _id = id;
        _gameMode = gameMode;
        _characterName = characterName;
        _chapterName = chapterName;
        _story = story;
        _stats = stats;
        _creationDate = creationDate;
        _inventory = inventory;
    }

    //Get Y Set
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_gameMode() {
        return _gameMode;
    }

    public void set_gameMode(String _gameMode) {
        this._gameMode = _gameMode;
    }

    public String get_characterName() {
        return _characterName;
    }

    public void set_characterName(String _characterName) {
        this._characterName = _characterName;
    }

    public String get_chapterName() {
        return _chapterName;
    }

    public void set_chapterName(String _chapterName) {
        this._chapterName = _chapterName;
    }

    public String get_story() {
        return _story;
    }

    public void set_story(String _story) {
        this._story = _story;
    }

    public ArrayList<ClsStat> get_stats() {
        return _stats;
    }

    public void set_stats(ArrayList<ClsStat> _stats) {
        this._stats = _stats;
    }

    public Date get_creationDate() {
        return _creationDate;
    }

    public void set_creationDate(Date _creationDate) {
        this._creationDate = _creationDate;
    }

    public ArrayList<ClsObjectAndQuantity> get_inventory() {
        return _inventory;
    }

    public void set_inventory(ArrayList<ClsObjectAndQuantity> _inventory) {
        this._inventory = _inventory;
    }
}
