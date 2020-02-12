package es.iesnervion.yeray.fragments_jugadores.POJO;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.List;

import es.iesnervion.yeray.fragments_jugadores.DDBB_Entities.ClsPlayer;
import es.iesnervion.yeray.fragments_jugadores.DDBB_Entities.ClsPosition;

public class ClsPlayerWithPositions {
    @Embedded
    public ClsPlayer player;

    @Relation(parentColumn = "id", entityColumn = "id", entity = ClsPosition.class)
    public List<ClsPosition> positions; // or use simply 'List pets;'

    //Constructores

    public ClsPlayerWithPositions(){
        this.player = new ClsPlayer();
        this.positions = new ArrayList<ClsPosition>();
    }

    @Ignore
    public ClsPlayerWithPositions(ClsPlayer player, List<ClsPosition> positions){
        this.player = player;
        this.positions = positions;
    }

    //Get y Set

    public ClsPlayer getPlayer() {
        return player;
    }

    public void setPlayer(ClsPlayer player) {
        this.player = player;
    }

    public List<ClsPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<ClsPosition> positions) {
        this.positions = positions;
    }

    //Funciones Extra
    public String getStringPositions(){
        String posiciones = "";

        for(int i = 0; i < positions.size(); i++){
            posiciones += positions.get(i).get_nombrePosicion() + " ";
        }

        return posiciones;
    }
}
