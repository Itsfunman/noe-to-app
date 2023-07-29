package objectClasses;

import jakarta.persistence.*;
import lombok.*;
import javax.swing.*;
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

    public void setRoomNumber(int roomNumber) {
        try {
            if (roomNumber == 0) {
                throw new IllegalArgumentException("Room number cannot be 0.");
            }
            if (usedRooms > roomNumber) {
                throw new IllegalArgumentException("Used rooms cannot be greater than total room number.");
            }
            this.roomNumber = roomNumber;
        } catch (IllegalArgumentException e) {
            showErrorDialog("Invalid input for room number: " + e.getMessage());
        }
    }
    public void setUsedRooms(int usedRooms) {
        try {
            if (usedRooms > roomNumber) {
                throw new IllegalArgumentException("Used rooms cannot be greater than total room number.");
            }
            this.usedRooms = usedRooms;
        } catch (IllegalArgumentException e) {
            showErrorDialog("Invalid input for used rooms: " + e.getMessage());
        }
    }
    public void setBedNumber(int bedNumber) {
        try {
            if (bedNumber == 0) {
                throw new IllegalArgumentException("Bed number cannot be 0.");
            }
            if (usedBeds > bedNumber) {
                throw new IllegalArgumentException("Used beds cannot be greater than total bed number.");
            }
            this.bedNumber = bedNumber;
        } catch (IllegalArgumentException e) {
            showErrorDialog("Invalid input for bed number: " + e.getMessage());
        }
    }
    public void setUsedBeds(int usedBeds) {
        try {
            if (usedBeds > bedNumber) {
                throw new IllegalArgumentException("Used beds cannot be greater than total bed number.");
            }
            this.usedBeds = usedBeds;
        } catch (IllegalArgumentException e) {
            showErrorDialog("Invalid input for used beds: " + e.getMessage());
        }
    }



    public Object[] toObjectArray() {
        Object[] dataArray = new Object[4];
        dataArray[0] = year;
        dataArray[1] = month;
        dataArray[2] = usedRooms;
        dataArray[3] = usedBeds;
        return dataArray;
    }

    public Object[] toSingleObjectOverviewArray() {
        Object[] dataArray = new Object[6];
        dataArray[0] = year;
        dataArray[1] = month;
        dataArray[2] = roomNumber;
        dataArray[3] = usedRooms;
        dataArray[4] = bedNumber;
        dataArray[5] = usedBeds;
        return dataArray;
    }

    public Object[] toMultiObjectOverviewArray(int hotelCount) {
        Object[] dataArray = new Object[7];
        dataArray[0] = year;
        dataArray[1] = month;
        dataArray[2] = hotelCount;
        dataArray[3] = roomNumber;
        dataArray[4] = usedRooms;
        dataArray[5] = bedNumber;
        dataArray[6] = usedBeds;
        return dataArray;
    }
    private void showErrorDialog(String message) {
          JOptionPane.showMessageDialog(null, message, "Invalid Input", JOptionPane.ERROR_MESSAGE);
}
}
