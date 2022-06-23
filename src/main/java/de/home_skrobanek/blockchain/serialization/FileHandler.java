package de.home_skrobanek.blockchain.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.home_skrobanek.blockchain.Blockchain;

import java.io.*;

public class FileHandler {

    private Gson gson;

    public FileHandler(){
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void writeToFile(String fileName, Blockchain chain){
        String json = gson.toJson(chain);

        try {
            FileWriter fw = new FileWriter(new File(fileName));
            BufferedWriter bw = new BufferedWriter(fw);

            fw.write(json);

            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Blockchain readFromFile(String fileName){
        try {
            FileReader fr = new FileReader(new File(fileName));
            BufferedReader br = new BufferedReader(fr);

            String line = "", json = "";
            while((line = br.readLine()) != null){
                json += line;
            }
            return gson.fromJson(json, Blockchain.class);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
