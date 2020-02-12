package es.iesnervion.avazquez.practicaroomfragments.room.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

import static androidx.room.ForeignKey.CASCADE;

@Entity(primaryKeys = {"idPersona", "id"},
        foreignKeys = @ForeignKey(entity = Persona.class,
                        parentColumns = "id",
                        childColumns = "idPersona",
                        onDelete = CASCADE,
                        onUpdate = CASCADE
        ))
public class CuentaRedSocial {


    private int idPersona; //seria el ID de la persona, ya que cuentaRedSocial es una entidad debil en existencia
    private int id;
    private String nombreRedSocial;

    @Ignore
    public CuentaRedSocial() {
        this.idPersona = 0;
        this.nombreRedSocial = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CuentaRedSocial(int idPersona, String nombreRedSocial) {
        this.idPersona = idPersona;
        this.nombreRedSocial = nombreRedSocial;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getNombreRedSocial() {
        return nombreRedSocial;
    }

    public void setNombreRedSocial(String nombreRedSocial) {
        this.nombreRedSocial = nombreRedSocial;
    }
}
