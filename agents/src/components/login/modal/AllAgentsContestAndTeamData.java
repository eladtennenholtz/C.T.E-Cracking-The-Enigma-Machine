package components.login.modal;

import com.google.gson.JsonArray;
import data.DataContestAndTeamForAgentDto;

import java.util.ArrayList;

public class AllAgentsContestAndTeamData {
    private String battleFieldName;
    private String uBoatName;
    private boolean status;
    private String level;
    private int alliesMax;
    private int alliesLogged;
    private String teamName;
    private int numberOfAgents;
    private int sizeOfTask;

    public void setAllData(JsonArray jsonArray) {
        for (int i = 0; i < jsonArray.size(); i++) {
            String[] res = jsonArray.get(i).toString().split(",");
            ArrayList<String> results = new ArrayList<>();
            for (String finalStr : res) {
                String[] another = finalStr.split(":");
                results.add(another[1]);
            }

            battleFieldName = results.get(0).substring(1, results.get(0).length() - 1);
            uBoatName = results.get(1).substring(1, results.get(1).length() - 1);
            String statusString = results.get(2).substring(0, results.get(2).length());
            if (statusString.equals("false")) {
                status = false;
            } else {
                status = true;
            }
            level = results.get(3).substring(1, results.get(3).length() - 1);
            alliesMax = (Integer.parseInt(results.get(4)));
            alliesLogged = (Integer.parseInt(String.valueOf(results.get(5).charAt(0))));
            teamName = (results.get(6).substring(1, results.get(6).length() - 1));
            numberOfAgents = (Integer.parseInt(results.get(7)));
            StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j < results.get(8).length(); j++) {
                if (results.get(8).charAt(j) >= '0' && results.get(8).charAt(j) <= '9') {
                    stringBuilder.append(results.get(8).charAt(j));
                }
            }
            sizeOfTask = (Integer.parseInt(stringBuilder.toString()));
        }
    }


    public void setBattleFieldName(String battleFieldName) {
        this.battleFieldName = battleFieldName;
    }

    public void setuBoatName(String uBoatName) {
        this.uBoatName = uBoatName;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setAlliesMax(int alliesMax) {
        this.alliesMax = alliesMax;
    }

    public void setAlliesLogged(int alliesLogged) {
        this.alliesLogged = alliesLogged;
    }

    public String getBattleFieldName() {
        return battleFieldName;
    }

    public String getuBoatName() {
        return uBoatName;
    }

    public boolean getStatus(){
        return status;
    }

    public String getLevel() {
        return level;
    }

    public int getAlliesMax() {
        return alliesMax;
    }

    public int getAlliesLogged() {
        return alliesLogged;
    }
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setNumberOfAgents(int numberOfAgents) {
        this.numberOfAgents = numberOfAgents;
    }

    public void setSizeOfTask(int sizeOfTask) {
        this.sizeOfTask = sizeOfTask;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getNumberOfAgents() {
        return numberOfAgents;
    }

    public int getSizeOfTask() {
        return sizeOfTask;
    }

    public void setAllDataNew(DataContestAndTeamForAgentDto[] dataContestAndTeamForAgentDtos) {

        for(int i=0;i<dataContestAndTeamForAgentDtos.length;i++){
            battleFieldName=dataContestAndTeamForAgentDtos[i].getBattleFieldName();
            uBoatName=dataContestAndTeamForAgentDtos[i].getuBoatName();
            status=dataContestAndTeamForAgentDtos[i].getStatus();
            level=dataContestAndTeamForAgentDtos[i].getLevel();
            alliesMax=dataContestAndTeamForAgentDtos[i].getAlliesMax();
            alliesLogged=dataContestAndTeamForAgentDtos[i].getAlliesLogged();
            teamName=dataContestAndTeamForAgentDtos[i].getTeamName();
            numberOfAgents=dataContestAndTeamForAgentDtos[i].getNumberOfAgents();
            sizeOfTask=dataContestAndTeamForAgentDtos[i].getSizeOfTask();
        }
    }
}
