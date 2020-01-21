package es.iesnervion.yeray.pocketcharacters.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (foreignKeys = {@ForeignKey(entity = ClsGameMode.class, parentColumns = "name", childColumns = "gameMode"),
@ForeignKey(entity = ClsCharacter.class, parentColumns = "id", childColumns = "idCharacter")})
public class ClsObjectAndQuantity {
    @PrimaryKey
    @ColumnInfo(name = "id")
    private int _id;
    @Embedded
    private ClsObject _object;
    @ColumnInfo(name = "quantity")
    private int _quantity;
    @ColumnInfo(name = "characterId")
    private int _characterId;

    //Constructores
    public ClsObjectAndQuantity(){
        _id = 0;
        _object = new ClsObject();
        _quantity = 0;
        _characterId = 0;
    }
    @Ignore
    public ClsObjectAndQuantity(int id, ClsObject object, int quantity, int characterId){
        _id = id;
        _object = object;
        _quantity = quantity;
        _characterId = characterId;
    }

    //Get Y Set
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    //Delegate patron ClsObject class
    public String getObjectType(){
        return _object.get_type();
    }

    public void setObjectType(String type){
        this._object.set_type(type);
    }

    public String getObjectName() {
        return _object.get_name();
    }

    public void setObjectName(String name){
        this._object.set_name(name);
    }

    public String getObjectDescription(){
        return _object.get_description();
    }

    public void setObjectDescription(String description){
        this._object.set_description(description);
    }

    public String getObjectGameMode(){
        return _object.get_gameMode();
    }

    public void setObjectGameMode(String gameMode){
        this._object.set_gameMode(gameMode);
    }

    //---

    public int get_quantity() {
        return _quantity;
    }

    public void set_quantity(int _quantity) {
        this._quantity = _quantity;
    }

    public int get_characterId() {
        return _characterId;
    }

    public void set_characterId(int _characterId) {
        this._characterId = _characterId;
    }
}
