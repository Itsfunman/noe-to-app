package sqlStuff;

import jakarta.persistence.PersistenceException;
import objectClasses.Occupancy;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;

import java.util.ArrayList;
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

}
