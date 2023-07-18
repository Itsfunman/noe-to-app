package sqlStuff;

import jakarta.persistence.PersistenceException;
import objectClasses.Hotel;
import objectClasses.Occupancy;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class OccupancyDAO {

    public static List<Occupancy> fetchOccupancyBasedOnField(int hotelId) {
        List<Occupancy> occupancies = new ArrayList<>();

        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.openSession();

            String queryString = "SELECT * FROM dbo.occupancy WHERE hotelid = " + String.valueOf(hotelId) +" ORDER BY year, month";
            NativeQuery<Occupancy> query = session.createNativeQuery(queryString, Occupancy.class);

            occupancies = query.getResultList();

            session.close();
        } catch (PersistenceException ex) {
            ex.printStackTrace();
        }

        return occupancies;
    }

    public static List<Occupancy> fetchSingleOccupancyOverview(int startYear, int endYear, int startMonth, int endMonth, int hotelid){
        List <Occupancy> occupancies = new ArrayList<>();

        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.openSession();

            String queryString = "SELECT * FROM dbo.occupancy WHERE hotelid = " + String.valueOf(hotelid) +
                    " AND (year > " + String.valueOf(startYear) + " OR (year = " + String.valueOf(startYear) + " AND month >= " + String.valueOf(startMonth) + "))" +
                    " AND (year < " + String.valueOf(endYear) + " OR (year = " + String.valueOf(endYear) + " AND month <= " + String.valueOf(endMonth) + "))";

            NativeQuery<Occupancy> query = session.createNativeQuery(queryString, Occupancy.class);

            occupancies = query.getResultList();

            session.close();
        } catch (PersistenceException ex) {
            ex.printStackTrace();
        }

        return occupancies;
    }


    public static List<Occupancy> fetchMultiOccupancyOverview(int startCategory, int endCategory, int startYear, int endYear,int startMonth, int endMonth) {
        List<Occupancy> occupancies = new ArrayList<>();

        try {
            List<Integer> hotelids = HotelDAO.getHotelIDs(String.valueOf(startCategory), String.valueOf(endCategory));

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.openSession();

            String hotelidString = hotelids.stream().map(String::valueOf).collect(Collectors.joining(","));

            String queryString = "SELECT year, month, SUM(roomNumber), SUM(usedRooms), SUM(bedNumber), SUM(usedBeds) " +
                    "FROM dbo.occupancy " +
                    "WHERE hotelid IN (" + hotelidString + ") " +
                    "AND ((year > " + startYear + " OR (year = " + startYear + " AND month >= " + startMonth + ")) " +
                    "AND (year < " + endYear + " OR (year = " + endYear + " AND month <= " + endMonth + "))) " +
                    "GROUP BY year, month " +
                    "ORDER BY year, month";


            NativeQuery<Object[]> query = session.createNativeQuery(queryString);
            List<Object[]> results = query.getResultList();

            for (Object[] result : results) {
                int year = (int) result[0];
                int month = (int) result[1];
                int roomNumber = (int) result[2];
                int usedRooms = (int) result[3];
                int bedNumber = (int) result[4];
                int usedBeds = (int) result[5];

                // Create Occupancy object and add it to the list
                Occupancy occupancy = new Occupancy();
                occupancy.setYear(year);
                occupancy.setMonth(month);
                occupancy.setRoomNumber(roomNumber);
                occupancy.setUsedRooms(usedRooms);
                occupancy.setBedNumber(bedNumber);
                occupancy.setUsedBeds(usedBeds);
                occupancies.add(occupancy);
            }

            session.close();
        } catch (PersistenceException ex) {
            ex.printStackTrace();
        }

        return occupancies;
    }

    public static void addHotelToOccupancyTable(Hotel hotel) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            for (int year = 2014; year <= currentYear; year++) {
                for (int month = 1; month <= 12; month++) {
                    Occupancy occupancy = new Occupancy();
                    occupancy.setHotelid(hotel.getHotelID());
                    occupancy.setRoomNumber(hotel.getRoomNumber());
                    occupancy.setUsedRooms(0);
                    occupancy.setBedNumber(hotel.getBedNumber());
                    occupancy.setUsedBeds(0);
                    occupancy.setYear(year);
                    occupancy.setMonth(month);

                    session.save(occupancy);
                }
            }

            transaction.commit();
            session.close();

            System.out.println("Data added to the occupancy table.");
        } catch (HibernateException e) {
            e.printStackTrace();
            System.out.println("Failed to add data to the occupancy table.");
        }
    }


    public static void deleteHotelFromOccupancyTable(String hotelID) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement("DELETE FROM dbo.occupancy WHERE hotelid = ?")) {
            pst.setInt(1, Integer.parseInt(hotelID));
            pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
}
