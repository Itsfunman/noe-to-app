package objectClasses;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;


import jakarta.persistence.*;
import lombok.*;
import sqlStuff.HotelDAO;
import sqlStuff.OccupancyDAO;
import javax.swing.*;

/**
 * The Hotel class represents a hotel and provides methods to manage hotel information and occupancy data.
 */

@Getter
@Setter
@Entity
@Table(name = "hotel")
public class Hotel {

    public static ArrayList<Hotel> hotels = new ArrayList<>();



    @Id
    @Column(name = "hotelid")
    private int hotelID;


    public static ArrayList <Integer> hotelIDs = new ArrayList<>();

    //Basic information:
    @Column(name = "kategorie")
    private String category;

    @Column(name = "hotelname")
    private String hotelName;

    @Column(name = "owner")
    private String hotelOwner;

    @Column(name = "contact")
    private String hotelContactInformation;

    @Column(name = "adress")
    private String address;

    @Column
    private String city;

    @Column(name = "plz")
    private String cityCode;

    @Column(name = "tel")
    private String phoneNumber;

    @Column
    private int roomNumber;

    @Column
    private int bedNumber;

    @Column
    private boolean family;

    @Column(name = "animals")
    private boolean dog;

    @Column
    private boolean spa;

    @Column
    private boolean fitness;



    //Used by hibernate for fetching data
    public Hotel(){
    }
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
    public Hotel(String category, String hotelName, String hotelOwner, String hotelContactInformation, String address,
                 String city, String cityCode, String phoneNumber, String roomNumber, String bedNumber,
                 String family, String dog, String spa, String fitness) {

        // Generate the hotelID by incrementing the last value in the hotelID column
        int lastHotelID = HotelDAO.getLastHotelID();
        this.hotelID = lastHotelID + 1;

        this.hotelName = hotelName.replaceAll("^\"|\"$", "");
        this.hotelName = this.hotelName.replaceAll("/", "");

        try {

            // Validate and set category
            if (!category.matches("^\\*+$")) {
                throw new IllegalArgumentException("Ungültige Kategorie! Die Kategorie muss aus einem oder mehreren Sternen (*) bestehen.");
            }
            this.category = category;

            // Validate and set hotelName
            if (hotelName.isEmpty()) {
                throw new IllegalArgumentException("Hotelname darf nicht leer sein.");
            }
            this.hotelName = hotelName.replaceAll("^\"|\"$", "");
            this.hotelName = this.hotelName.replaceAll("/", "");

            // Validate and set hotelOwner
            if (hotelOwner.isEmpty() || !hotelOwner.matches("^[a-zA-Z ]+$")) {
                throw new IllegalArgumentException("Ungültiger Hotelbesitzer! Der Hotelbesitzer darf nicht leer sein und darf keine Zahlen oder Sonderzeichen enthalten.");
            }
            this.hotelOwner = hotelOwner.replaceAll("\"", "");

            // Validate and set hotelContactInformation
            if (hotelContactInformation.isEmpty()) {
                throw new IllegalArgumentException("Kontaktinformationen dürfen nicht leer sein.");
            }
            this.hotelContactInformation = hotelContactInformation.replaceAll("\"", "");

            // Validate and set address
            if (address.isEmpty()) {
                throw new IllegalArgumentException("Adresse darf nicht leer sein.");
            }
            this.address = address.replaceAll("\"", "");

            // Validate and set city
            if (city.isEmpty() || !city.matches("^[a-zA-Z]+$")) {
                throw new IllegalArgumentException("Ungültige Stadt! Der Stadname darf nicht leer sein und darf keine Zahlen oder Sonderzeichen enthalten.");
            }
            this.city = city.replaceAll("\"", "");

            // Validate and set cityCode
            if (cityCode.isEmpty() || !cityCode.matches("^[0-9]+$") || cityCode.length() > 5) {
                throw new IllegalArgumentException("Ungültiger Citycode! Der Citycode darf nicht leer sein, muss aus Zahlen bestehen und darf nicht länger als 5 Zahlen sein.");
            }
            this.cityCode = cityCode.replaceAll("\"", "");

            // Validate and set phoneNumber
            if (phoneNumber.isEmpty() || !phoneNumber.matches("^[0-9]+$")) {
                throw new IllegalArgumentException("Ungültige Telefonnummer! Die Telefonnummer darf nicht leer sein und darf nur Zahlen enthalten.");
            }
            this.phoneNumber = phoneNumber.replaceAll("\"", "");

            // Validate and set roomNumber
            int roomNumberInt = Integer.parseInt(roomNumber);
            if (roomNumberInt <= 0) {
                throw new IllegalArgumentException("Ungültige Anzahl von Zimmern! Die Anzahl der Zimmer muss größer als 0 sein.");
            }
            this.roomNumber = roomNumberInt;

            // Validate and set bedNumber
            int bedNumberInt = Integer.parseInt(bedNumber);
            if (bedNumberInt <= 0) {
                throw new IllegalArgumentException("Ungültige Anzahl von Betten! Die Anzahl der Betten muss größer als 0 sein.");
            }
            this.bedNumber = bedNumberInt;

            // Validate and set family, dog, spa, fitness
            if (!family.equals("true") && !family.equals("false")) {
                throw new IllegalArgumentException("Ungültiger Wert für Family! Der Wert muss entweder \"true\" oder \"false\" sein.");
            }
            this.family = Boolean.parseBoolean(family);

            if (!dog.equals("true") && !dog.equals("false")) {
                throw new IllegalArgumentException("Ungültiger Wert für Dog! Der Wert muss entweder \"true\" oder \"false\" sein.");
            }
            this.dog = Boolean.parseBoolean(dog);

            if (!spa.equals("true") && !spa.equals("false")) {
                throw new IllegalArgumentException("Ungültiger Wert für Spa! Der Wert muss entweder \"true\" oder \"false\" sein.");
            }
            this.spa = Boolean.parseBoolean(spa);

            if (!fitness.equals("true") && !fitness.equals("false")) {
                throw new IllegalArgumentException("Ungültiger Wert für Fitness! Der Wert muss entweder \"true\" oder \"false\" sein.");
            }
            this.fitness = Boolean.parseBoolean(fitness);

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

            // Add hotel to the occupancy table
            OccupancyDAO.addHotelToOccupancyTable(this);
        } catch (NumberFormatException e) {
            showErrorMessage("Ungültige Eingabe! Die Zimmeranzahl und Bettenanzahl müssen ganze Zahlen sein.");
        } catch (IllegalArgumentException e) {
            showErrorMessage(e.getMessage());
        }
    }

    private void showErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "Fehlermeldung", JOptionPane.ERROR_MESSAGE);
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
        OccupancyDAO.addHotelToOccupancyTable(this);
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

    public Object[] toObjectArray() {
        Object[] dataArray = new Object[15];
        dataArray[0] = hotelID;
        dataArray[1] = category;
        dataArray[2] = hotelName;
        dataArray[3] = hotelOwner;
        dataArray[4] = hotelContactInformation;
        dataArray[5] = address;
        dataArray[6] = city;
        dataArray[7] = cityCode;
        dataArray[8] = phoneNumber;
        dataArray[9] = roomNumber;
        dataArray[10] = bedNumber;
        dataArray[11] = family;
        dataArray[12] = dog;
        dataArray[13] = spa;
        dataArray[14] = fitness;
        return dataArray;
    }
}

