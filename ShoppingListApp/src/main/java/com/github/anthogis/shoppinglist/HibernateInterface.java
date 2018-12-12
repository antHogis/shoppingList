package com.github.anthogis.shoppinglist;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.spi.ServiceException;

import java.util.List;

/**
 * TODO write javadoc
 */
public class HibernateInterface {

    /**
     * TODO write javadoc
     */
    private Configuration configuration;

    /**
     * TODO write javadoc
     */
    private SessionFactory sessionFactory;

    /**
     * TODO write javadoc
     */
    public HibernateInterface() throws ServiceException {
        try {
            configuration = new Configuration();
            configuration.configure();
            configuration.addAnnotatedClass(ShoppingListItem.class);

            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            throw new ServiceException("Unable to create requested service");
        }
    }

    /**
     * TODO write javadoc
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
     * TODO write javadoc
     */
    public void close() {
        if (sessionFactory != null) sessionFactory.close();
    }
}
