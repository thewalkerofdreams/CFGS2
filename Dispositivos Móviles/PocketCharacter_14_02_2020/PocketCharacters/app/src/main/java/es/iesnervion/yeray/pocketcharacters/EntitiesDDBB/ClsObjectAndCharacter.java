package es.iesnervion.yeray.pocketcharacters.EntitiesDDBB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

@Entity (indices = {@Index("idCharacter"), @Index("idObject")}, primaryKeys = {"idCharacter", "idObject"}, foreignKeys = {@ForeignKey(entity = ClsObject.class, parentColumns = "id", childColumns = "idObject"),
@ForeignKey(entity = ClsCharacter.class, parentColumns = "id", childColumns = "idCharacter")})
public class ClsObjectAndCharacter {

    @ColumnInfo(name = "idCharacter")
    private int _idCharacter;
    @ColumnInfo(name = "idObject")
    private int _idObject;
    @ColumnInfo(name = "quantity")
    private int _quantity;

    //Constructores
    public ClsObjectAndCharacter() {
    }

    @Ignore
    public ClsObjectAndCharacter(int idCharacter, int idObject, int quantity) {
        _idCharacter = idCharacter;
        _idObject = idObject;
        _quantity = quantity;
    }

    //Get Y Set
    public int get_idCharacter() {
        return _idCharacter;
    }

    public void set_idCharacter(int _id) {
        this._idCharacter = _id;
    }

    public int get_idObject() {
        return _idObject;
    }

    public void set_idObject(int _idObject) {
        this._idObject = _idObject;
    }

    public int get_quantity() {
        return _quantity;
    }

    public void set_quantity(int _quantity) {
        this._quantity = _quantity;
    }
}
