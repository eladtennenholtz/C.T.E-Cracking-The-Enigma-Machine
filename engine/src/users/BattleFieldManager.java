package users;

import logic.BattleField;

import java.util.HashMap;
import java.util.HashSet;

public class BattleFieldManager {
    private  HashSet<String> allBattleFieldsNames;
    private  HashMap<String, BattleField>allBattleFields;

    public BattleFieldManager(){
        allBattleFieldsNames=new HashSet<>();
        allBattleFields=new HashMap<>();
    }
    public synchronized  boolean addBattle(String battle,BattleField battleField){

        if(!allBattleFieldsNames.contains(battle)){
        //if(allBattleFields.get(battle).equals(null)){
            allBattleFields.put(battle,battleField);
            allBattleFieldsNames.add(battle);
            return true;
        }
        return false;
    }
    public synchronized void removeBattle(String battle){
        if(allBattleFields.containsKey(battle)){
            allBattleFields.remove(battle);
            allBattleFieldsNames.remove(battle);
        }
    }

    public HashMap<String, BattleField> getAllBattleFields() {
        return allBattleFields;
    }

    public HashSet<String> getAllBattleFieldsNames() {
        return allBattleFieldsNames;
    }



}
