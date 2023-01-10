package machine;

import keyboard.KeyBoard;
import plugboard.PlugBoard;
import reflector.Reflector;
import rotor.Rotor;
import java.util.ArrayList;
import java.util.HashMap;

public class EnigmaMachine {

    public HashMap<Integer, Rotor> allRotors;  //All the rotors that exist
    public ArrayList <Rotor>usedRotors; //All the rotors that will be used in the machine
    public HashMap<Integer,Reflector>allReflectors;  //All the reflectors that exist
    public Reflector usedReflector;  //The reflector that will be used in the machine
    public PlugBoard plugBoard; //The plugBoard that will be used in the machine
    public KeyBoard keyboard; //The keyBoard that will be used in the machine
    private final int alphabetSize; //The number of letters that this machine has
    private final int usedRotorsCount; //The number of rotors that this machine uses
    private String alphabet;

    //Enigma Machine receives how many rotors it is using and the letters it has in the alphabet.
    public EnigmaMachine(int usedRotorsCount,String alphabet){

        this.usedRotorsCount=usedRotorsCount;
        this.keyboard=new KeyBoard(alphabet);
        this.alphabetSize= this.keyboard.getNumberOfLetters();
        this.allRotors=new HashMap<>();
        this.usedRotors=new ArrayList<>();
        this.allReflectors=new HashMap<>();
        this.plugBoard=new PlugBoard(this.alphabetSize);
        this.alphabet=alphabet;
    }


    public int getUsedRotorsCount() {
        return usedRotorsCount;
    }

    public int getAlphabetSize() {
        return alphabetSize;
    }

    public String getAlphabet() {
        return alphabet;
    }

    public void rotate () { // Turns the first rotor. If the notch is at the first position at the second one it rotates it too.
        this.usedRotors.get(0).turnAround();
        for(int i = 0; i < usedRotorsCount-1; i++) {
            if (usedRotors.get(i).isAtNotchPosition()) {
                usedRotors.get(i+1).turnAround();
            }
        }
    }
    public void addRotor(int rotorId,int notchPosition,ArrayList<Character> forwardEntry, ArrayList<Character> backwardEntry){

        Rotor rotor=new Rotor(rotorId,notchPosition,this.alphabetSize,forwardEntry,backwardEntry);
        this.allRotors.put(rotorId,rotor);
    }
    public void addReflector(int reflectorId,HashMap<Integer,Integer> reflectorMap){
        Reflector reflector=new Reflector(reflectorId,reflectorMap);
        this.allReflectors.put(reflectorId,reflector);
    }

    public void chooseRotorsToBeUsed(int[]rotors){
        for (int i=0;i<rotors.length;i++){
            Rotor r = duplicate(allRotors.get(rotors[i]));
            this.usedRotors.add(r);
        }
    }

    private Rotor duplicate(Rotor rotor) {
        Rotor retVal = new Rotor(rotor.getRotorId(), rotor.getNotchPosition(),rotor.getAmountOfEntriesInRotor(),rotor.forwardEntry,rotor.backwardEntry);
        return retVal;
    }

    public void chooseReflectorToBeUsed(int reflector){

        this.usedReflector=this.allReflectors.get(reflector);

    }

    public String encryption(String stringToBeEncrypted){
        StringBuilder resultString = new StringBuilder();
        int temp=0;
        char result=0;
        char[] inputLetters = stringToBeEncrypted.toCharArray();
        for(char ch:inputLetters){
            rotate();
            temp=this.keyboard.mapCharacterToNumber(ch);
            temp=this.plugBoard.connectOnBoardForPlugins(temp);
            temp=this.rotorTravelForward(temp);
            temp=this.usedReflector.mappingOfReflector(temp);
            temp=this.rotorTravelBackwards(temp);
            temp=this.plugBoard.connectOnBoardForPlugins(temp);
            result=this.keyboard.mapNumberToCharacter(temp);
            resultString=resultString.append(result);
        }
        return resultString.toString();
    }

    public int rotorTravelForward (int numberFromTheInput) {
        int temp = numberFromTheInput;
        for (Rotor rotor : usedRotors) {
            temp = rotor.encodeForward(rotor.forwardEntry.get(temp));
        }
        return temp;
    }

    public int rotorTravelBackwards (int numberFromReflector) {
        int temp = numberFromReflector;
        for (int i = usedRotorsCount-1; i >= 0; i--) {
            temp = usedRotors.get(i).encodeBackward(usedRotors.get(i).backwardEntry.get(temp));
        }
        return temp;
    }

    public ArrayList<Rotor> getUsedRotors() {
        return usedRotors;
    }
}
