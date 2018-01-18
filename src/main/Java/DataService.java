package main.Java;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Used manage the data of a peer.
 */
public class DataService {

    private ArrayList<HashMap<String, String>> initialData;
    private ArrayList<HashMap<String, String>> data;

    /**
     * Constructor
     * @param initialData Data that has been defined by the user
     */
    DataService(ArrayList<HashMap<String, String>> initialData) {
        this.initialData = new ArrayList<>();
        this.data = new ArrayList<>();
        this.initialData = initialData;
    }

    /**
     * Used to retrieve initial data in a comma separated format
     * @return List of comma separated values
     */
    ArrayList<String> getCommaSeparatedInitialData() {
        ArrayList<String> result = new ArrayList<>();
        for(HashMap<String, String> map: this.initialData) {
            String dataString = "";
            dataString += map.get("artist");
            dataString += ",";
            dataString += map.get("title");
            result.add(dataString);
        }
        return result;
    }

    /**
     * Adds data to the service
     * @param artist Name of the artist
     * @param title Name of the title
     */
    private void addData(String artist, String title) {
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("artist", artist);
        dataMap.put("title", title);
        this.data.add(dataMap);
    }

    /**
     * Adds data to the service that comes in the same comma separated format used
     * by the method 'getCommasSeparatedInitialData()'
     * @param data Comma separated values
     */
    public void addCommaSeparatedData(String data) {
        String[] information = data.split(",");
        addData(information[0], information[1]);
    }

    /**
     * Prints all to data to std.out
     */
    public void printAllData(){
        System.out.println("Initial Data:\n");
        this.initialData.forEach((map) -> {
            System.out.println("Artist: " + map.get("artist"));
            System.out.println("Title: " + map.get("title"));
        });
        System.out.println("\nData:\n");
        this.data.forEach((map) -> {
            System.out.println("Artist: " + map.get("artist"));
            System.out.println("Title: " + map.get("title"));
        });
    }
}
