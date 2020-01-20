package es.iesnervion.yeray.pocketcharacters.Entities;

import java.util.ArrayList;

public class ClsCharacter {
    private int _id;
    private String _gameMode;//D&D, Pathfinder, mascarada...
    private String _characterName;
    private String _chapterName;//Sesión de juego
    private String _story;
    private ArrayList<ClsStat> _stats;
    private ArrayList<ClsObjectAndQuantity> _inventory;

    //Constructores
    public ClsCharacter(){
        _id = 0;
        _gameMode = "DEFAULT";
        _characterName = "DEFAULT";
        _chapterName = "DEFAULT";
        _story = "DEFAULT";
        _stats = new ArrayList<ClsStat>();
        _inventory = new ArrayList<ClsObjectAndQuantity>();
    }

    public ClsCharacter(int id, String gameMode, String characterName, String chapterName, String story, ArrayList<ClsStat> stats, ArrayList<ClsObjectAndQuantity> inventory){
        _id = id;
        _gameMode = gameMode;
        _characterName = characterName;
        _chapterName = chapterName;
        _story = story;
        _stats = stats;
        _inventory = inventory;
    }

    //Get Y Set
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_gameMode() {
        return _gameMode;
    }

    public void set_gameMode(String _gameMode) {
        this._gameMode = _gameMode;
    }

    public String get_characterName() {
        return _characterName;
    }

    public void set_characterName(String _characterName) {
        this._characterName = _characterName;
    }

    public String get_chapterName() {
        return _chapterName;
    }

    public void set_chapterName(String _chapterName) {
        this._chapterName = _chapterName;
    }

    public String get_story() {
        return _story;
    }

    public void set_story(String _story) {
        this._story = _story;
    }

    public ArrayList<ClsStat> get_stats() {
        return _stats;
    }

    public void set_stats(ArrayList<ClsStat> _stats) {
        this._stats = _stats;
    }

    public ArrayList<ClsObjectAndQuantity> get_inventory() {
        return _inventory;
    }

    public void set_inventory(ArrayList<ClsObjectAndQuantity> _inventory) {
        this._inventory = _inventory;
    }
}
