package components.main.model;

import com.google.gson.JsonArray;
import data.DataContestWebDto;

import java.util.ArrayList;

public class AllContestsData {

    private ArrayList<String> battleFieldsName;
    private ArrayList<String> uBoatsName;
    private ArrayList<Boolean> statuses;
    private ArrayList<String> levels;
    private ArrayList<Integer> alliesMax;
    private ArrayList<Integer> alliesLogged;
    private boolean isEmpty;

    public AllContestsData(){

        battleFieldsName=new ArrayList<>();
        uBoatsName=new ArrayList<>();
        statuses=new ArrayList<>();
        levels=new ArrayList<>();
        alliesMax=new ArrayList<>();
        alliesLogged=new ArrayList<>();

    }

    public void setAllData(JsonArray jsonArray) {
        for(int i=0;i<jsonArray.size();i++){
            String[] res=jsonArray.get(i).toString().split(",");
            ArrayList<String>results=new ArrayList<>();
            for(String finalStr: res){
               String []another= finalStr.split(":");
               results.add(another[1]);
            }
            battleFieldsName.add(results.get(0).substring(1,results.get(0).length()-1));
            uBoatsName.add(results.get(1).substring(1,results.get(1).length()-1));
            String statusString=results.get(2).substring(0,results.get(2).length());
            if(statusString.equals("false")){
                statuses.add(false);
            }else{
                statuses.add(true);
            }
            levels.add(results.get(3).substring(1,results.get(3).length()-1));
            alliesMax.add(Integer.parseInt(results.get(4)));
            alliesLogged.add(Integer.parseInt(String.valueOf(results.get(5).charAt(0))));
        }
    }

    public void setAllDataNew(DataContestWebDto[]dataContestWebDtos){

        for(int i=0;i< dataContestWebDtos.length;i++){
            battleFieldsName.add(dataContestWebDtos[i].getBattleFieldName());
            uBoatsName.add(dataContestWebDtos[i].getuBoatName());
            if(dataContestWebDtos[i].getStatus()){
                statuses.add(true);
            }else{
                statuses.add(false);
            }
            levels.add(dataContestWebDtos[i].getLevel());
            alliesMax.add(dataContestWebDtos[i].getAlliesMax());
            alliesLogged.add(dataContestWebDtos[i].getAlliesLogged());
        }

    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public ArrayList<String> getBattleFieldsName() {
        return battleFieldsName;
    }

    public ArrayList<String> getUBoatsName() {
        return uBoatsName;
    }

    public ArrayList<Boolean> getStatuses() {
        return statuses;
    }

    public ArrayList<String> getLevels() {
        return levels;
    }

    public ArrayList<Integer> getAlliesMax() {
        return alliesMax;
    }

    public ArrayList<Integer> getAlliesLogged() {
        return alliesLogged;
    }

    public void setStatuses(ArrayList<Boolean> statuses) {
        this.statuses = statuses;
    }
}
