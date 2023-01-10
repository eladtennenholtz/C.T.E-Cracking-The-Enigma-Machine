package users;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class UserUBoatManager {


    private final Set<String> usersSet;
    private static HashSet<String>allBattleFields;
    private static HashMap<String,String>allBattleFieldsMap;

    public UserUBoatManager() {
        usersSet = new HashSet<>();
        allBattleFields=new HashSet<>();
        allBattleFieldsMap=new HashMap<>();

    }


    public synchronized void addUser(String username) {
        usersSet.add(username);
    }

    public synchronized void removeUser(String username) {
        usersSet.remove(username);
    }

    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public boolean isUserExists(String username) {
        return usersSet.contains(username);
    }


    public static HashSet<String> getAllBattleFields() {
        return allBattleFields;
    }

    public synchronized static boolean addBattle(String battle, String UBoatName){
        if(!allBattleFields.contains(battle)){
            allBattleFields.add(battle);
            allBattleFieldsMap.put(UBoatName,battle);
            return true;
        }
        return false;
    }
    public synchronized void removeBattle(String battle,String userName){
        if(allBattleFields.contains(battle)){
            allBattleFields.remove(battle);
            allBattleFieldsMap.remove(userName);
        }
    }

    public static HashMap<String, String> getAllBattleFieldsMap() {
        return allBattleFieldsMap;
    }

}
