package sqlStuff;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import utilityClasses.Hotel;

import java.util.ArrayList;
import java.util.List;


public class HotelDAO {

    // Fetch data from DB using Hibernate
    public static List<Hotel> fetchDataFromDB() {
        List<Hotel> hotels = new ArrayList<>();

        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.openSession();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Hotel> query = cb.createQuery(Hotel.class);
            Root<Hotel> root = query.from(Hotel.class);
            query.select(root);
            TypedQuery<Hotel> typedQuery = session.createQuery(query);
            hotels = typedQuery.getResultList();

            session.close();
        } catch (PersistenceException ex) {
            ex.printStackTrace();
        }

        return hotels;
    }


    // Add a hotel to the DB using Hibernate
    public static void addHotelToDB(Hotel hotel) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.persist(hotel);
            tx.commit();
            session.close();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
    }

    public static void updateHotelInDB(Hotel hotel) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.merge(hotel);
            tx.commit();
            session.close();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
    }

}

