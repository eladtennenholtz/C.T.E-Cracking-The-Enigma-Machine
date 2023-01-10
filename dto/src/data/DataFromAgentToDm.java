package data;

public class DataFromAgentToDm {

    private String configuration;
    private String decrypted;
    private double numberOfTask;
    private String teamName;
    private String agentName;

    public DataFromAgentToDm(String decrypted,String configuration,double numberOfTask){
        //this.numberOfThread=numberOfThread;
        this.decrypted=decrypted;
        this.configuration=configuration;
        this.numberOfTask=numberOfTask;
    }


    public String getDecrypted() {
        return decrypted;
    }

    public String getConfiguration() {
        return configuration;
    }

    public double getNumberOfTask() {
        return numberOfTask;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentName() {
        return agentName;
    }
}
