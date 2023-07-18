package sqlStuff;

import jakarta.persistence.PersistenceException;
import objectClasses.Occupancy;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;

import java.util.ArrayList;
import java.util.List;

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


}
