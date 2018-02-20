package com.ryanwittrup.contactmgr;

import com.ryanwittrup.contactmgr.model.Contact;
import com.ryanwittrup.contactmgr.model.Contact.ContactBuilder;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class Application {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();

        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
        Contact contact = new ContactBuilder("Ryan", "Wittrup")
                                .withEmail("ryan@gmail.com")
                                .withPhone(4405675555L)
                                .build();

        int id = save(contact);

        // display before update
        System.out.printf("%n%nBefore update%n%n");
        fetchAllContacts().stream().forEach(System.out::println);

        // get contact
        Contact c = findContactById(id);
        c.setFirstName("Ravid");
        c.setLastName("Rondy");


        // update contact
        System.out.printf("%n%nUpdating...%n%n");
        update(c);

        // display after update
        System.out.printf("%n%nAfter update%n%n");
        fetchAllContacts().stream().forEach(System.out::println);


        // delete contact
        System.out.printf("%n%nDeleting...%n%n");
        delete(c);

        // display after delete
        System.out.printf("%n%nAfter delete%n%n");
        fetchAllContacts().stream().forEach(System.out::println);
    }


    private static List<Contact> fetchAllContacts() {
        Session session = sessionFactory.openSession();

        // create criteria builder
        CriteriaBuilder builder = session.getCriteriaBuilder();

        // create criteria query
        CriteriaQuery<Contact> criteria = builder.createQuery(Contact.class);

        // specify criteria root
        criteria.from(Contact.class);

        // execute query
        List<Contact> contacts = session.createQuery(criteria).getResultList();

        session.close();

        return contacts;
    }

    private static Contact findContactById(int id) {
        Session session = sessionFactory.openSession();

        // retrieve by id or null if not found
        Contact contact = session.get(Contact.class, id);

        session.close();

        return contact;
    }


    private static void update(Contact contact) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.update(contact);

        session.getTransaction().commit();

        session.close();
    }

    private static void delete(Contact contact) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.delete(contact);

        session.getTransaction().commit();

        session.close();
    }


    private static int save(Contact contact) {
        // open a session
        Session session = sessionFactory.openSession();

        // begin transaction
        session.beginTransaction();

        // use session to save contact
        int id = (int)session.save(contact);

        // commit transaction
        session.getTransaction().commit();

        // close session
        session.close();

        return id;
    }
}
