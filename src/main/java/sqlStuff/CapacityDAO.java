package sqlStuff;

import jakarta.persistence.PersistenceException;
import objectClasses.Occupancy;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static sqlStuff.DBConnection.getConnection;

public class CapacityDAO {

    public static Object [][] getCapacityViewFromDB(){

        List<Object[]> viewData = new ArrayList<>();

        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.openSession();

            // Update the SQL query to select data from the view
            String queryString = "SELECT kategorie, element_count, total_roomNumber, total_bedNumber, avg_roomNumber, avg_bedNumber FROM dbo.hotel_summary_view";

            NativeQuery<?> query = session.createNativeQuery(queryString);

            // Execute the query and get the result list
            List<?> resultList = query.getResultList();

            // Process the result list and add it to the viewData list
            for (Object result : resultList) {
                Object[] row = (Object[]) result;
                viewData.add(row);
            }

            session.close();
        } catch (PersistenceException ex) {
            ex.printStackTrace();
        }

        // Convert the list to a 2D array
        Object[][] viewArray = new Object[viewData.size()][];
        for (int i = 0; i < viewData.size(); i++) {
            viewArray[i] = viewData.get(i);
        }

        return viewArray;

    }
}


