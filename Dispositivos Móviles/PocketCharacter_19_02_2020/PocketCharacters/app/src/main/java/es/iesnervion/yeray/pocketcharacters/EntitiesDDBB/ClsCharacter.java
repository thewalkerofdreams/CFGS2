package es.iesnervion.yeray.pocketcharacters.EntitiesDDBB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(indices = {@Index("gameMode")},foreignKeys = @ForeignKey(entity = ClsGameMode.class, parentColumns = "name", childColumns = "gameMode"))
public class ClsCharacter implements Serializable {
    @PrimaryKey(autoGenerate = true)
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
    //@ColumnInfo(name = "creationDate")
    //private Date _creationDate;

    //Constructores
    public ClsCharacter(){
        _id = 0;
        _gameMode = "DEFAULT";
        _characterName = "DEFAULT";
        _chapterName = "DEFAULT";
        _story = "DEFAULT";
        //_creationDate = null;
    }

    @Ignore
    public ClsCharacter(String gameMode, String characterName, String chapterName, String story){
        _gameMode = gameMode;
        _characterName = characterName;
        _chapterName = chapterName;
        _story = story;
        //_creationDate = creationDate;
    }

    @Ignore
    public ClsCharacter(ClsCharacter character){
        _id = character.get_id();
        _gameMode = character.get_gameMode();
        _characterName = character.get_characterName();
        _chapterName = character.get_chapterName();
        _story = character.get_story();
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

    /*public Date get_creationDate() {
        return _creationDate;
    }

    public void set_creationDate(Date _creationDate) {
        this._creationDate = _creationDate;
    }
*/

}
