package keyboard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class KeyBoard {

    private Set<Character> alphabeticCharacters;
    private int numberOfLetters;
    private HashMap<Integer,Character>FromNumberToCharacterMapping;
    private HashMap<Character,Integer>FromCharacterToNumberMapping;

    public KeyBoard(String alphabet){
        Set<Character> temp=new HashSet<>();
        this.alphabeticCharacters=new HashSet<Character>();
        this.FromNumberToCharacterMapping=new HashMap<Integer,Character>();
        this.FromCharacterToNumberMapping=new HashMap<Character,Integer>();
        for(char ch: alphabet.toCharArray()){
            temp.add(ch);
        }
        int count=0;
        for (char ch : temp) {
            this.alphabeticCharacters.add(ch);
            this.FromCharacterToNumberMapping.put(ch, count);
            this.FromNumberToCharacterMapping.put(count, ch);
            count++;
        }
        this.numberOfLetters = count;
    }
    public int getNumberOfLetters() {
        return this.numberOfLetters;
    }
    public int mapCharacterToNumber(char character){

        return this.FromCharacterToNumberMapping.get(character);

    }
    public char mapNumberToCharacter(int number){
        return this.FromNumberToCharacterMapping.get(number);
    }
}
