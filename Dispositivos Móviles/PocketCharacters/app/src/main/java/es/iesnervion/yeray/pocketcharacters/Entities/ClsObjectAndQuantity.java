package es.iesnervion.yeray.pocketcharacters.Entities;

public class ClsObjectAndQuantity {
    private ClsObject _object;
    private int _quantity;
    private int _idCharacter;

    //Constructores
    public ClsObjectAndQuantity(){
        _object = new ClsObject();
        _quantity = 0;
        _idCharacter = 0;
    }

    public ClsObjectAndQuantity(ClsObject object, int quantity, int idCharacter){
        _object = object;
        _quantity = quantity;
        _idCharacter = idCharacter;
    }

    //Get Y Set
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

    public int get_idCharacter() {
        return _idCharacter;
    }

    public void set_idCharacter(int _idCharacter) {
        this._idCharacter = _idCharacter;
    }
}
