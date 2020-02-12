package es.iesnervion.avazquez.practicaroomfragments.room.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(indices = {@Index("idPersona"),@Index("idMascota")}
        ,primaryKeys = {"idPersona", "idMascota"},
        foreignKeys = {
                @ForeignKey(entity = Persona.class,
                        parentColumns = "id",
                        childColumns = "idPersona"),
                @ForeignKey(entity = Mascota.class,
                        parentColumns = "id",
                        childColumns = "idMascota"),

        }
)

public class PersonaConMascota {

    private int idPersona;
    private int idMascota;

    public PersonaConMascota(int idPersona, int idMascota) {
        this.idPersona = idPersona;
        this.idMascota = idMascota;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public int getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(int idMascota) {
        this.idMascota = idMascota;
    }
}
