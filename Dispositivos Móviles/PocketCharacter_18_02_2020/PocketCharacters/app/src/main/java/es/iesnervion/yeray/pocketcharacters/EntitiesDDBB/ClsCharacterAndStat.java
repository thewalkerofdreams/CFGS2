package es.iesnervion.yeray.pocketcharacters.EntitiesDDBB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

@Entity (indices = {@Index("idCharacter"), @Index("idStat")}, primaryKeys = {"idCharacter", "idStat"}, foreignKeys = {@ForeignKey(entity = ClsStat.class, parentColumns = "id", childColumns = "idStat"),
        @ForeignKey(entity = ClsCharacter.class, parentColumns = "id", childColumns = "idCharacter")})
public class ClsCharacterAndStat {
    @ColumnInfo(name = "idCharacter")
    private int _idCharacter;
    @ColumnInfo(name = "idStat")
    private int _idStat;
    @ColumnInfo(name = "value")
    private String _value;

    //Constructores
    public ClsCharacterAndStat(){
    }

    @Ignore
    public ClsCharacterAndStat(int idCharacter, int idStat, String value){
        this._idCharacter = idCharacter;
        this._idStat = idStat;
        this._value = value;
    }

    //Get Y Set
    public int get_idCharacter() {
        return _idCharacter;
    }

    public void set_idCharacter(int _idCharacter) {
        this._idCharacter = _idCharacter;
    }

    public int get_idStat() {
        return _idStat;
    }

    public void set_idStat(int _idStat) {
        this._idStat = _idStat;
    }

    public String get_value() {
        return _value;
    }

    public void set_value(String _value) {
        this._value = _value;
    }
}
