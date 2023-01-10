package components.main.model;

import com.google.gson.JsonArray;
import data.DtoAgent;

import java.util.ArrayList;

public class AllTeamsAgentsData {

    private ArrayList<String> namesOfAgents;
    private ArrayList<Integer> numberOfThreads;
    private ArrayList<Integer> sizeOfTasks;

    public AllTeamsAgentsData() {
        namesOfAgents = new ArrayList<>();
        numberOfThreads = new ArrayList<>();
        sizeOfTasks = new ArrayList<>();
    }

    public void setAllData(JsonArray jsonArray) {

        for (int i = 0; i < jsonArray.size(); i++) {
            String[] res = jsonArray.get(i).toString().split(",");
            ArrayList<String> results = new ArrayList<>();
            for (String finalStr : res) {
                String[] another = finalStr.split(":");
                results.add(another[1]);
            }
            namesOfAgents.add(results.get(0).substring(1,results.get(0).length()-1));
            numberOfThreads.add(Integer.parseInt(results.get(2)));
            String forTasks=results.get(3);
            StringBuilder stringBuilder=new StringBuilder();
            for(int j=0;j<forTasks.length();j++){
                if(forTasks.charAt(j)>='0'&& forTasks.charAt(j)<='9') {
                    stringBuilder.append(forTasks.charAt(j));
                }
            }
            sizeOfTasks.add(Integer.parseInt(stringBuilder.toString()));
        }
    }

    public ArrayList<String> getNamesOfAgents() {
        return namesOfAgents;
    }

    public ArrayList<Integer> getNumberOfThreads() {
        return numberOfThreads;
    }

    public ArrayList<Integer> getSizeOfTasks() {
        return sizeOfTasks;
    }

    public void setAllDataNew(DtoAgent[] dtoAgents) {

        for(int i=0;i<dtoAgents.length;i++){
            namesOfAgents.add(dtoAgents[i].getNameAgent());
            numberOfThreads.add(dtoAgents[i].getNumberOfThreads());
            sizeOfTasks.add(dtoAgents[i].getSizeOfTasks());
        }

    }
}