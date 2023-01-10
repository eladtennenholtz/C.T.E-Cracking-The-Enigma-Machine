package components.contestPage;

import javafx.beans.property.SimpleStringProperty;

public class AllTeamsAgentsProgressTableView {


    private SimpleStringProperty colAgentProgress;
    private SimpleStringProperty colTaskDone;
    private SimpleStringProperty colTaskRemain;
    private SimpleStringProperty colCandidates;

    public AllTeamsAgentsProgressTableView(String colAgentsProgress,String colTaskDone,String colTaskRemain,String colCandidates){
        this.colAgentProgress=new SimpleStringProperty(colAgentsProgress);
        this.colTaskDone=new SimpleStringProperty(colTaskDone);
        this.colTaskRemain=new SimpleStringProperty(colTaskRemain);
        this.colCandidates=new SimpleStringProperty(colCandidates);

    }

    public String getColAgentProgress() {
        return colAgentProgress.get();
    }

    public void setColAgentProgress(String colAgentProgress) {
        this.colAgentProgress.set(colAgentProgress);
    }

    public String getColTaskDone() {
        return colTaskDone.get();
    }

    public void setColTaskDone(String colTaskDone) {
        this.colTaskDone.set(colTaskDone);
    }

    public String getColTaskRemain() {
        return colTaskRemain.get();
    }

    public void setColTaskRemain(String colTaskRemain) {
        this.colTaskRemain.set(colTaskRemain);
    }

    public String getColCandidates() {
        return colCandidates.get();
    }

    public void setColCandidates(String colCandidates) {
        this.colCandidates.set(colCandidates);
    }
}
