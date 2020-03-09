import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class PersistenciaAsistenciaMedica {

    /**
     * Interfaz
     * Nombre: getAsistenciaMedica
     * Comentario: Este método nos permite obtener una asistencia médica de la base de datos.
     * Cabecera: public Seguro getAsistenciaMedica(Session sesion, int idAsistenciaMedica)
     * @param sesion
     * @param idAsistenciaMedica
     * @return AsistenciaMedica asistenciaMedica
     */
    public AsistenciaMedica getAsistenciaMedica(Session sesion, int idAsistenciaMedica)
    {
        Query query = sesion.createQuery("from AsistenciaMedica where idAsistenciaMedica= :idAsistenciaMedica");
        query.setInteger("idAsistenciaMedica", idAsistenciaMedica);
        AsistenciaMedica asistenciaMedica = (AsistenciaMedica) query.uniqueResult();
        return asistenciaMedica;
    }

    /**
     * Interfaz
     * Nombre: insertAsistenciaMedica
     * Comentario: Este método nos permite insertar una asistencia médica en la base de datos.
     * Cabecera: public void insertAsistenciaMedica(Session session, AsistenciaMedica asistenciaMedica)
     * @param session
     * @param asistenciaMedica
     */
    public void insertAsistenciaMedica(Session session, AsistenciaMedica asistenciaMedica)
    {
        Transaction transaction = session.beginTransaction();
        session.save(asistenciaMedica);
        transaction.commit();
    }

    /**
     * Interfaz
     * Nombre: deleteAsistenciaMedica
     * Comentario: Este método nos permite eliminar un seguro de la base de datos.
     * Cabecera: public void deleteAsistenciaMedica(Session session, int idAsistenciaMedica)
     * @param session
     * @param idAsistenciaMedica
     */
    public void deleteAsistenciaMedica(Session session, int idAsistenciaMedica)
    {
        Transaction transaction = session.beginTransaction();

        AsistenciaMedica asistenciaMedica = session.get(AsistenciaMedica.class, idAsistenciaMedica);
        session.delete(asistenciaMedica);

        transaction.commit();
    }

    /**
     * Interfaz
     * Nombre: updateAsistenciaMedica
     * Comentario: Este método nos permite actualizar un seguro de la base de datos.
     * Cabecera: public void updateAsistenciaMedica(Session session, AsistenciaMedica asistenciaMedica)
     * @param session
     * @param asistenciaMedica
     */
    public void updateAsistenciaMedica(Session session, AsistenciaMedica asistenciaMedica)
    {
        Transaction transaction = session.beginTransaction();

        session.update(asistenciaMedica);

        transaction.commit();
    }
}
