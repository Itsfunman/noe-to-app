package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Hotel {

    public static ArrayList <Hotel> hotels = new ArrayList();

    //hotelID counter:
    private static int COUNTER = 0;
    private int hotelID;

    //Basic information:
    private String hotelName;
    private int stars;
    private int roomNumber;
    private HashMap <Date, Integer> roomOccupancy;
    private int bedNumber;
    private HashMap <Date, Integer> bedOccupancy;

    private HashMap <String, Boolean> extras;

    public Hotel(String hotelName, String stars, String roomNumber, String bedNumber){
        this.hotelID = COUNTER++;
        this.hotelName = hotelName;
        this.stars = Integer.parseInt(stars);
        this.roomNumber = Integer.parseInt(roomNumber);
        this.bedNumber = Integer.parseInt(bedNumber);

        //Add rest with try catch blocks;
    }

    public String toStringSimple() {
        return hotelName + "," + stars + "," + roomNumber + "," + bedNumber;
    }

    public int getHotelID() {
        return hotelID;
    }

    public void setHotelID(int hotelID) {
        this.hotelID = hotelID;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public HashMap<Date, Integer> getRoomOccupancy() {
        return roomOccupancy;
    }

    public void setRoomOccupancy(HashMap<Date, Integer> roomOccupancy) {
        this.roomOccupancy = roomOccupancy;
    }

    public int getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(int bedNumber) {
        this.bedNumber = bedNumber;
    }

    public HashMap<Date, Integer> getBedOccupancy() {
        return bedOccupancy;
    }

    public void setBedOccupancy(HashMap<Date, Integer> bedOccupancy) {
        this.bedOccupancy = bedOccupancy;
    }

    public HashMap<String, Boolean> getExtras() {
        return extras;
    }

    public void setExtras(HashMap<String, Boolean> extras) {
        this.extras = extras;
    }

    public void addToFile(Hotel hotel) {
        String filePath = "data/hotelData.txt";
        try (FileReader fileReader = new FileReader(filePath);
             BufferedReader bufferedReader = new BufferedReader(fileReader);
             FileWriter writer = new FileWriter(filePath, true)) {

            String line = bufferedReader.readLine();
            while (line != null) {
                String[] parts = line.split(",");
                line = bufferedReader.readLine();
            }

            // user does not exist in file, so add to file
            writer.write(hotel.hotelName + "," + hotel.stars + "," + hotel.roomNumber + "," + hotel.bedNumber + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
