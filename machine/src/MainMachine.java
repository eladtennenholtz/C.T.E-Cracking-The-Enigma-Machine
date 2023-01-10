import machine.EnigmaMachine;

import java.util.ArrayList;
import java.util.HashMap;

public class MainMachine {
    public static void main(String[] args) {

        EnigmaMachine e = new EnigmaMachine(2, "ABCDEFGHIJKLNMOPQRSTUVWXYZ");

        HashMap<Integer, Integer> reflectorTemp1 = new HashMap<>();
        reflectorTemp1.put(0,24); reflectorTemp1.put(24,0); reflectorTemp1.put(1,17); reflectorTemp1.put(17,1);
        reflectorTemp1.put(2,20); reflectorTemp1.put(20,2);reflectorTemp1.put(3,7);reflectorTemp1.put(7,3);reflectorTemp1.put(4,16);reflectorTemp1.put(16,4);reflectorTemp1.put(5,18);
        reflectorTemp1.put(18,5);reflectorTemp1.put(6,11);reflectorTemp1.put(11,6);reflectorTemp1.put(8,15);reflectorTemp1.put(15,8);reflectorTemp1.put(9,23);
        reflectorTemp1.put(23,9);reflectorTemp1.put(10,13);reflectorTemp1.put(13,10);reflectorTemp1.put(12,14);reflectorTemp1.put(14,12);reflectorTemp1.put(19,25);
        reflectorTemp1.put(25,19);reflectorTemp1.put(21,22);reflectorTemp1.put(21,22);
        e.addReflector(1, reflectorTemp1);


        ArrayList<Character> rotor1Forward = new ArrayList<>();
        ArrayList<Character> rotor1Backward = new ArrayList<>();
        rotor1Forward.add('A');rotor1Forward.add('B');rotor1Forward.add('C');rotor1Forward.add('D');rotor1Forward.add('E');rotor1Forward.add('F');
        rotor1Backward.add('F');rotor1Backward.add('E');rotor1Backward.add('D');rotor1Backward.add('C');rotor1Backward.add('B');rotor1Backward.add('A');
        e.addRotor(1,3, rotor1Forward, rotor1Backward);

        ArrayList<Character> rotor2Forward = new ArrayList<>();
        ArrayList<Character> rotor2Backward = new ArrayList<>();
        rotor2Forward.add('A');rotor2Forward.add('B');rotor2Forward.add('C');rotor2Forward.add('D');rotor2Forward.add('E');rotor2Forward.add('F');
        rotor2Backward.add('E');rotor2Backward.add('B');rotor2Backward.add('D');rotor2Backward.add('F');rotor2Backward.add('C');rotor2Backward.add('A');
        e.addRotor(2,0, rotor2Forward, rotor2Backward);

        int[] theUsedRotors = {1,2};
        e.chooseRotorsToBeUsed(theUsedRotors);
        e.chooseReflectorToBeUsed(1);

        e.plugBoard.addAnotherPlugin(0,5);
        e.usedRotors.get(0).setRotorToStartingPosition(2);
        e.usedRotors.get(1).setRotorToStartingPosition(2);
        System.out.println(e.encryption("FEDCBADDEF"));
    }





}
