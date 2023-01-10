package logic;

import data.DataCandidatesToAllies;
import data.DataCandidatesToUBoat;

import java.util.ArrayList;
import java.util.HashMap;

public class Ally {
    private String name;
    private ArrayList<String>agents;
    private HashMap<String,Agent>agentsMap;
    private String allyBattle;

    private ArrayList<String>agentsForCurrentCompetition;
    private HashMap<String,Agent>agentsMapForCurrentCompetition;
    private ArrayList<DataCandidatesToAllies>candidatesToAllies;
    private boolean readyToStartCompetition;
    private int sizeOfTask;
    private int totalTasks;
    private int positionInArray;
    private boolean finishedPressed;
    private boolean contestRealyStarted;


    public Ally(String name){
        this.name=name;
        this.agents=new ArrayList<>();
        this.agentsMap=new HashMap<>();
        this.agentsForCurrentCompetition=new ArrayList<>();
        this.agentsMapForCurrentCompetition=new HashMap<>();
        readyToStartCompetition=false;
        this.sizeOfTask=0;
        allyBattle="";
        candidatesToAllies=new ArrayList<>();
        positionInArray=0;
        finishedPressed=false;
        contestRealyStarted=false;

    }

    public String getName() {
        return name;
    }


    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public synchronized boolean addAgent(String nameOfAgent, Agent agent){
        if(!agents.contains(nameOfAgent)){
            if(!readyToStartCompetition){
                agentsForCurrentCompetition.add(nameOfAgent);
                agentsMapForCurrentCompetition.put(nameOfAgent,agent);
            }
            agents.add(nameOfAgent);
            agentsMap.put(nameOfAgent,agent);
            return true;
        }else{
            return false;
        }
    }

    public HashMap<String, Agent> getAgentsMap() {
        return agentsMap;
    }

    public ArrayList<String> getAgents() {
        return agents;
    }

    public ArrayList<String> getAgentsForCurrentCompetition() {
        return agentsForCurrentCompetition;
    }

    public HashMap<String, Agent> getAgentsMapForCurrentCompetition() {
        return agentsMapForCurrentCompetition;
    }

    public boolean isReadyToStartCompetition() {
        return readyToStartCompetition;
    }

    public void setReadyToStartCompetition(boolean readyToStartCompetition) {
        this.readyToStartCompetition = readyToStartCompetition;
    }

    public void setSizeOfTask(int sizeOfTask) {
        this.sizeOfTask = sizeOfTask;
    }

    public int getSizeOfTask() {
        return sizeOfTask;
    }

    public void setAllyBattle(String allyBattle) {
        this.allyBattle = allyBattle;
    }

    public String getAllyBattle() {
        return allyBattle;
    }
    public void addToDataCandidates(DataCandidatesToAllies dataCandidatesToAllies){
        this.candidatesToAllies.add(dataCandidatesToAllies);
    }

    public ArrayList<DataCandidatesToAllies> getCandidatesToAllies() {
        return candidatesToAllies;
    }

    public int getPositionInArray() {
        return positionInArray;
    }

    public void setPositionInArray(int positionInArray) {
        this.positionInArray = positionInArray;
    }

    public boolean isFinishedPressed() {
        return finishedPressed;
    }

    public void setFinishedPressed(boolean finishedPressed) {
        this.finishedPressed = finishedPressed;
    }

    public void contestRealyStarted(boolean result) {
        contestRealyStarted=result;
    }

    public boolean isContestRealyStarted() {
        return contestRealyStarted;
    }
}
