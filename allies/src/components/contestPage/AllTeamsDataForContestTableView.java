package components.contestPage;

import javafx.beans.property.SimpleStringProperty;

public class AllTeamsDataForContestTableView {
    private SimpleStringProperty colTeam;
    private SimpleStringProperty colAgents;
    private SimpleStringProperty colTaskSize;

    public AllTeamsDataForContestTableView(String colTeam,String colAgents,String colTaskSize){
        this.colTeam=new SimpleStringProperty(colTeam);
        this.colAgents=new SimpleStringProperty(colAgents);
        this.colTaskSize=new SimpleStringProperty(colTaskSize);
    }

    public String getColTeam() {
        return colTeam.get();
    }

    public void setColTeam(String colTeam) {
        this.colTeam.set(colTeam);
    }

    public String getColAgents() {
        return colAgents.get();
    }

    public void setColAgents(String colAgents) {
        this.colAgents.set(colAgents);
    }

    public String getColTaskSize() {
        return colTaskSize.get();
    }

    public void setColTaskSize(String colTaskSize) {
        this.colTaskSize.set(colTaskSize);
    }
}
