package src;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

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
                hotelIDs.add(this.hotelID); // Add the new ID to the list
            }
        }

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

        if (!hotelExists){
            addToFile(this);
            this.hotelExists = true;
        }

        System.out.println("THIS IS CALLED TO ADD");
        //createBedOccupancyFile(this.hotelName);
        //createRoomOccupancyFile(this.hotelName);
        addToOccupancyFile();

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


        //createBedOccupancyFile(this.hotelName);
        //createRoomOccupancyFile(this.hotelName);
        fillOccupancyFile();
        System.out.println(this.hotelID);

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

        //createBedOccupancyFile(this.hotelName);
        //createRoomOccupancyFile(this.hotelName);
        fillOccupancyFile();
        System.out.println(this.hotelID);

        if (!hotelExists){
            addToFile(this);
            this.hotelExists = true;
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

            writer.write(this.toStringSimple() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addToOccupancyFile() {
        String occupancyFile = "data/occupancies.txt";
        File occupancy = new File(occupancyFile);

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        try (FileWriter fileWriter = new FileWriter(occupancy, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            for (int year = 2014; year <= currentYear; year++) {
                for (int month = 1; month <= 12; month++) {
                    String newLine = this.hotelID + "," + this.roomNumber + ",0," + this.bedNumber + ",0," + year + "," + month;
                    bufferedWriter.write(newLine);
                    bufferedWriter.newLine();
                }
            }

            System.out.println("Data added to the occupancy file.");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to add data to the occupancy file.");
        }
    }


    private void fillOccupancyFile() {
        String hotelFile = "data/hotelData.txt";
        String occupancyFile = "data/occupancies.txt";
        File hotels = new File(hotelFile);
        File occupancy = new File(occupancyFile);

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        int lastHotelID = getLastHotelID(hotels);
        if (lastHotelID == -1) {
            System.out.println("No hotels found. Data generation stopped.");
            return;
        }


        // Check if any data exists for the last hotelID
        if (yearExists(occupancy, lastHotelID)) {
            System.out.println("Data already exists for the last hotel ID. Skipping data generation.");
            return;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(occupancy, true))) {
            IntStream.rangeClosed(2014, currentYear)
                    .parallel() // Perform parallel processing
                    .forEach(year -> {
                        // Check if the year exists for the last hotelID
                        if (!yearExists(occupancy, year, lastHotelID)) {
                            IntStream.rangeClosed(1, 12)
                                    .forEach(month -> {
                                        String newLine = lastHotelID + "," + this.roomNumber + "," + 0 + "," +
                                                this.bedNumber + "," + 0 + "," + year + "," + month;
                                        writer.println(newLine);
                                    });
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getLastHotelID(File hotelsFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(hotelsFile))) {
            String line;
            String lastLine = null;
            while ((line = reader.readLine()) != null) {
                lastLine = line;
            }
            if (lastLine != null) {
                String[] parts = lastLine.split(",");
                if (parts.length >= 1) {
                    return Integer.parseInt(parts[0].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private boolean yearExists(File occupancyFile, int hotelID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(occupancyFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    int existingHotelID = Integer.parseInt(parts[0].trim());
                    if (existingHotelID == hotelID) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean yearExists(File occupancyFile, int year, int hotelID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(occupancyFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7)
                {
                    int existingYear = Integer.parseInt(parts[5].trim());
                    int existingHotelID = Integer.parseInt(parts[0].trim());
                    if (existingYear == year && existingHotelID == hotelID) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}

