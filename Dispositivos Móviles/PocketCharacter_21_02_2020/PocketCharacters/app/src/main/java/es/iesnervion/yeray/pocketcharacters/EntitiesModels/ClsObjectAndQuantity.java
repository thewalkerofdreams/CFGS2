package es.iesnervion.yeray.pocketcharacters.EntitiesModels;

import androidx.room.Ignore;

import java.io.Serializable;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObject;

public class ClsObjectAndQuantity implements Serializable {
    private ClsObject _object;
    private int _quantity;

    //Constructores
    public ClsObjectAndQuantity(){
        _object = new ClsObject();
        _quantity = 0;
    }

    public ClsObjectAndQuantity(ClsObject object, int quantity){
        _object = object;
        _quantity = quantity;
    }

    //Get Y Set
    public ClsObject get_object() {
        return _object;
    }

    public void set_object(ClsObject _object) {
        this._object = _object;
    }

    public int get_quantity() {
        return _quantity;
    }

    public void set_quantity(int _quantity) {
        this._quantity = _quantity;
    }
}
