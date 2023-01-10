package rotor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
public class Rotor {

    private final int rotorId;
    private final int notchPosition;
    private int rotorPosition;
    private int amountOfEntriesInRotor;
    public ArrayList<Character> forwardEntry;
    public ArrayList<Character> backwardEntry;


    public Rotor(int rotorId, int notchPosition, int amountOfEntriesInRotor, ArrayList<Character> forwardEntry, ArrayList<Character> backwardEntry) {
        this.rotorId = rotorId;
        this.notchPosition = notchPosition;
        this.amountOfEntriesInRotor = amountOfEntriesInRotor;
        this.forwardEntry = (ArrayList<Character>) forwardEntry.clone();
        this.backwardEntry = (ArrayList<Character>) backwardEntry.clone();

    }
    //This method returns the letter that is now in the small window (the "top" place of the rotor.)
    public int getRotorPosition() {
        return this.rotorPosition;
    }

    public int getNotchPosition() {
        return notchPosition;
    }

    public int getRotorId(){
        return this.rotorId;
    }

    //This method checks if the current position is the notch position.
    public boolean isAtNotchPosition() {
        if (this.rotorPosition == this.notchPosition) {
            return true;
        } else {
            return false;
        }
    }

    public int getAmountOfEntriesInRotor() {
        return amountOfEntriesInRotor;
    }

    //This method turns the rotor to the starting position. ( The starting position is the letter that we should see from the small window)
    public void setRotorToStartingPosition(int position) {

        this.rotorPosition = position;
        Collections.rotate(this.forwardEntry, this.amountOfEntriesInRotor - this.rotorPosition);
        Collections.rotate(this.backwardEntry, this.amountOfEntriesInRotor - this.rotorPosition);
    }
    public void setRotors(int position){
            this.rotorPosition = position;
            //Collections.rotate(this.forwardEntry, -1);
            //Collections.rotate(this.backwardEntry, -1);

    }

    public void setRotorPosition(int rotorPosition) {
        this.rotorPosition = rotorPosition;
    }

    //This method changes the rotor position by one step by rotating the rotor in one
    public void turnAround() {

        this.rotorPosition = (this.rotorPosition + 1) % (this.amountOfEntriesInRotor);
        Collections.rotate(this.forwardEntry, -1);//or (amountOfEntriesInRotor-1)
        Collections.rotate(this.backwardEntry, -1);// ""
    }

    public int encodeForward(char characterToLookFor) {
        for (int i = 0; i < this.amountOfEntriesInRotor; i++) {
            if (backwardEntry.get(i) == characterToLookFor)
                return i;
        }
        return -1;
    }

    public int encodeBackward(char characterToLookFor) {
        for (int i = 0; i < this.amountOfEntriesInRotor; i++) {
            if (forwardEntry.get(i) == characterToLookFor)
                return i;
        }
        return -1;
    }

}





















