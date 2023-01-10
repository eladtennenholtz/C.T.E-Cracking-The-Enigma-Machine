package components.login.modal;

import com.google.gson.JsonArray;

import java.util.ArrayList;

public class AllTeamsData {


    private ArrayList<String>allTeams;
    public AllTeamsData(){
        allTeams=new ArrayList<>();
    }
    public void setAllData(JsonArray asJsonArray) {
        for(int i=0;i<asJsonArray.size();i++){
            allTeams.add(asJsonArray.get(i).toString().substring(1,asJsonArray.get(i).toString().length()-1));
        }
    }

    public ArrayList<String> getAllTeams() {
        return allTeams;
    }
}
