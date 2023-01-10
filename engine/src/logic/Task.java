package logic;

import machine.EnigmaMachine;

import java.util.ArrayList;
import java.util.HashSet;

public class Task {
   // private EnigmaMachine enigmaMachine;
    //private String encryptedString;
    private ArrayList<Integer> startingPosition;
    //private ArrayList<Integer> finishingPosition;
    private double index;
    private int totalTasks;
    //private ArrayList<Integer>rotorsId;
    private int[]rotorsId;
    private int reflectorId;
    //private int sizeOfTask;




    public Task(int[]rotorsId,int reflectorId, ArrayList<Integer> startingPosition,double index,int totalTasks){
        //this.enigmaMachine=machine;
        this.rotorsId=rotorsId;
        this.reflectorId=reflectorId;
        //this.encryptedString=encryptedString;
        this.startingPosition=startingPosition;
        this.index=index;
        this.totalTasks=totalTasks;
        //this.sizeOfTask=sizeOfTask;
    }
   // public EnigmaMachine getEnigmaMachine() {
     //   return enigmaMachine;
    //}

    public int getReflectorId() {
        return reflectorId;
    }

    //public ArrayList<Integer> getRotorsId() {
      //  return rotorsId;
    //}

    public int[] getRotorsId() {
        return rotorsId;
    }


    //public String getEncryptedString() {
      //  return encryptedString;
    //}


   // public int getSizeOfTask() {
     //   return sizeOfTask;
    //}

//    public ArrayList<Integer> getThePositions() {
  //      return thePositions;
    //}



    public ArrayList<Integer> getStartingPosition() {
        return startingPosition;
    }

    //public ArrayList<Integer> getFinishingPosition() {
      //  return finishingPosition;
    //}

    public int getTotalTasks() {
        return totalTasks;
    }

    public double getIndex() {
        return index;
    }
}
