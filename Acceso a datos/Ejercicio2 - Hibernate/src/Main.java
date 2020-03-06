import org.hibernate.HibernateException;
import org.hibernate.Metamodel;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.metamodel.EntityType;

import java.sql.Date;
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

    public static void main(final String[] args) throws Exception {
        Session session = null;
        PersistenciaSeguro persistenciaSeguro = new PersistenciaSeguro();

        session = ourSessionFactory.openSession();

        //persistenciaSeguro.insertSeguro(session, new Seguro("45652367G", "José", "Espinal", "Mateu", 23, 0, new Date(1996, 3, 12)));
        //persistenciaSeguro.deleteSeguro(session,1);
        //persistenciaSeguro.updateSeguro(session, new Seguro(2, "44555622J", "JoseNo2", "EspinalNo", "Mateu", 23, 0, new Date(1996, 6, 10)));
        System.out.println(persistenciaSeguro.getSeguro(session, 2));
    }
}