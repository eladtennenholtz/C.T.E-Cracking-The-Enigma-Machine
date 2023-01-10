package data;

public class DataAgentsProgressToAllies {

    private String agentName;
    private int tasksDone;
    private int tasksRemain;
    private int candidates;

    public DataAgentsProgressToAllies(String agentName,int tasksDone,int tasksRemain,int candidates){
        this.agentName=agentName;
        this.tasksDone=tasksDone;
        this.tasksRemain=tasksRemain;
        this.candidates=candidates;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public int getTasksDone() {
        return tasksDone;
    }

    public void setTasksDone(int tasksDone) {
        this.tasksDone = tasksDone;
    }

    public int getTasksRemain() {
        return tasksRemain;
    }

    public void setTasksRemain(int tasksRemain) {
        this.tasksRemain = tasksRemain;
    }

    public int getCandidates() {
        return candidates;
    }

    public void setCandidates(int candidates) {
        this.candidates = candidates;
    }
}
