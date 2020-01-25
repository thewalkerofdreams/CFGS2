package es.iesnervion.yeray.pocketcharacters.EntitiesModels;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Ignore;

import java.util.ArrayList;

import es.iesnervion.yeray.pocketcharacters.DDBB.AppDataBase;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsCharacter;
import es.iesnervion.yeray.pocketcharacters.EntitiesDDBB.ClsObject;

public class ClsCharacterModel extends ClsCharacter implements Parcelable {
    private ArrayList<ClsStatModel> _stats;
    private ArrayList<ClsObjectAndQuantity> _objects;

    public ClsCharacterModel(ClsCharacter character){
        super(character);
        _stats = new ArrayList<>();
        _objects = new ArrayList<>();
        loadStats();
    }

    //Get Y Set
    public ArrayList<ClsStatModel> get_stats() {
        return _stats;
    }

    public void set_stats(ArrayList<ClsStatModel> _stats) {
        this._stats = _stats;
    }

    public ArrayList<ClsObjectAndQuantity> get_objects() {
        return _objects;
    }

    public void set_objects(ArrayList<ClsObjectAndQuantity> _objects) {
        this._objects = _objects;
    }

    /*
    * Interfaz
    * Nombre: loadStats
    * Comentario: Este método nos permite cargar los stats del personaje.
    * Cabecera: public void loadStats()
    * Postcondiciones: El método carga los stats del personaje en el atributo "-stats".
    * */
    public void loadStats(){
        //_stats = new ArrayList<ClsStatModel>(AppDataBase.getDataBase(_context).characterAndStatDao().getStatsAndValueByCharacter(get_id()));
    }

    //Parcelable
    @Ignore
    protected ClsCharacterModel(Parcel in) {

        if (in.readByte() == 0x01) {
            _stats = new ArrayList<ClsStatModel>();
            in.readList(_stats, ClsStatModel.class.getClassLoader());
        } else {
            _stats = null;
        }

        if (in.readByte() == 0x01) {
            _objects = new ArrayList<ClsObjectAndQuantity>();
            in.readList(_objects, ClsObjectAndQuantity.class.getClassLoader());
        } else {
            _objects = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (_stats == null) {
            dest.writeByte((byte) (0x00));
        }
        else {
            dest.writeByte((byte) (0x01));
            dest.writeList(_stats);
        }

        if (_objects == null) {
            dest.writeByte((byte) (0x00));
        }
        else {
            dest.writeByte((byte) (0x01));
            dest.writeList(_objects);
        }
    }
}
