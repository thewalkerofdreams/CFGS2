package es.iesnervion.yeray.pocketcharacters.EntitiesModels;

import java.io.Serializable;

public class ClsStatModel implements Serializable {
    private String _name;
    private String _value;

    //Constructores
    public ClsStatModel(){
        _name = "DEFAULT";
        _value = "DEFAULT";
    }

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
