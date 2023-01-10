package plugboard;

import java.util.HashMap;

public class PlugBoard {

    private int amountOfEntries;
    public HashMap<Integer,Integer>mappingPairs;

    public PlugBoard(int amountOfEntries){

        this.amountOfEntries=amountOfEntries;
        this.mappingPairs=new HashMap<>();
    }
    //This method changes the value between to numbers if there is a plugin between them.
    public int connectOnBoardForPlugins(int number){
        if(this.mappingPairs.containsKey(number)){
            return this.mappingPairs.get(number);

        }
        else {
            return number;
        }
    }
    //This method adds another plugin to the system.
    //If the plugin is between the same number it returns
    //If the plugin exists already it returns.
    public void addAnotherPlugin(int firstNumber,int secondNumber) {
        if(this.mappingPairs.containsKey(firstNumber)||this.mappingPairs.containsKey(secondNumber)){
            return;
        }
        if(firstNumber==secondNumber){
            return;
        }
        this.mappingPairs.put(firstNumber, secondNumber);
        this.mappingPairs.put(secondNumber, firstNumber);
    }

}
