package es.iesnervion.yeray.pocketcharacters.EntitiesModels;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import java.io.Serializable;

public class ClsStatModel implements Serializable {
    @ColumnInfo(name = "name")
    private String _name;
    @ColumnInfo(name = "value")
    private String _value;

    //Constructores
    public ClsStatModel(){
    }

    @Ignore
    public ClsStatModel(String name, String value){
        _name = name;
        _value = value;
    }

    //Get Y Set
    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_value() {
        return _value;
    }

    public void set_value(String _value) {
        this._value = _value;
    }
}
