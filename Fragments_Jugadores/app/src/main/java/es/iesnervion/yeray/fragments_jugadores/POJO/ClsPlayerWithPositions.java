package es.iesnervion.yeray.fragments_jugadores.POJO;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import es.iesnervion.yeray.fragments_jugadores.DDBB_Entities.ClsPlayer;
import es.iesnervion.yeray.fragments_jugadores.DDBB_Entities.ClsPosition;

public class ClsPlayerWithPositions {
    @Embedded
    public ClsPlayer player;

    @Relation(parentColumn = "id", entityColumn = "id", entity = ClsPosition.class)
    public List<ClsPosition> positions; // or use simply 'List pets;'

    //Funciones Extra
    public String getStringPositions(){
        String posiciones = "";

        for(int i = 0; i < positions.size(); i++){
            posiciones += positions.get(i).get_nombrePosicion() + " ";
        }

        return posiciones;
    }
}
