package es.iesnervion.yeray.pocketcharacters.EntitiesDDBB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class ClsObjectType {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int _id;
    @ColumnInfo(name = "name")
    private String _name;

    //Constructores
    public ClsObjectType(){
    }

    @Ignore
    public ClsObjectType(int id, String name){
        _id = id;
        _name = name;
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
}
