package com.github.anthogis.shoppinglist;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.spi.ServiceException;

import java.util.List;

public class HibernateInterface {
    private Configuration configuration;
    private SessionFactory sessionFactory;

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

    public void addValues(List<ShoppingListItem> listItems) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        for(ShoppingListItem item : listItems) {
            session.persist(item);
        }

        transaction.commit();
        session.close();
    }

    public void close() {
        if (sessionFactory != null) sessionFactory.close();
    }
}
