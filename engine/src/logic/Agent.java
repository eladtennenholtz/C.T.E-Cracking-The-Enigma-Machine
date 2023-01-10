package logic;

public class Agent {

    private String nameAgent;
    private String nameAllyTeam;
    private int numberOfThreads;
    private int sizeOfTasks;

    private int candidates;
    private int taskPreformed;
    private int taskRemain;

    public Agent(String nameAgent,int numberOfThreads,int sizeOfTasks,String nameAllyTeam){
        this.nameAgent=nameAgent;
        this.nameAllyTeam=nameAllyTeam;
        this.numberOfThreads=numberOfThreads;
        this.sizeOfTasks=sizeOfTasks;
        this.candidates=0;
        this.taskPreformed=0;
        this.taskRemain=0;
    }

    public String getNameAgent() {
        return nameAgent;
    }

    public String getNameAllyTeam() {
        return nameAllyTeam;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public int getSizeOfTasks() {
        return sizeOfTasks;
    }

    public void setCandidates(int candidates) {
        this.candidates = candidates;
    }

    public int getCandidates() {
        return candidates;
    }

    public void setTaskPreformed(int taskPreformed) {
        this.taskPreformed = taskPreformed;
    }

    public int getTaskPreformed() {
        return taskPreformed;
    }

    public void setTaskRemain(int taskRemain) {
        this.taskRemain = taskRemain;
    }

    public int getTaskRemain() {
        return taskRemain;
    }
}
