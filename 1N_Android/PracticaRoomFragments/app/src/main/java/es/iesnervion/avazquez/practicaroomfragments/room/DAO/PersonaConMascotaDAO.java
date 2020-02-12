package es.iesnervion.avazquez.practicaroomfragments.room.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.iesnervion.avazquez.practicaroomfragments.room.entities.PersonaConMascota;

@Dao
public interface PersonaConMascotaDAO {
    @Insert
    public void insertarPersonaConMascota(PersonaConMascota... personaConMascotas);

    @Update
    public void updatePersonaConMascota(PersonaConMascota... personaConMascotas);

    @Delete
    public void deletePersonaConMascota(PersonaConMascota... personaConMascotas);

    @Query("SELECT * FROM PersonaConMascota WHERE idPersona = :idPersona")
    public List<PersonaConMascota> getPersonaConMascotaPorIDPersona(int idPersona);

    @Query("SELECT * FROM PersonaConMascota WHERE idMascota = :idMascota")
    public List<PersonaConMascota> getPersonaConMascotaPorIDMascota(int idMascota);

    @Query("SELECT * FROM PersonaConMascota WHERE idMascota = :idMascota AND idPersona = :idPersona")
    public PersonaConMascota getPersonaConMascota(int idMascota, int idPersona);


}
