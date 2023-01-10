package components.login.modal;

import java.util.ArrayList;

public class AllCandidatesOfAgent {

    private ArrayList<String> configurations;
    private ArrayList<String> decrypted;
    private ArrayList<Integer> numberOfTasks;

    public AllCandidatesOfAgent(){
        this.decrypted=new ArrayList<>();
        this.configurations=new ArrayList<>();
        this.numberOfTasks=new ArrayList<>();
    }


    public void addDecrypted(String decrypted) {
        this.decrypted.add(decrypted);
    }
    public void addConfiguration(String configuration){
        this.configurations.add(configuration);
    }
    public void addNumberOfTasks(int number){
        this.numberOfTasks.add(number);
    }

    public ArrayList<String> getDecrypted() {
        return decrypted;
    }

    public ArrayList<String> getConfigurations() {
        return configurations;
    }

    public ArrayList<Integer> getNumberOfTasks() {
        return numberOfTasks;
    }
}
