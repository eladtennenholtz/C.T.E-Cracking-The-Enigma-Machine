package components.main.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class AllCandidatesForUBoat {

    ArrayList<String>decryption;
    ArrayList<String>teams;
    ArrayList<String>configurations;

    public AllCandidatesForUBoat(){
        decryption=new ArrayList<>();
        teams=new ArrayList<>();
        configurations=new ArrayList<>();
    }


    public void setAllData(JsonArray jsonArray){

        for(int i=0;i<jsonArray.size();i++){

            JsonObject jsonObject=jsonArray.get(i).getAsJsonObject();
            String team=jsonObject.get("teamName").toString();
            System.out.println (team);
        }






    }
}
