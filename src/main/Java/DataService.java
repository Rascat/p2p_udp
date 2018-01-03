package main.Java;

import java.util.ArrayList;
import java.util.HashMap;

public class DataService {

    private ArrayList<HashMap<String, String>> initialData = null;
    private ArrayList<HashMap<String, String>> data = null;

    DataService(String artist, String title) {
        HashMap<String, String> initialDataMap = new HashMap<>();
        initialDataMap.put("artist", artist);
        initialDataMap.put("title", title);
        this.initialData = new ArrayList<>();
        this.data = new ArrayList<>();
        this.initialData.add(initialDataMap);
    }

    public ArrayList<HashMap<String, String>> getInitialData() {
        return initialData;
    }

    public ArrayList<HashMap<String, String>> getData() {
        return data;
    }

    public ArrayList<HashMap<String, String>> getAllData() {
        ArrayList <HashMap<String, String>> result = new ArrayList<>();
        result.addAll(this.initialData);
        result.addAll(this.data);
        return result;
    }

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

    private void addData(String artist, String title) {
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("artist", artist);
        dataMap.put("title", title);
        this.data.add(dataMap);
    }

    public void addCommaSeparatedData(String data) {
        String[] information = data.split(",");
        addData(information[0], information[1]);
    }

    public void printInitialData(){
        this.initialData.forEach((map) -> {
            System.out.println(map.get("artist"));
            System.out.println(map.get("title"));
        });
    }

    public void printData(){
        this.data.forEach((map) -> {
            System.out.println(map.get("artist"));
            System.out.println(map.get("title"));
        });
    }

    public void printAllData(){
        this.initialData.forEach((map) -> {
            System.out.println(map.get("artist"));
            System.out.println(map.get("title"));
        });
        this.data.forEach((map) -> {
            System.out.println(map.get("artist"));
            System.out.println(map.get("title"));
        });
    }
}
