package es.iesnervion.yeray.pocketcharacters.EntitiesDDBB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

@Entity (primaryKeys = {"idCharacter", "stat"}, foreignKeys = {@ForeignKey(entity = ClsStat.class, parentColumns = "name", childColumns = "stat"),
        @ForeignKey(entity = ClsCharacter.class, parentColumns = "id", childColumns = "idCharacter")})
public class ClsCharacterAndStat {
    @ColumnInfo(name = "idCharacter")
    private int _idCharacter;
    @ColumnInfo(name = "stat")
    private String _stat;
    @ColumnInfo(name = "value")
    private String _value;

    //Constructores
    public ClsCharacterAndStat(){
    }

    @Ignore
    public ClsCharacterAndStat(int idCharacter, String stat, String value){
        this._idCharacter = idCharacter;
        this._stat = stat;
        this._value = value;
    }

    //Get Y Set
    public int get_idCharacter() {
        return _idCharacter;
    }

    public void set_idCharacter(int _idCharacter) {
        this._idCharacter = _idCharacter;
    }

    public String get_stat() {
        return _stat;
    }

    public void set_stat(String _stat) {
        this._stat = _stat;
    }

    public String get_value() {
        return _value;
    }

    public void set_value(String _value) {
        this._value = _value;
    }
}
