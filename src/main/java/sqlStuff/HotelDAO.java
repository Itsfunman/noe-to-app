package sqlStuff;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.hibernate.query.NativeQuery;
import objectClasses.Hotel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    public static List<Hotel> fetchHotelsOrderedByField(String field) {
        List<Hotel> hotels = new ArrayList<>();

        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.openSession();

            String queryString = "SELECT * FROM hotel ORDER BY " + field;
            NativeQuery<Hotel> query = session.createNativeQuery(queryString, Hotel.class);
            hotels = query.getResultList();

            session.close();
        } catch (PersistenceException ex) {
            ex.printStackTrace();
        }

        return hotels;
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

    public static int getLastHotelID() {

        int lastHotelID = 0;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Integer> query = builder.createQuery(Integer.class);
            Root<Hotel> root = query.from(Hotel.class);
            query.select(root.get("hotelID"))
                    .orderBy(builder.desc(root.get("hotelID")));

            List<Integer> resultList = session.createQuery(query)
                    .setMaxResults(1)
                    .getResultList();

            if (!resultList.isEmpty()) {
                lastHotelID = resultList.get(0);
            }

            session.close();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }

        return lastHotelID;
    }

    public static void saveToDataBase(Hotel hotel) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.save(hotel);
            tx.commit();
            session.close();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Deletes a hotel from the database.
     */
    public static void deleteHotelFromDB(String hotelID) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement("DELETE FROM hotel WHERE hotelid = ?")) {
            pst.setInt(1, Integer.parseInt(hotelID));
            pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static List<Integer> getHotelIDs(String minCategory, String maxCategory) {
        List<Integer> hotelIDs = new ArrayList<>();

        try {
            List<Hotel> hotels = HotelDAO.fetchDataFromDB();
            for (Hotel hotel : hotels) {
                int catLength = hotel.getCategory().length();
                if (catLength >= minCategory.length() && catLength <= maxCategory.length()) {
                    hotelIDs.add(hotel.getHotelID());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            hotelIDs = new ArrayList<>(); // Empty list in case of an exception
        }

        return hotelIDs;
    }


    public static int[] otherValues(List<Integer> hotelIDs) {
        int[] otherValues = new int[]{0, 0};

        try {
            List<Hotel> hotels = HotelDAO.fetchDataFromDB();
            for (Hotel hotel : hotels) {
                if (hotelIDs.contains(hotel.getHotelID())) {
                    otherValues[0] += hotel.getRoomNumber();
                    otherValues[1] += hotel.getBedNumber();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            otherValues = new int[]{0, 0}; // Zero values in case of an exception
        }

        return otherValues;
    }

}

