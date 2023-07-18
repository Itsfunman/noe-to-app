package objectClasses;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name="occupancy")
public class Occupancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name="hotelid")
    private int hotelid;

    @Column(name="year")
    private int year;

    @Column(name="month")
    private int month;

    @Column
    private int roomNumber;

    @Column
    private int usedRooms;

    @Column
    private int bedNumber;

    @Column
    private int usedBeds;

    public Occupancy(){
    }

    public Object[] toObjectArray() {
        Object[] dataArray = new Object[4];
        dataArray[0] = year;
        dataArray[1] = month;
        dataArray[2] = usedRooms;
        dataArray[3] = usedBeds;
        return dataArray;
    }
}
