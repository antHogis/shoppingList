package com.github.anthogis.shoppinglist;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.spi.ServiceException;

import java.util.List;

/**
 * Interface to the Hibernate API.
 *
 * This class is an interface between ShoppingListApp and the Hibernate API. It enables storing ShoppingListItems to a
 * configured database. Depends on a configuration file, which is contains the properties on how database connections are established.
 *
 * @author antHogis
 * @version 1.3
 * @since 1.3
 */
public class HibernateInterface {

    /**
     * A factory for creating sessions for hibernate database transactions.
     */
    private SessionFactory sessionFactory;

    /**
     * Constructs an interface to the Hibernate API.
     *
     * @throws ServiceException if any step in the configuration of sessionFactory fails.
     */
    public HibernateInterface() throws ServiceException {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();
            configuration.addAnnotatedClass(ShoppingListItem.class);

            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            throw new ServiceException("Unable to create requested service");
        }
    }

    /**
     * Inserts values of a list to a table in the database. The table is specified in the configuration of sessionFactory.
     *
     * @param listItems the list of value to insert.
     */
    public void addValues(List<ShoppingListItem> listItems) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        for(ShoppingListItem item : listItems) {
            session.persist(item);
        }

        transaction.commit();
        session.close();
    }

    /**
     * Closes sessionFactory (if it has been instantiated by the constructor).
     */
    public void close() {
        if (sessionFactory != null) sessionFactory.close();
    }
}
