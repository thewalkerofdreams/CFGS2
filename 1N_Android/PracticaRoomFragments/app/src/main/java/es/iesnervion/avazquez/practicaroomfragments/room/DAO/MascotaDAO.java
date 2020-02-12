package es.iesnervion.avazquez.practicaroomfragments.room.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.iesnervion.avazquez.practicaroomfragments.room.entities.Mascota;

@Dao
public interface MascotaDAO {
    @Insert
    public long insertarMascota(Mascota mascotas);

    @Update
    public void updateMascotas(Mascota... mascotas);

    @Delete
    public void deleteMascotas(Mascota... mascotas);

    @Query("SELECT * FROM Mascota WHERE id = :idMascota")
    public Mascota getMascotaPorID(int idMascota);

    @Query("SELECT * FROM Mascota")
    public List<Mascota> getMascotas();
}
