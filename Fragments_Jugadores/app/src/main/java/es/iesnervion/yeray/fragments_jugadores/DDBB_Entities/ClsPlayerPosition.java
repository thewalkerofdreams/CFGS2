package es.iesnervion.yeray.fragments_jugadores.DDBB_Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

@Entity (indices = {@Index("idPlayer"), @Index("idPosition")}, primaryKeys = {"idPlayer", "idPosition"}, foreignKeys = {@ForeignKey(entity = ClsPlayer.class, parentColumns = "id", childColumns = "idPlayer"),
        @ForeignKey(entity = ClsPosition.class, parentColumns = "id", childColumns = "idPosition")})
public class ClsPlayerPosition {
    @ColumnInfo (name = "idPlayer")
    private int _idPlayer;
    @ColumnInfo (name = "idPosition")
    private int _idPosition;

    //Constructores
    public ClsPlayerPosition(){

    }

    @Ignore
    public ClsPlayerPosition(int idPlayer, int idPosition){
        _idPlayer = idPlayer;
        _idPosition = idPosition;
    }

    //Get y Set

    public int get_idPlayer() {
        return _idPlayer;
    }

    public void set_idPlayer(int _idPlayer) {
        this._idPlayer = _idPlayer;
    }

    public int get_idPosition() {
        return _idPosition;
    }

    public void set_idPosition(int _idPosition) {
        this._idPosition = _idPosition;
    }
}
