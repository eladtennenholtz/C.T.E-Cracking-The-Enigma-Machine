package users;

import logic.Ally;
import logic.BattleField;

import java.util.HashMap;
import java.util.HashSet;

public class AlliesManager {
    private HashSet<String> allAlliesNames;
    private HashMap<String, Ally> allAllies;

    public AlliesManager(){
        this.allAllies=new HashMap<>();
        this.allAlliesNames=new HashSet<>();
    }

    public HashMap<String, Ally> getAllAllies() {
        return allAllies;
    }

    public HashSet<String> getAllAlliesNames() {
        return allAlliesNames;
    }
    public synchronized  boolean addAlly(String name){

        if(!allAlliesNames.contains(name)){
            //if(allBattleFields.get(battle).equals(null)){
            allAllies.put(name,new Ally(name));
            allAlliesNames.add(name);
            return true;
        }
        return false;
    }
}
