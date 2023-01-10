package logic;

import machine.EnigmaMachine;
import reflector.Reflector;
import rotor.Rotor;

import java.util.HashMap;
import java.util.HashSet;

public class CurrentMachineDetails {

    private EnigmaMachine machine;
    private HashMap<Integer, Rotor> allRotors;
    private int usedRotorsSize;
    private HashMap<Integer, Reflector> allReflectors;
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

    public void setMachine(EnigmaMachine machine) {
        this.machine = machine;
    }

    public void setDictionary(HashSet<String> dictionary) {
        this.dictionary = dictionary;
    }

    public void setAllRotors(HashMap<Integer, Rotor> allRotors) {
        this.allRotors = allRotors;
    }

    public void setUsedRotorsSize(int usedRotorsSize) {
        this.usedRotorsSize = usedRotorsSize;
    }

    public void setAllReflectors(HashMap<Integer, Reflector> allReflectors) {
        this.allReflectors = allReflectors;
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

    public HashMap<Integer, Rotor> getAllRotors() {
        return allRotors;
    }

    public int getUsedRotorsSize() {
        return usedRotorsSize;
    }

    public HashMap<Integer, Reflector> getAllReflectors() {
        return allReflectors;
    }

    public HashMap<Integer, Integer> getPlugBoard() {
        return plugBoard;
    }

    public String getAlphabet() {
        return alphabet;
    }
    public EnigmaMachine getMachine(){
        return machine;
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
