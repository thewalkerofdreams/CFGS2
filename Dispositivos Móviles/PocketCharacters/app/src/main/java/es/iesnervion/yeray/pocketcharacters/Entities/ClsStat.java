package es.iesnervion.yeray.pocketcharacters.Entities;

public class ClsStat {
    private String _name;
    private String _value;//no sabemos si el valor de la estadística será numérico
    private int _characterId;
    private String _gameMode;

    //Constructores
    public ClsStat(){
        _name = "DEFAULT";
        _value = "0";
        _characterId = 0;
        _gameMode = "DEFAULT";
    }

    public ClsStat(String name, String value, int characterId, String gameMode){
        _name = name;
        _value = value;
        _characterId = characterId;
        _gameMode = gameMode;
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

    public int get_characterId() {
        return _characterId;
    }

    public void set_characterId(int _characterId) {
        this._characterId = _characterId;
    }

    public String get_gameMode() {
        return _gameMode;
    }

    public void set_gameMode(String _gameMode) {
        this._gameMode = _gameMode;
    }
}
