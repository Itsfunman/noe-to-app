package src;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
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
    private int bedNumber;
    private HashMap <String, Boolean> extras;

    public Hotel(String hotelName, String stars, String roomNumber, String bedNumber){
        this.hotelID = COUNTER++;
        this.hotelName = hotelName;
        this.stars = Integer.parseInt(stars);
        this.roomNumber = Integer.parseInt(roomNumber);
        this.bedNumber = Integer.parseInt(bedNumber);

        createBedOccupancyFile();
        createRoomOccupancyFile();
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

    public int getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(int bedNumber) {
        this.bedNumber = bedNumber;
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

    public void createBedOccupancyFile(){

        String name = "data/" + hotelName + "BedOccupancy.txt";
        File file = new File(name);

        if (!file.exists()) {

            String baseContent = "";

            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);

            for (int i = 2000; i <= currentYear; i++) {
                for (int j = 0; j < 12; j++) {
                    baseContent = baseContent + "0,";
                }
                baseContent = baseContent + "\n";
            }

            try {
                FileWriter writer = new FileWriter(name);
                writer.write(baseContent);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void createRoomOccupancyFile(){

        String name = "data/" + hotelName + "RoomOccupancy.txt";
        File file = new File(name);

        if (!file.exists()) {

            String baseContent = "";

            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);

            for (int i = 2000; i <= currentYear; i++) {
                for (int j = 0; j < 12; j++) {
                    baseContent = baseContent + "0,";
                }
                baseContent = baseContent + "\n";
            }

            try {
                FileWriter writer = new FileWriter(name);
                writer.write(baseContent);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
