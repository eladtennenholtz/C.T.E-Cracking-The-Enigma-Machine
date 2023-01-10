package data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import machine.EnigmaMachine;
import reflector.Reflector;
import rotor.Rotor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class CurrentDetailsMachine {

    private HashMap<Integer, FrontRotor> allRotors;
    private int usedRotorsSize;
    private HashMap<Integer, FrontReflector> allReflectors;
    private HashMap<Integer,Integer>plugBoard;
    private String alphabet;
    private HashSet<String> dictionary;
    private int numberOfAgents;
    private int sizeOfAlphabet;
    private int numberOfAllies;
    private String level;
    private int allNumberOfRotors;
    private int allNumberOfReflectors;


    public void setAllNumberOfRotors(int allNumberOfRotors) {
        this.allNumberOfRotors = allNumberOfRotors;
    }

    public void setAllNumberOfReflectors(int allNumberOfReflectors) {
        this.allNumberOfReflectors = allNumberOfReflectors;
    }

    public int getAllNumberOfRotors() {
        return allNumberOfRotors;
    }

    public int getAllNumberOfReflectors() {
        return allNumberOfReflectors;
    }

    public void setSizeOfAlphabet(int sizeOfAlphabet) {
        this.sizeOfAlphabet = sizeOfAlphabet;
    }

    public void setDictionary(JsonArray jsonArray) {
        this.dictionary=new HashSet<>();
        String tmp="";
        for(int i=0;i<jsonArray.size();i++){
            tmp=jsonArray.get(i).toString();
            StringBuilder stringBuilder=new StringBuilder();
            for(int j=0;j<tmp.length();j++){
                if(tmp.charAt(j)!='"'){
                    stringBuilder.append(tmp.charAt(j));
                }
            }
            dictionary.add(stringBuilder.toString());
        }

    }

    public void setAllRotors(JsonObject jsonObject) {
        this.allRotors=new HashMap<>();
        for(int i=0;i<getAllNumberOfRotors();i++){
            JsonElement jsonElement=jsonObject.get(Integer.toString(i+1));
            JsonObject object=jsonElement.getAsJsonObject();
            String tmpForward=object.get("forwardEntry").toString();
            String tmpBackward=object.get("backwardEntry").toString();
            ArrayList<Character>front=new ArrayList<>();
            ArrayList<Character>back=new ArrayList<>();
            for(int j=1;j<tmpForward.length()-1;j++){
                if(tmpForward.charAt(j)!='"'&&tmpForward.charAt(j)!=',') {
                    front.add(tmpForward.charAt(j));
                }
                if(tmpBackward.charAt(j)!='"'&& tmpBackward.charAt(j)!=',') {
                    back.add(tmpBackward.charAt(j));
                }
            }
            FrontRotor frontRotor=new FrontRotor(object.get("rotorId").getAsInt(),object.get("notchPosition").getAsInt(),object.get("amountOfEntriesInRotor").getAsInt(),front,back);
            allRotors.put(i+1,frontRotor);
        }
    }

    public void setAllReflectors(JsonObject jsonObject) {

        this.allReflectors=new HashMap<>();
        for(int i=0;i<getAllNumberOfReflectors();i++){
            JsonElement jsonElement=jsonObject.get(Integer.toString(i+1));
            JsonObject object=jsonElement.getAsJsonObject();
            String tmp=object.get("reflectorMapping").toString();
            ArrayList<Character>newTmp=new ArrayList<>();
            HashMap<Integer,Integer>mappedReflector=new HashMap<>();
            for(int k=1;k<tmp.length()-1;k++){
                if(tmp.charAt(k)!='"'){
                    newTmp.add(tmp.charAt(k));
                }
            }
            int tmpLeft=0;
            int tmpRight=0;
            for(int j=0;j<newTmp.size();j++){
                StringBuilder stringBuilder=new StringBuilder();
                while(newTmp.get(j)!=':'){
                    stringBuilder.append(newTmp.get(j));
                    j++;
                }
                j++;
                tmpLeft=Integer.parseInt(stringBuilder.toString());
                stringBuilder=new StringBuilder();
                while(newTmp.get(j)!=','){
                    stringBuilder.append(newTmp.get(j));
                    j++;
                    if(j>=newTmp.size()){
                        break;
                    }
                }

                tmpRight=Integer.parseInt(stringBuilder.toString());
                mappedReflector.put(tmpLeft,tmpRight);
            }
            FrontReflector frontReflector=new FrontReflector(object.get("reflectorId").getAsInt(),mappedReflector);
            this.allReflectors.put(object.get("reflectorId").getAsInt(),frontReflector);
        }



    }
    public void setUsedRotorsSize(int usedRotorsSize) {
        this.usedRotorsSize = usedRotorsSize;
    }

    public void setPlugBoard(HashMap<Integer, Integer> plugBoard) {
        this.plugBoard = plugBoard;
    }
    public void setAlphabet(String alphabet) {
        this.alphabet=alphabet;
    }
    public void setNumberOfAgents(int numberOfAgents){this.numberOfAgents=numberOfAgents;}

    public void setLevel(String level) {
        this.level = level;
    }

    public void setNumberOfAllies(int numberOfAllies) {
        this.numberOfAllies = numberOfAllies;
    }


    public HashMap<Integer, FrontRotor> getAllRotors() {
        return allRotors;
    }

    public int getUsedRotorsSize() {
        return usedRotorsSize;
    }

    public HashMap<Integer, FrontReflector> getAllReflectors() {
        return allReflectors;
    }

    public HashMap<Integer, Integer> getPlugBoard() {
        return plugBoard;
    }

    public String getAlphabet() {
        return alphabet;
    }

    public HashSet<String> getDictionary() {
        return dictionary;
    }

    public int getNumberOfAgents() {
        return numberOfAgents;
    }

    public int getSizeOfAlphabet() {
        return sizeOfAlphabet;
    }

    public int getNumberOfAllies() {
        return numberOfAllies;
    }

    public String getLevel() {
        return level;
    }



}
