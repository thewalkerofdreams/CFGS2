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

    /*public static void main(final String[] args) throws Exception {
        final Session session = getSession();
        try {
            System.out.println("querying all the managed entities...");
            final Metamodel metamodel = session.getSessionFactory().getMetamodel();
            for (EntityType<?> entityType : metamodel.getEntities()) {
                final String entityName = entityType.getName();
                final Query query = session.createQuery("from " + entityName);
                System.out.println("executing: " + query.getQueryString());
                for (Object o : query.list()) {
                    System.out.println("  " + o);
                }
            }
        } finally {
            session.close();
        }


    }*/
    //private static SessionFactory sessionFactory = null;

    public static void main(String[] args){
        Session session = null;
        PersistenciaSeguro persistenciaSeguro = new PersistenciaSeguro();

        session = ourSessionFactory.openSession();

        //persistenciaSeguro.insertSeguro(session, new Seguro("45652367G", "Iván", "Moreno", "Romero", 23, 33, new Date(1996, 6, 14)));
        //persistenciaSeguro.deleteSeguro(session,1);
        //persistenciaSeguro.updateSeguro(session, new Seguro(2, "87658799K", "IvánNo", "MorenoNo", "Romero", 23, 33, new Date(1996, 6, 14)));
        System.out.println(persistenciaSeguro.getSeguro(session, 2));

    }
}