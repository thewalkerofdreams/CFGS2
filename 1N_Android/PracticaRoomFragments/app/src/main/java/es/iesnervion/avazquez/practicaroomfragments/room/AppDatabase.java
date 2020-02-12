package es.iesnervion.avazquez.practicaroomfragments.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import es.iesnervion.avazquez.practicaroomfragments.room.DAO.CuentaRedSocialDAO;
import es.iesnervion.avazquez.practicaroomfragments.room.DAO.MascotaDAO;
import es.iesnervion.avazquez.practicaroomfragments.room.DAO.PersonaConMascotaDAO;
import es.iesnervion.avazquez.practicaroomfragments.room.DAO.PersonaDAO;
import es.iesnervion.avazquez.practicaroomfragments.room.entities.CuentaRedSocial;
import es.iesnervion.avazquez.practicaroomfragments.room.entities.Mascota;
import es.iesnervion.avazquez.practicaroomfragments.room.entities.Persona;
import es.iesnervion.avazquez.practicaroomfragments.room.entities.PersonaConMascota;

@Database(entities = {Persona.class, Mascota.class, CuentaRedSocial.class, PersonaConMascota.class},
        version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PersonaDAO personaDAO();
    public abstract MascotaDAO mascotaDAO();
    public abstract CuentaRedSocialDAO cuentaRedSocialDAO();
    public abstract PersonaConMascotaDAO personaConMascotaDAO();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context){
        if(INSTANCE == null) {
            synchronized (AppDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "personas_db.db")
                            .fallbackToDestructiveMigration()   //para que borre to cuando cambio algo y no pete por falta de migracion
                            .allowMainThreadQueries()   //solo pa proba
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
