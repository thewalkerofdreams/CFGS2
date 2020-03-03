import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class PersistenciaSeguro {

    /**
     * Interfaz
     * Nombre: getSeguro
     * Comentario: Este método nos permite obtener un seguro de la base de datos.
     * Cabecera: public Seguro getSeguro(Session sss, byte idSeguro)
     * @param sesion
     * @param idSeguro
     * @return Seguro seguro
     */
    public Seguro getSeguro(Session sesion, int idSeguro)
    {
        Query query = sesion.createQuery("from Seguro where idSeguro= :idSeguro");
        query.setInteger("idSeguro", idSeguro);
        Seguro seguro = (Seguro) query.uniqueResult();
        return seguro;
    }

    /**
     * Interfaz
     * Nombre: insertSeguro
     * Comentario: Este método nos permite insertar un seguro en la base de datos.
     * Cabecera: public void insertSeguro(Session session, Seguro seguro)
     * @param session
     * @param seguro
     */
    public void insertSeguro(Session session, Seguro seguro)
    {
        Transaction transaction = session.beginTransaction();
        session.save(seguro);
        transaction.commit();
    }

    /**
     * Interfaz
     * Nombre: deleteSeguro
     * Comentario: Este método nos permite eliminar un seguro de la base de datos.
     * Cabecera: public void deleteSeguro(Session session, int idSeguro)
     * @param session
     * @param idSeguro
     */
    public void deleteSeguro(Session session, int idSeguro)
    {
        Transaction transaction = session.beginTransaction();

        Seguro seguro = session.get(Seguro.class, idSeguro);
        session.delete(seguro);

        transaction.commit();
    }

    /**
     * Interfaz
     * Nombre: updateSeguro
     * Comentario: Este método nos permite actualizar un seguro de la base de datos.
     * Cabecera: public void updateSeguro(Session session, Seguro seguro)
     * @param session
     * @param seguro
     */
    public void updateSeguro(Session session, Seguro seguro)
    {
        Transaction transaction = session.beginTransaction();

        session.update(seguro);

        transaction.commit();
    }
}
