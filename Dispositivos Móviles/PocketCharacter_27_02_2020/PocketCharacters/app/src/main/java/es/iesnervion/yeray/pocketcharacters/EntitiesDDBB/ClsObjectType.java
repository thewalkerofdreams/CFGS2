package es.iesnervion.yeray.pocketcharacters.EntitiesDDBB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class ClsObjectType {
    @PrimaryKey
    @ColumnInfo(name = "name")
    @NonNull
    private String _name;

    //Constructores
    public ClsObjectType(){
    }

    @Ignore
    public ClsObjectType(String name){
        _name = name;
    }

    //Get Y Set
    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }
}
