package components.main.model;

import com.google.gson.JsonArray;
import data.DataTeamWebDto;

import java.util.ArrayList;

public class AllTeamsDataForContest {
    private ArrayList<String> teamsName;
    private ArrayList<Integer> numberOfAgents;
    private ArrayList<Integer> sizeOfTasks;

    public AllTeamsDataForContest(){
        teamsName=new ArrayList<>();
        numberOfAgents=new ArrayList<>();
        sizeOfTasks=new ArrayList<>();
    }

    public void setAllData(JsonArray jsonArray) {
        for(int i=0;i<jsonArray.size();i++){
            String[] res=jsonArray.get(i).toString().split(",");
            ArrayList<String>results=new ArrayList<>();
            for(String finalStr: res){
                String []another= finalStr.split(":");
                results.add(another[1]);
            }
            teamsName.add(results.get(0).substring(1,results.get(0).length()-1));
            numberOfAgents.add(Integer.parseInt(results.get(1)));
            StringBuilder stringBuilder=new StringBuilder();
            for(int j=0;j<results.get(2).length();j++){
                if(results.get(2).charAt(j)>='0'&& results.get(2).charAt(j)<='9') {
                    stringBuilder.append(results.get(2).charAt(j));
                }
            }
            sizeOfTasks.add(Integer.parseInt(stringBuilder.toString()));
        }

    }

    public ArrayList<String> getTeamsName() {
        return teamsName;
    }

    public ArrayList<Integer> getNumberOfAgents() {
        return numberOfAgents;
    }

    public ArrayList<Integer> getSizeOfTasks() {
        return sizeOfTasks;
    }

    public void setAllDataNew(DataTeamWebDto[] dataTeamWebDtos) {

        for(int i=0;i<dataTeamWebDtos.length;i++){
            teamsName.add(dataTeamWebDtos[i].getTeamName());
            numberOfAgents.add(dataTeamWebDtos[i].getNumberOfAgents());
            sizeOfTasks.add(dataTeamWebDtos[i].getSizeOfTask());
        }
    }
}
