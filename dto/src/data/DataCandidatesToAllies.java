package data;

public class DataCandidatesToAllies {

    private String agentName;
    private String configuration;
    private String decrypt;



    public DataCandidatesToAllies(String decrypt,String agentName,String configuration){
        this.agentName=agentName;
        this.configuration=configuration;
        this.decrypt=decrypt;
        setRotorsAndReflector();

        //this.configuration=configuration;
    }
    public void setRotorsAndReflector(){

        StringBuilder stringBuilder=new StringBuilder();
        int counter=0;
        String[]results=configuration.split(">");
        String rotors=results[0].substring(1);
        String positions=results[1].substring(1);
        String reflector=results[2].substring(1,results[2].length());
        stringBuilder.append(rotors+"\t"+positions+"\t"+reflector);
        this.configuration=stringBuilder.toString();
    }

    public String getDecrypt() {
        return decrypt;
    }

    public String getConfiguration() {
        return configuration;
    }

    public String getAgentName() {
        return agentName;
    }
}
