package utilityClasses;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.stream.IntStream;


/**
 * The Hotel class represents a hotel and provides methods to manage hotel information and occupancy data.
 */
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

    /**
     * Constructs a Hotel object with the provided information.
     *
     * @param hotelName                the name of the hotel
     * @param category                 the category of the hotel
     * @param roomNumber               the number of rooms in the hotel
     * @param bedNumber                the number of beds in the hotel
     * @param hotelOwner               the owner of the hotel
     * @param hotelContactInformation  the contact information of the hotel
     * @param address                  the address of the hotel
     * @param city                     the city where the hotel is located
     * @param cityCode                 the code of the city where the hotel is located
     * @param phoneNumber              the phone number of the hotel
     * @param family                   indicates if the hotel is family-friendly
     * @param dog                      indicates if the hotel allows dogs
     * @param spa                      indicates if the hotel has a spa
     * @param fitness                  indicates if the hotel has a fitness center
     */
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

    /**
     * Constructs a Hotel object with the provided information, including the hotel ID.
     *
     * @param id                       the ID of the hotel
     * @param hotelName                the name of the hotel
     * @param category                 the category of the hotel
     * @param roomNumber               the number of rooms in the hotel
     * @param bedNumber                the number of beds in the hotel
     * @param hotelOwner               the owner of the hotel
     * @param hotelContactInformation  the contact information of the hotel
     * @param address                  the address of the hotel
     * @param city                     the city where the hotel is located
     * @param cityCode                 the code of the city where the hotel is located
     * @param phoneNumber              the phone number of the hotel
     * @param family                   indicates if the hotel is family-friendly
     * @param dog                      indicates if the hotel allows dogs
     * @param spa                      indicates if the hotel has a spa
     * @param fitness                  indicates if the hotel has a fitness center
     */
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
        //fillOccupancyFile();
        //System.out.println(this.hotelID);

        //Add rest with try catch blocks;
    }

    /**
     * Constructs a Hotel object with the provided information.
     *
     * @param id                     the hotel ID
     * @param hotelName              the hotel name
     * @param category               the hotel category
     * @param roomNumber             the number of rooms in the hotel
     * @param bedNumber              the number of beds in each room
     * @param hotelOwner             the hotel owner
     * @param hotelContactInformation the contact information for the hotel
     * @param address                the address of the hotel
     * @param city                   the city where the hotel is located
     * @param cityCode               the code of the city where the hotel is located
     * @param phoneNumber            the phone number of the hotel
     */
    public Hotel(String id, String hotelName, String category,
                 String roomNumber, String bedNumber, String hotelOwner,
                 String hotelContactInformation, String address, String city, String cityCode, String phoneNumber) {

        // Parse and assign the hotel ID
        this.hotelID = Integer.parseInt(id);
        hotelIDs.add(this.hotelID);

        // Remove quotes from the category, hotel name, owner, contact information, address, city, city code, and phone number
        this.category = category.replaceAll("\"", "");
        this.hotelName = hotelName.replaceAll("\"", "");
        this.hotelName = this.hotelName.replaceAll("/", "");
        this.hotelOwner = hotelOwner.replaceAll("\"", "");
        this.hotelContactInformation = hotelContactInformation.replaceAll("\"", "");
        this.address = address.replaceAll("\"", "");
        this.city = city.replaceAll("\"", "");
        this.cityCode = cityCode.replaceAll("\"", "");
        this.phoneNumber = phoneNumber.replaceAll("\"", "");

        // Set additional features to false
        this.family = false;
        this.dog = false;
        this.spa = false;
        this.fitness = false;

        // Parse room number and bed number
        this.roomNumber = Integer.parseInt(roomNumber);
        this.bedNumber = Integer.parseInt(bedNumber);

        // Add the hotel to the file and update the occupancy file if the hotel doesn't already exist
        if (!hotelExists){
            addToFile(this);
            addToOccupancyFile();
            this.hotelExists = true;
        }
    }

    /**
     * Constructs a Hotel object with the provided category, number of rooms, and number of beds.
     *
     * @param category   the hotel category
     * @param noRooms    the number of rooms in the hotel
     * @param noBeds     the number of beds in each room
     */
    public Hotel(String category, int noRooms, int noBeds) {
        this.category = createCategory(Integer.parseInt(category));
        this.roomNumber = noRooms;
        this.bedNumber = noBeds;
    }

    /**
     * Returns a simplified string representation of the Hotel object.
     *
     * @return a string representation of the Hotel object
     */
    public String toStringSimple() {
        return hotelID + "," + hotelName + "," + category + "," + roomNumber + "," + bedNumber + "," + hotelOwner +
                "," + hotelContactInformation + "," + address + "," + city + "," + cityCode + "," + phoneNumber +
                "," + family + "," + dog + "," + spa + "," + fitness;
    }

    /**
     * Creates a category string based on the given category value.
     *
     * @param c the category value
     * @return the category string representation
     */
    private String createCategory(int c){
        // StringBuilder allows us to create Strings much easier
        StringBuilder category = new StringBuilder();

        for (int i = 0; i <= c; i++){
            category.append("*");
        }

        return category.toString();
    }

    /**
     * Returns the hotel ID.
     *
     * @return the hotel ID
     */
    public int getHotelID() {
        return hotelID;
    }

    /**
     * Returns the hotel name.
     *
     * @return the hotel name
     */
    public String getHotelName() {
        return hotelName;
    }

    /**
     * Returns the hotel category.
     *
     * @return the hotel category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Returns the number of rooms in the hotel.
     *
     * @return the number of rooms
     */
    public int getRoomNumber() {
        return roomNumber;
    }

    /**
     * Returns the number of beds in each room.
     *
     * @return the number of beds
     */
    public int getBedNumber() {
        return bedNumber;
    }

    /**
     * Adds the hotel to the file.
     *
     * @param hotel the hotel to add
     */
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

    /**
     * Adds the hotel to the occupancy file.
     */
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
    /**
     * Fills the occupancy file with data for each year and month for the last hotel ID.
     */
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

    /**
     * Retrieves the last hotel ID from the hotel data file.
     *
     * @param hotelsFile the hotel data file
     * @return the last hotel ID
     */
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

    /**
     * Checks if the occupancy file contains data for a specific hotel ID.
     *
     * @param occupancyFile the occupancy file
     * @param hotelID       the hotel ID
     * @return true if the occupancy file contains data for the hotel ID, false otherwise
     */
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

    /**
     * Checks if the occupancy file contains data for a specific year and hotel ID.
     *
     * @param occupancyFile the occupancy file
     * @param year          the year
     * @param hotelID       the hotel ID
     * @return true if the occupancy file contains data for the year and hotel ID, false otherwise
     */
    private boolean yearExists(File occupancyFile, int year, int hotelID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(occupancyFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
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

