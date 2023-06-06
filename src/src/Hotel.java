package src;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;

public class Hotel {

    public static ArrayList<Hotel> hotels = new ArrayList();

    //hotelID counter:
    private static int COUNTER = 0;
    private int hotelID;

    //Basic information:
    private int category;
    private String hotelName;
    private String hotelOwner;
    private String hotelContactInformation;
    private String address;
    private String city;
    private int cityCode;
    private Long phoneNumber;
    private int roomNumber;
    private int bedNumber;
    private boolean family;
    private boolean dog;
    private boolean spa;
    private boolean fitness;


    public Hotel(String hotelName, String category, String roomNumber, String bedNumber, String hotelOwner,
                 String hotelContactInformation, String address, String city, String cityCode, String phoneNumber,
                 String family, String dog, String spa, String fitness) {
        this.hotelID = COUNTER++;
        this.hotelName = hotelName;
        try {
            this.category = Integer.parseInt(category);
            this.roomNumber = Integer.parseInt(roomNumber);
            this.bedNumber = Integer.parseInt(bedNumber);
            this.cityCode = Integer.parseInt(cityCode);
            this.phoneNumber = Long.parseLong(phoneNumber);
        } catch (NumberFormatException e) {
            // Handle the error, e.g., display an error message or provide default values
            e.printStackTrace();
            return; // exit the constructor or handle the error in a different way
        }
        // Rest of the constructor code...
    }
    public String toStringSimple() {
        return hotelID + "," + hotelName + "," + category + "," + roomNumber + "," + bedNumber + "," + hotelOwner +
                "," + hotelContactInformation + "," + address + "," + city + "," + cityCode + "," + phoneNumber +
                "," + family + "," + dog + "," + spa + "," + fitness;
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

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
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

    public String getHotelOwner() {
        return hotelOwner;
    }

    public void setHotelOwner(String hotelOwner) {
        this.hotelOwner = hotelOwner;
    }

    public String getHotelContactInformation() {
        return hotelContactInformation;
    }

    public void setHotelContactInformation(String hotelContactInformation) {
        this.hotelContactInformation = hotelContactInformation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
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
            writer.write(this.toStringSimple() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createBedOccupancyFile() {

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

    public void createRoomOccupancyFile() {

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

