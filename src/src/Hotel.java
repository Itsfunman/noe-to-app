package src;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;

public class Hotel {

    public static ArrayList<Hotel> hotels = new ArrayList();

    //hotelID counter:
    private static int COUNTER = 0;
    private int hotelID;

    public static ArrayList <Integer> hotelIDs = new ArrayList<Integer>();

    //Basic information:
    private String category;
    private String hotelName;
    private String hotelOwner;
    private String hotelContactInformation;
    private String address;
    private String city;
    private String cityCode;
    private String phoneNumber;
    private int roomNumber;
    private int bedNumber;
    private boolean family;
    private boolean dog;
    private boolean spa;
    private boolean fitness;

    private boolean hotelExists = false;


    public Hotel(String hotelName, String category, String roomNumber, String bedNumber, String hotelOwner,
                 String hotelContactInformation, String address, String city, String cityCode, String phoneNumber,
                 String family, String dog, String spa, String fitness) {

        boolean idNotAssigned = true;

        while (idNotAssigned) {
            int x = COUNTER++;
            boolean inList = false;

            for (int id : hotelIDs) {
                if (id == x) {
                    inList = true;
                    break;
                }
            }

            if (!inList) {
                idNotAssigned = false;
                this.hotelID = x;
            }
        }

        hotelIDs.add(this.hotelID);

        this.hotelName = hotelName.replaceAll("^\"|\"$", "");
        this.hotelName = this.hotelName.replaceAll("/", "");

        try {
            int categoryInt = Integer.parseInt(category);
            this.category = createCategory(categoryInt);
        } catch (Exception e){
            this.category = category;
        }


        this.roomNumber = Integer.parseInt(roomNumber);
        this.bedNumber = Integer.parseInt(bedNumber);
        this.hotelOwner = hotelOwner.replaceAll("\"", "");
        this.hotelContactInformation = hotelContactInformation.replaceAll("\"", "");
        this.address = address.replaceAll("\"", "");
        this.city = city.replaceAll("\"", "");
        this.cityCode = cityCode.replaceAll("\"", "");
        this.phoneNumber = phoneNumber.replaceAll("\"", "");
        this.family = Boolean.parseBoolean(family);
        this.dog = Boolean.parseBoolean(dog);
        this.spa = Boolean.parseBoolean(spa);
        this.fitness = Boolean.parseBoolean(fitness);


        createBedOccupancyFile(this.hotelName);
        createRoomOccupancyFile(this.hotelName);


        //Add rest with try catch blocks;
    }

    public Hotel(String id, String hotelName, String category, String roomNumber, String bedNumber, String hotelOwner,
                 String hotelContactInformation, String address, String city, String cityCode, String phoneNumber,
                 String family, String dog, String spa, String fitness) {

        this.hotelID = Integer.parseInt(id);
        hotelIDs.add(this.hotelID);

        this.hotelName = hotelName.replaceAll("^\"|\"$", "");
        this.hotelName = this.hotelName.replaceAll("/", "");

        try {
            int categoryInt = Integer.parseInt(category);
            this.category = createCategory(categoryInt);
        } catch (Exception e){
            this.category = category;
        }


        this.roomNumber = Integer.parseInt(roomNumber);
        this.bedNumber = Integer.parseInt(bedNumber);
        this.hotelOwner = hotelOwner.replaceAll("\"", "");
        this.hotelContactInformation = hotelContactInformation.replaceAll("\"", "");
        this.address = address.replaceAll("\"", "");
        this.city = city.replaceAll("\"", "");
        this.cityCode = cityCode.replaceAll("\"", "");
        this.phoneNumber = phoneNumber.replaceAll("\"", "");
        this.family = Boolean.parseBoolean(family);
        this.dog = Boolean.parseBoolean(dog);
        this.spa = Boolean.parseBoolean(spa);
        this.fitness = Boolean.parseBoolean(fitness);


        createBedOccupancyFile(this.hotelName);
        createRoomOccupancyFile(this.hotelName);


        //Add rest with try catch blocks;
    }

    public Hotel(String id, String hotelName, String category,
                 String roomNumber, String bedNumber, String hotelOwner,
                 String hotelContactInformation, String address, String city, String cityCode, String phoneNumber) {

        this.hotelID = Integer.parseInt(id);
        hotelIDs.add(this.hotelID);

        this.category = category.replaceAll("\"", "");

        this.hotelName = hotelName.replaceAll("\"", "");
        this.hotelName = this.hotelName.replaceAll("/", "");

        this.hotelOwner = hotelOwner.replaceAll("\"", "");
        this.hotelContactInformation = hotelContactInformation.replaceAll("\"", "");
        this.address = address.replaceAll("\"", "");
        this.city = city.replaceAll("\"", "");
        this.cityCode = cityCode.replaceAll("\"", "");
        this.phoneNumber = phoneNumber.replaceAll("\"", "");
        this.family = false;
        this.dog = false;
        this.spa = false;
        this.fitness = false;

        this.roomNumber = Integer.parseInt(roomNumber);
        this.bedNumber = Integer.parseInt(bedNumber);

        createBedOccupancyFile(this.hotelName);
        createRoomOccupancyFile(this.hotelName);

        if (!hotelExists){
            addToFile(this);
            hotelExists = true;
        }
    }

    public Hotel(String category, int noRooms, int noBeds) {
        this.category = createCategory(Integer.parseInt(category));
        this.roomNumber = noRooms;
        this.bedNumber= noBeds;
    }

    public String toStringSimple() {
        return hotelID + "," + hotelName + "," + category + "," + roomNumber + "," + bedNumber + "," + hotelOwner +
                "," + hotelContactInformation + "," + address + "," + city + "," + cityCode + "," + phoneNumber +
                "," + family + "," + dog + "," + spa + "," + fitness;
    }

    private String createCategory(int c){

        //StringBuilder allows us to create Strings much easier
        StringBuilder category = new StringBuilder();

        for (int i = 0; i <= c; c++){
            category.append("*");
        }

        return category.toString();
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
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

    public void createBedOccupancyFile(String hotelName) {

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

    public void createRoomOccupancyFile(String hotelName) {

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

