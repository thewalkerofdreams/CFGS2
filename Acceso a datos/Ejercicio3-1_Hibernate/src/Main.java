import org.hibernate.HibernateException;
import org.hibernate.Metamodel;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.metamodel.EntityType;

import java.util.Date;
import java.util.Map;

public class Main {
    private static final SessionFactory ourSessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            ourSessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    public static void main(String[] args){
        Session session = null;
        PersistenciaSeguro persistenciaSeguro = new PersistenciaSeguro();

        session = ourSessionFactory.openSession();

        persistenciaSeguro.insertSeguro(session, new Seguro("45652367G", "Iván", "Moreno", "Romero", 23, 33, new Date(1996, 6, 14)));
        //persistenciaSeguro.deleteSeguro(session,1);
        //persistenciaSeguro.updateSeguro(session, new Seguro(2, "87658799K", "IvánNo", "MorenoNo", "Romero", 23, 33, new Date(1996, 6, 14)));
        Seguro seguro = persistenciaSeguro.getSeguro(session, 1);

        //Pruebas Asistencias Médicas
        PersistenciaAsistenciaMedica persistenciaAsistenciaMedica = new PersistenciaAsistenciaMedica();

        persistenciaAsistenciaMedica.insertAsistenciaMedica(session, new AsistenciaMedica(seguro, "Dolor de garganta", "Sevilla"));
        System.out.println(persistenciaAsistenciaMedica.getAsistenciaMedica(session, 1));
    }
}