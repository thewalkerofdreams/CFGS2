package es.iesnervion.avazquez.practicaroomfragments.room.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.iesnervion.avazquez.practicaroomfragments.room.entities.CuentaRedSocial;

@Dao
public interface CuentaRedSocialDAO {
    @Insert
    public void insertarCuentaRedSocial(CuentaRedSocial... redesSociales);

    @Update
    public void updateCuentaRedSocial(CuentaRedSocial... redesSociales);

    @Delete
    public void deleteCuentaRedSocial(CuentaRedSocial... redesSociales);

    @Query("SELECT * FROM CuentaRedSocial WHERE idPersona = :idPersona")
    public List<CuentaRedSocial> getRedesSocialesPorIDPersona(int idPersona);

    @Query("SELECT * FROM CuentaRedSocial WHERE id = :id")
    public CuentaRedSocial getRedSocial(int id);

}
